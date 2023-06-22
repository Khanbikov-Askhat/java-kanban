package manager;
import task.Epic;
import task.Subtask;
import task.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager{

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private int generatorId = 0;


    //ДОБАВЛЕНИЕ НОВОГО ТАСКА
    @Override
    public int addTask(Task task) {
        int id = ++generatorId;

        task.setId(id);
        tasks.put(id, task);
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

        if (epic == null) {
            return null;
        }

        int id = ++generatorId;
        subtask.setId(id);
        subtasks.put(id, subtask);
        epics.get(epicId).setSubtaskIds(id);
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
        tasks.clear();
    }


    //УДАЛЕНИЕ ВСЕХ ЭПИКОВ
    @Override
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }


    //УДАЛЕНИЕ ВСЕХ ПОДЗАДАЧ
    @Override
    public void deleteSubtasks() {
        for (Epic epic: epics.values()) {
            epic.cleanSubtaskIds();
            checkEpicStatus(epic.getEpicId());
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
        int id = task.getId();
        Task saveTask = tasks.get(id);
        task.setStatus(newStatus);
        if (saveTask == null) {
            return;
        }
        task.setId(id);
        tasks.put(id, task);
    }


    //ОБНОВЛЕНИЕ ЭПИКА
    @Override
    public void updateEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
    }


    //ОБНОВЛЕНИЕ ПОДЗАДАЧИ
    @Override
    public void updateSubtask(Subtask subtask, String newStatus) {
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
        checkEpicStatus(epicId);
    }


    //УДАЛЕНИЕ ЗАДАЧИ ПО ИНДЕНТИФИКАТОРУ
    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }


    //УДАЛЕНИЕ ЭПИКА ПО ИНДЕНТИФИКАТОРУ
    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
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
                if (subtasks.get(key).getStatus().equals("DONE")) {
                    statusDone += 1;
                } else if (subtasks.get(key).getStatus().equals("NEW")) {
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

}
