package manager;

import exceptions.ManagerSaveException;
import task.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {

        TaskManager managerFirst = Managers.getDefault();

        //Создание
        System.out.println("Создание");
        Task task1 = new Task("Task #1", "Task1 description");
        Task task2 = new Task("Task #2", "Task2 description");
        int taskId1 = managerFirst.addTask(task1);
        int taskId2 = managerFirst.addTask(task2);
        Epic epic1 = new Epic("Epic #1", "Epic1 description");
        Epic epic2 = new Epic("Epic #2", "Epic2 description");
        int epicId1 = managerFirst.addEpic(epic1);
        int epicId2 = managerFirst.addEpic(epic2);
        Subtask subtask1 = new Subtask("Subtask #1-1", "Subtask1 description", epicId1);
        Subtask subtask2 = new Subtask("Subtask #2-1", "Subtask2 description", epicId1);
        Subtask subtask3 = new Subtask("Subtask #3-1", "Subtask3 description", epicId1);
        Integer subtaskId1 = managerFirst.addSubtask(subtask1);
        Integer subtaskId2 = managerFirst.addSubtask(subtask2);
        Integer subtaskId3 = managerFirst.addSubtask(subtask3);

        // Получение задач по ID
        System.out.println("Получение задач по ID в первый раз");
        System.out.println(managerFirst.getTask(taskId1));
        System.out.println(managerFirst.getTask(taskId2));
        System.out.println(managerFirst.getEpic(epicId1));
        System.out.println(managerFirst.getEpic(epicId2));
        System.out.println(managerFirst.getSubtask(subtaskId1));
        System.out.println(managerFirst.getSubtask(subtaskId2));
        System.out.println(managerFirst.getSubtask(subtaskId3));
        System.out.println();


        //Принт всех задач
        System.out.println(managerFirst.getTasks());
        System.out.println(managerFirst.getEpics());
        System.out.println(managerFirst.getSubtasks());
        System.out.println();

        // Получение истории задач
        System.out.println("Получение истории задач в первый раз");
        System.out.println(managerFirst.getDefaultHistory());
        System.out.println();


        File save = new File("./resources/task.csv");
        final TaskManager historyTaskManager = FileBackedTasksManager.loadFromFile(save);

        // Получение истории задач из файла
        System.out.println("Получение истории задач из файла");
        System.out.println(historyTaskManager.getDefaultHistory());
        System.out.println();


        //Принт всех задач
        System.out.println(historyTaskManager.getTasks());
        System.out.println(historyTaskManager.getEpics());
        System.out.println(historyTaskManager.getSubtasks());
        System.out.println();


        // Получение задач по ID
        System.out.println("Получение задач по ID в первый раз");
        System.out.println(historyTaskManager.getTask(taskId1));
        System.out.println(historyTaskManager.getTask(taskId2));
        System.out.println(historyTaskManager.getEpic(epicId1));
        System.out.println(historyTaskManager.getEpic(epicId2));
        System.out.println(historyTaskManager.getSubtask(subtaskId1));
        System.out.println(historyTaskManager.getSubtask(subtaskId2));
        System.out.println(historyTaskManager.getSubtask(subtaskId3));
        System.out.println();

    }

    //СОХРАНЕНИЕ ФАЙЛА
    protected void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(CSVTaskFormat.getHeader());
            writer.newLine();
            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                final Task task = entry.getValue();
                writer.write(CSVTaskFormat.toString(task));
                writer.newLine();
            }
            for (Map.Entry<Integer, Subtask> entry : subtasks.entrySet()) {
                final Task task = entry.getValue();
                writer.write(CSVTaskFormat.toString(task));
                writer.newLine();
            }
            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                final Task task = entry.getValue();
                writer.write(CSVTaskFormat.toString(task));
                writer.newLine();
            }
            writer.newLine();
            writer.write(CSVTaskFormat.toString(historyManager));
            writer.newLine();
        } catch (IOException e) {
            throw new ManagerSaveException("Can't save to file: " + file.getName(), e);
        }
    }


    @Override
    public Integer addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
        return subtask.getId();
    }

    @Override
    public int addTask(Task task) {
        super.addTask(task);
        save();
        return task.getId();
    }

    @Override
    public int addEpic(Epic epic) {
        super.addEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        if (task != null) {
            save();
            return task;
        } else {
            return null;
        }
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        if (epic != null) {
            save();
            return epic;
        } else {
            return null;
        }
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subTask = super.getSubtask(id);
        if (subTask != null) {
            save();
            return subTask;
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<Task> getTasks() {
        return super.getTasks();
    }


    //ПОЛУЧЕНИЕ ВСЕХ ЭПИКОВ
    @Override
    public ArrayList<Epic> getEpics() {
        return super.getEpics();
    }


    //ПОЛУЧЕНИЕ ВСЕХ ПОДЗАДАЧ
    @Override
    public ArrayList<Subtask> getSubtasks() {
        return super.getSubtasks();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        final FileBackedTasksManager taskManager = new FileBackedTasksManager(file);
        try {
            final String csv = Files.readString(file.toPath());
            final String[] lines = csv.split(System.lineSeparator());
            int generatorId = 0;
            List<Integer> history = Collections.emptyList();
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                if (line.isEmpty()) {
                    history = CSVTaskFormat.historyFromString(lines[i + 1]);
                    break;
                }
                final Task task = CSVTaskFormat.tasksFromString(line);
                final int id = task.getId();
                if (id > generatorId) {
                    generatorId = id;
                }
                taskManager.addAnyTask(task);
            }
            for (Map.Entry<Integer, Subtask> e : taskManager.subtasks.entrySet()) {
                final Subtask subtask = e.getValue();
                final Epic epic = taskManager.epics.get(subtask.getEpicId());
                epic.setSubtaskIds(subtask.getId());
            }
            for (Integer taskId : history) {
                taskManager.historyManager.add(taskManager.findTask(taskId));
            }
            taskManager.generatorId = generatorId;
        } catch (IOException e) {
            throw new ManagerSaveException("Can't read form file: " + file.getName(), e);
        }
        return taskManager;
    }

    protected void addAnyTask(Task task) {
        final int id = task.getId();
        switch (task.getTaskType()) {
            case TASK:
                tasks.put(id, task);
                break;
            case SUBTASK:
                subtasks.put(id, (Subtask) task);
                break;
            case EPIC:
                epics.put(id, (Epic) task);
                break;
        }
    }

    protected Task findTask(Integer id) {
        final Task task = tasks.get(id);
        if (task != null) {
            return task;
        }
        final Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            return subtask;
        }
        return epics.get(id);
    }
}


/*
Спасибо за ревью!
Я, честно, не разобрался с методом toString в CSVTaskFormat, он у меня записывал epicId в эпики, что не соответствовало
формату и он вылетал в ошибку. Плюс появилась необходимость создать метод в Task, что мне кажется странно,
там метода быть не должно.
Пока я оставил всё в переопределении базового метода, он вроде как работает и всё верно вызывает.
Пока я этот метод только закомментировал, удалять не стал.
С остальным я разобрался вроде, вот этот вопрос только остался.

P.S.
Хотя я вот поправил метод на SUBTASK и всё отработало
* */