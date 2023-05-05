package manager;
import task.Epic;
import task.Subtask;
import task.Task;
import java.util.ArrayList;
import java.util.HashMap;


public class TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int generatorId = 0;


    //ДОБАВЛЕНИЕ НОВОГО ТАСКА
    public int addTask(Task task) {
        int id = ++generatorId;

        task.setId(id);
        tasks.put(id, task);
        return id;
    }


    //ДОБАВЛЕНИЕ НОВОГО ЭПИКА
    public int addEpic(Epic epic) {
        int id = ++generatorId;

        epic.setId(id);
        epics.put(id, epic);
        return id;
    }


    //ДОБАВЛЕНИЕ ПОДЗАДАЧИ
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
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }


    //ПОЛУЧЕНИЕ ВСЕХ ЭПИКОВ
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }


    //ПОЛУЧЕНИЕ ВСЕХ ПОДЗАДАЧ
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }


    //УДАЛЕНИЕ ВСЕХ ЗАДАЧ
    public void deleteTasks() {
        tasks.clear();
    }


    //УДАЛЕНИЕ ВСЕХ ЭПИКОВ
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }


    //УДАЛЕНИЕ ВСЕХ ПОДЗАДАЧ
    public void deleteSubtasks() {
        for (Epic epic: epics.values()) {
            epic.cleanSubtaskIds();
            checkEpicStatus(epic.getEpicId());
        }
        subtasks.clear();
    }


    //ПОЛУЧЕНИЕ ЗАДАЧИ ПО ИДЕНТИФИКАТОРУ
    public Task getTask(int id) {
        return tasks.get(id);
    }


    //ПОЛУЧЕНИЕ ЭПИКА ПО ИДЕНТИФИКАТОРУ
    public Epic getEpic(int id) {
        return epics.get(id);
    }


    //ПОЛУЧЕНИЕ ПОДЗАДАЧИ ПО ИДЕНТИФИКАТОРУ
    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }


    //ОБНОВЛЕНИЕ ЗАДАЧИ
    public void updateTask(Task task) {
        int id = task.getId();
        Task saveTask = tasks.get(id);
        if (saveTask == null) {
            return;
        }
        task.setId(id);
        tasks.put(id, task);
    }


    //ОБНОВЛЕНИЕ ЭПИКА
    public void updateEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
    }


    //ОБНОВЛЕНИЕ ПОДЗАДАЧИ
    public void updateSubtask(Subtask subtask) {
        final int id = subtask.getId();
        final int epicId = subtask.getEpicId();
        final Subtask savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            return;
        }
        final Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }
        subtasks.put(id, subtask);
        checkEpicStatus(epicId);
    }


    //УДАЛЕНИЕ ЗАДАЧИ ПО ИНДЕНТИФИКАТОРУ
    public void deleteTask(int id) {
        tasks.remove(id);
    }


    //УДАЛЕНИЕ ЭПИКА ПО ИНДЕНТИФИКАТОРУ
    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        for (Integer subtaskId : epic.getSubtaskIds()) {
            subtasks.remove(subtaskId);
        }
    }


    //УДАЛЕНИЕ ПОДЗАДАЧИ ПО ИНДЕНТИФИКАТОРУ
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtask(id);
        checkEpicStatus(epic.getId());
    }


    //ПОЛУЧЕНИЕ ВСЕХ ПОДЗАДАЧ ОПРЕДЕЛЕННОГО ЭПИКА
    public ArrayList<Subtask> getEpicsSubtasks(int epicId) {
        ArrayList<Subtask> epicsSubtasks = new ArrayList<>();

        for (Integer key: subtasks.keySet()) {
            if (subtasks.get(key).getEpicId() == epicId) {
                epicsSubtasks.add(subtasks.get(key));
            }
        }
        return epicsSubtasks;
    }


    //ПРОВЕРКА СТАТУСОВ У ЭПИКОВ
    public void checkEpicStatus(int epicId) {
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
