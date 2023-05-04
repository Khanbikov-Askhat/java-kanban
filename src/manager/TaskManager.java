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
    public int addSubtask(Subtask subtask) {
        int id = ++generatorId;

        subtask.setId(id);
        subtasks.put(id, subtask);

        for (Integer key: epics.keySet()) {
            if (key == subtask.getEpicId()) {
                epics.get(key).setSubtaskIds(id);
            }
            checkEpicStatus(key);
        }
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
        if (tasks.values().isEmpty()) {
            return;
        } else {
            tasks.clear();
        }
    }


    //УДАЛЕНИЕ ВСЕХ ЭПИКОВ
    public void deleteEpics() {
        if (epics.values().isEmpty()) {
            return;
        } else {
            epics.clear();
        }
    }


    //УДАЛЕНИЕ ВСЕХ ПОДЗАДАЧ
    public void deleteSubtasks() {
        if (subtasks.values().isEmpty()) {
            return;
        } else {
            subtasks.clear();
        }
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
    public void updateTask(Task task, int id) {
        Task saveTask = tasks.get(id);
        if (saveTask == null) {
            return;
        }
        task.setId(id);
        tasks.put(id, task);
    }


    //ОБНОВЛЕНИЕ ЭПИКА
    public void updateEpic(Epic epic, int id) {
        Epic saveTask = epics.get(id);
        if (saveTask == null) {
            return;
        }
        epics.put(id, epic);
        epic.setId(id);
        for (Integer key: subtasks.keySet()) {
            if (subtasks.get(key).getEpicId() == id) {
                epic.setSubtaskIds(key);
            }
        }
        checkEpicStatus(epic.getEpicId());
    }


    //ОБНОВЛЕНИЕ ПОДЗАДАЧИ
    public void updateSubtask(Subtask subtask, int id) {
        Subtask saveTask = subtasks.get(id);
        if (saveTask == null) {
            return;
        }
        subtask.setId(id);
        subtasks.put(id, subtask);
        checkEpicStatus(subtask.getEpicId());
    }


    //УДАЛЕНИЕ ЗАДАЧИ ПО ИНДЕНТИФИКАТОРУ
    public void deleteTask(int id) {
        for (Integer key : tasks.keySet()) {
            if (key == id) {
                tasks.remove(id);
                return;
            } else {
                return;
            }
        }
    }


    //УДАЛЕНИЕ ЭПИКА ПО ИНДЕНТИФИКАТОРУ
    public void deleteEpic(int id) {
        for (Integer key : epics.keySet()) {
            if (key == id) {
                epics.remove(id);
                return;
            } else {
                return;
            }
        }
    }


    //УДАЛЕНИЕ ПОДЗАДАЧИ ПО ИНДЕНТИФИКАТОРУ
    public void deleteSubtask(int id) {
        for (Integer key : subtasks.keySet()) {
            if (key == id) {
                subtasks.remove(id);
                return;
            } else {
                return;
            }
        }
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
