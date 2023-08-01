package manager;
import exceptions.TaskValidationException;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected final HashMap<Integer, Task> tasks;
    protected final HashMap<Integer, Epic> epics;
    protected final HashMap<Integer, Subtask> subtasks;
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    private final TreeSet<Task> prioritizedTasks;
    protected int generatorId = 0;



    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.generatorId = 0;
        prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }


    //ДОБАВЛЕНИЕ НОВОГО ТАСКА
    @Override
    public Integer addTask(Task task) {
        int id = ++generatorId;

        task.setId(id);
        tasks.put(id, task);
        add(task);
        return id;
    }


    //ДОБАВЛЕНИЕ НОВОГО ЭПИКА
    @Override
    public Integer addEpic(Epic epic) {
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

        if (epic == null) {
            return null;
        }

        int id = ++generatorId;
        subtask.setId(id);
        subtasks.put(id, subtask);
        epics.get(epicId).setSubtaskIds(id);
        add(subtask);
        updateEpic(epicId);

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
            prioritizedTasks.remove(task);
        }
        tasks.clear();
    }


    //УДАЛЕНИЕ ВСЕХ ЭПИКОВ
    @Override
    public void deleteEpics() {
        for (Epic epic: epics.values()) {
            prioritizedTasks.remove(epic);
        }
        epics.clear();
        for (Subtask subtask: subtasks.values()) {
            prioritizedTasks.remove(subtask);
        }
        subtasks.clear();
    }


    //УДАЛЕНИЕ ВСЕХ ПОДЗАДАЧ
    @Override
    public void deleteSubtasks() {
        for (Epic epic: epics.values()) {
            epic.cleanSubtaskIds();
            updateEpic(epic.getEpicId());
        }
        for (Subtask subtask: subtasks.values()) {
            prioritizedTasks.remove(subtask);
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
    public boolean updateTask(Task task, String newStatus) {
        if (task == null) {
            return false;
        }
        int id = task.getId();
        Task saveTask = tasks.get(id);
        task.setStatus(newStatus);
        if (saveTask == null) {
            return false;
        }
        task.setId(id);
        tasks.put(id, task);
        prioritizedTasks.remove(saveTask);
        add(task);
        return true;
    }


    //ОБНОВЛЕНИЕ ЭПИКА
    @Override
    public boolean updateEpic(Epic epic) {
        if (epic == null) {
            return false;
        }
        Epic savedEpic = epics.get(epic.getId());
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
        return true;
    }


    //ОБНОВЛЕНИЕ ПОДЗАДАЧИ
    @Override
    public boolean updateSubtask(Subtask subtask, String newStatus) {
        if (subtask == null) {
            return false;
        }
        int id = subtask.getId();
        int epicId = subtask.getEpicId();
        Subtask savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            return false;
        }
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return false;
        }
        subtask.setStatus(newStatus);
        subtasks.put(id, subtask);
        prioritizedTasks.remove(subtask);
        add(subtask);
        updateEpic(epicId);
        return true;
    }


    //УДАЛЕНИЕ ЗАДАЧИ ПО ИНДЕНТИФИКАТОРУ
    @Override
    public boolean deleteTask(int id) {
        Task task = tasks.remove(id);
        if (task == null) {
            return false;
        }
        prioritizedTasks.remove(task);
        historyManager.remove(id);
        return true;
    }


    //УДАЛЕНИЕ ЭПИКА ПО ИНДЕНТИФИКАТОРУ
    @Override
    public boolean deleteEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic == null) {
            return false;
        }
        for (Integer subtaskId : epic.getSubtaskIds()) {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
        historyManager.remove(id);
        return true;
    }


    //УДАЛЕНИЕ ПОДЗАДАЧИ ПО ИНДЕНТИФИКАТОРУ
    @Override
    public boolean deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return false;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtask(id);
        updateEpic(epic.getId());
        historyManager.remove(id);
        return true;
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

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    protected void updateEpic(int epicId) {
        Epic epic = epics.get(epicId);
        checkEpicStatus(epicId);
        updateEpicDuration(epic);
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

    private void updateEpicDuration(Epic epic) {
        List<Integer> subs = epic.getSubtaskIds();
        if (subs.isEmpty()) {
            epic.setDuration(0L);
            return;
        }
        LocalDateTime start = LocalDateTime.MAX;
        LocalDateTime end = LocalDateTime.MIN;
        long duration = 0L;
        for (int id : subs) {
            final Subtask subtask = subtasks.get(id);
            final LocalDateTime startTime = subtask.getStartTime();
            final LocalDateTime endTime = subtask.getEndTime();
            if (startTime.isBefore(start)) {
                start = startTime;
            }
            if (endTime.isAfter(end)) {
                end = endTime;
            }
            duration += subtask.getDuration();
        }
        epic.setDuration(duration);
        epic.setStartTime(start);
        epic.setEndTime(end);
    }

    public void add(Task task) {
        final LocalDateTime startTime = task.getStartTime();
        final LocalDateTime endTime = task.getEndTime();
        for (Task t : prioritizedTasks) {
            final LocalDateTime existStart = t.getStartTime();
            final LocalDateTime existEnd = t.getEndTime();
            if (!endTime.isAfter(existStart)) {
                continue;
            }
            if (!existEnd.isAfter(startTime)) {
                continue;
            }
            throw new TaskValidationException("Задача пересекаются с id=" + t.getId() + " c " + existStart + " по " + existEnd);
        }
        prioritizedTasks.add(task);
    }
}
