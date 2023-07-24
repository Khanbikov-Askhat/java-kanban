package manager;
import exceptions.TaskOverlapAnotherTaskException;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected final HashMap<Integer, Task> tasks;
    protected final HashMap<Integer, Epic> epics;
    protected final HashMap<Integer, Subtask> subtasks;
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    private final TreeSet<Task> tasksSortedByStartTime;
    protected int generatorId = 0;



    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.generatorId = 0;
        tasksSortedByStartTime = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }


    //ДОБАВЛЕНИЕ НОВОГО ТАСКА
    @Override
    public Integer addTask(Task task) {
        if (taskOverlapTasks(task)) {
            return null;
        }
        int id = ++generatorId;

        task.setId(id);
        tasks.put(id, task);
        tasksSortedByStartTime.add(task);
        return id;
    }


    //ДОБАВЛЕНИЕ НОВОГО ЭПИКА
    @Override
    public int addEpic(Epic epic) {
        int id = ++generatorId;

        epic.setId(id);
        epics.put(id, epic);
        return id;
    }


    //ДОБАВЛЕНИЕ ПОДЗАДАЧИ
    @Override
    public Integer addSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);

        if (epic == null || taskOverlapTasks(subtask)) {
            return null;
        }

        int id = ++generatorId;
        subtask.setId(id);
        subtasks.put(id, subtask);
        epics.get(epicId).setSubtaskIds(id);
        tasksSortedByStartTime.add(subtask);
        setDurationStartTimeAndEndTimeOfEpic(epic, subtask);
        checkEpicStatus(epicId);

        return id;
    }


    //ПОЛУЧЕНИЕ ВСЕХ ЗАДАЧ
    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }


    //ПОЛУЧЕНИЕ ВСЕХ ЭПИКОВ
    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }


    //ПОЛУЧЕНИЕ ВСЕХ ПОДЗАДАЧ
    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }


    //УДАЛЕНИЕ ВСЕХ ЗАДАЧ
    @Override
    public void deleteTasks() {
        for (Task task : tasks.values()) {
            tasksSortedByStartTime.remove(task);
        }
        tasks.clear();

    }


    //УДАЛЕНИЕ ВСЕХ ЭПИКОВ
    @Override
    public void deleteEpics() {
        for (Epic epic: epics.values()) {
            tasksSortedByStartTime.remove(epic);
        }
        epics.clear();
        for (Subtask subtask: subtasks.values()) {
            tasksSortedByStartTime.remove(subtask);
        }
        subtasks.clear();
    }


    //УДАЛЕНИЕ ВСЕХ ПОДЗАДАЧ
    @Override
    public void deleteSubtasks() {
        for (Epic epic: epics.values()) {
            epic.cleanSubtaskIds();
            checkEpicStatus(epic.getEpicId());
            epic.setDuration(Duration.parse("PT0M"));
        }
        for (Subtask subtask: subtasks.values()) {
            tasksSortedByStartTime.remove(subtask);
        }
        subtasks.clear();
    }


    //ПОЛУЧЕНИЕ ЗАДАЧИ ПО ИДЕНТИФИКАТОРУ
    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }


    //ПОЛУЧЕНИЕ ЭПИКА ПО ИДЕНТИФИКАТОРУ
    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }


    //ПОЛУЧЕНИЕ ПОДЗАДАЧИ ПО ИДЕНТИФИКАТОРУ
    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }


    //ОБНОВЛЕНИЕ ЗАДАЧИ
    @Override
    public void updateTask(Task task, String newStatus) {
        if (task == null || taskOverlapTasks(task)) {
            return;
        }
        int id = task.getId();
        Task saveTask = tasks.get(id);
        task.setStatus(newStatus);
        if (saveTask == null) {
            return;
        }
        task.setId(id);
        tasks.put(id, task);
        tasksSortedByStartTime.remove(saveTask);
        tasksSortedByStartTime.add(task);
    }


    //ОБНОВЛЕНИЕ ЭПИКА
    @Override
    public void updateEpic(Epic epic) {
        if (epic == null) {
            return;
        }
        Epic savedEpic = epics.get(epic.getId());
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
    }


    //ОБНОВЛЕНИЕ ПОДЗАДАЧИ
    @Override
    public void updateSubtask(Subtask subtask, String newStatus) {
        if (subtask == null || taskOverlapTasks(subtask)) {
            return;
        }
        int id = subtask.getId();
        int epicId = subtask.getEpicId();
        Subtask savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            return;
        }
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }
        subtask.setStatus(newStatus);
        subtasks.put(id, subtask);
        epic.subtractionDuration(savedSubtask.getDuration());
        setDurationStartTimeAndEndTimeOfEpic(epic, subtask);
        tasksSortedByStartTime.remove(subtask);
        tasksSortedByStartTime.add(subtask);
        checkEpicStatus(epicId);
    }


    //УДАЛЕНИЕ ЗАДАЧИ ПО ИНДЕНТИФИКАТОРУ
    @Override
    public void deleteTask(int id) {
        Task task = tasks.remove(id);
        if (task == null) {
            return;
        }
        tasksSortedByStartTime.remove(task);
        //tasks.remove(id);
        historyManager.remove(id);
    }


    //УДАЛЕНИЕ ЭПИКА ПО ИНДЕНТИФИКАТОРУ
    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic == null) {
            return;
        }
        for (Integer subtaskId : epic.getSubtaskIds()) {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
        historyManager.remove(id);
    }


    //УДАЛЕНИЕ ПОДЗАДАЧИ ПО ИНДЕНТИФИКАТОРУ
    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtask(id);
        checkEpicStatus(epic.getId());
        epic.subtractionDuration(subtask.getDuration());
        setTimeAndEndTimeOfEpic(epic);
        historyManager.remove(id);
    }


    //ПОЛУЧЕНИЕ ВСЕХ ПОДЗАДАЧ ОПРЕДЕЛЕННОГО ЭПИКА
    @Override
    public ArrayList<Subtask> getEpicsSubtasks(int epicId) {
        ArrayList<Subtask> epicsSubtasks = new ArrayList<>();

        for (Integer key: subtasks.keySet()) {
            if (subtasks.get(key).getEpicId() == epicId) {
                epicsSubtasks.add(subtasks.get(key));
            }
        }
        return epicsSubtasks;
    }


    //ПОЛУЧЕНИЕ ИСТОРИИ ИЗМЕНЕНИЙ
    @Override
    public List<Task> getDefaultHistory() {
        return historyManager.getHistory();
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }


    //ПРОВЕРКА СТАТУСОВ У ЭПИКОВ
    private void checkEpicStatus(int epicId) {
        int statusDone = 0;
        int statusNew = 0;

        if (epics.get(epicId).getSubtaskIdsSize() == 0) {
            epics.get(epicId).setStatus("NEW");
            return;
        }
        for (Integer key: subtasks.keySet()) {
            if (subtasks.get(key).getEpicId() == epicId) {
                if (subtasks.get(key).getStatus() == TaskStatus.DONE) {
                    statusDone += 1;
                } else if (subtasks.get(key).getStatus() == TaskStatus.NEW) {
                    statusNew += 1;
                }
            }
        }
        if (statusDone == epics.get(epicId).getSubtaskIdsSize()) {
            epics.get(epicId).setStatus("DONE");
        } else if (statusNew == epics.get(epicId).getSubtaskIdsSize()) {
            epics.get(epicId).setStatus("NEW");
        } else {
            epics.get(epicId).setStatus("IN_PROGRESS");
        }
    }





    private void setTimeAndEndTimeOfEpic(Epic epic) {
        ArrayList<Integer> subTasksOfFoundedEpic = new ArrayList<>(epic.getSubtaskIds());
        epic.setStartTime(LocalDateTime.MAX);
        epic.setEndTime(LocalDateTime.MIN);
        for (Integer subTaskId : subTasksOfFoundedEpic) {
            Subtask subTask = subtasks.get(subTaskId);
            LocalDateTime startTimeOfEpic = epic.getStartTime();
            LocalDateTime startTimeOfSubtask = subTask.getStartTime();
            LocalDateTime endTimeOfEpic = epic.getEndTime();
            LocalDateTime endTimeOfSubtask = subTask.getEndTime();
            if (startTimeOfEpic.isAfter(startTimeOfSubtask)) {
                epic.setStartTime(startTimeOfSubtask);
            }
            if (endTimeOfEpic.isBefore(endTimeOfSubtask)) {
                epic.setEndTime(endTimeOfSubtask);
            }
        }
    }

    public void setDurationStartTimeAndEndTimeOfEpic(Epic epic, Subtask subTask) {
        epic.increaseDuration(subTask.getDuration());
        LocalDateTime startTimeOfEpic = epic.getStartTime();
        LocalDateTime startTimeOfSubtask = subTask.getStartTime();
        LocalDateTime endTimeOfEpic = epic.getEndTime();
        LocalDateTime endTimeOfSubtask = subTask.getEndTime();
        if (startTimeOfEpic.isAfter(startTimeOfSubtask)) {
            epic.setStartTime(startTimeOfSubtask);
        }
        if (endTimeOfEpic.isBefore(endTimeOfSubtask)) {
            epic.setEndTime(endTimeOfSubtask);
        }
    }

    public boolean taskOverlapTasks(Task newTask) {
        for (Task task : tasksSortedByStartTime) {
            if (task.getId() == newTask.getId()) {
                if (task.equals(newTask)) {
                    return true;
                } else {
                    continue;
                }
            }
            if (taskOverlapAnotherTask(newTask, task)) {
                return true;
            }
        }
        return false;
    }

    public boolean taskOverlapAnotherTask(Task checkedTask, Task anotherTask) {
        LocalDateTime checkedTaskStartTime = checkedTask.getStartTime();
        LocalDateTime checkedTaskEndTime = checkedTask.getEndTime();
        LocalDateTime taskStartTime = anotherTask.getStartTime();
        LocalDateTime taskEndTime = anotherTask.getEndTime();
        return checkedTaskStartTime.isEqual(taskStartTime) && checkedTaskEndTime.isEqual(taskEndTime) ||
                (checkedTaskStartTime.isBefore(taskEndTime)) && (taskStartTime.isBefore(checkedTaskEndTime));
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(tasksSortedByStartTime);
    }

}
