package manager;

import exceptions.ManagerSaveException;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskType;

import java.io.*;

public class FileBackedTasksManager extends InMemoryTaskManager{

    protected static String fileName = "savedTasks.csv";
    protected static File file = new File(fileName);

    public static void main(String[] args) {

        FileBackedTasksManager managerFirst = new FileBackedTasksManager();

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


        // Получение истории задач
        System.out.println("Получение истории задач в первый раз");
        System.out.println(managerFirst.getDefaultHistory());
        System.out.println();

        FileBackedTasksManager managerSecond = loadFromFile(file);

        // Получение истории задач из файла
        System.out.println("Получение истории задач в первый раз");
        System.out.println(managerSecond.getDefaultHistory());
        System.out.println();

    }

    //СОХРАНЕНИЕ ФАЙЛА
    protected void save() {
        try (Writer fileWriter = new FileWriter(fileName)) {
            fileWriter.write("id,type,name,status,description,epic\n");
            for (Task task : getTasks()) {
                fileWriter.write(task.toString() + "\n");
            }
            for (Epic epic : getEpics()) {
                fileWriter.write(epic.toString() + "\n");
            }
            for (Subtask subtask : getSubtasks()) {
                fileWriter.write(subtask.toString() + "\n");
            }
            fileWriter.write("\n");
            for (Task task : getDefaultHistory()) {
                fileWriter.write(task.getId() + ",");
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Не получается сохранить данные в файл");
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
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getName()))) {
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                String taskLine = bufferedReader.readLine();
                if (!taskLine.isEmpty()) {
                    tasksFromString(taskLine, fileBackedTasksManager);
                } else {
                    String historyLine = bufferedReader.readLine();
                    if (historyLine != null) {
                        historyFromString(historyLine, fileBackedTasksManager);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не получается загрузить данные из файла");
        }
        return fileBackedTasksManager;
    }

    private static void tasksFromString(String taskLine, FileBackedTasksManager fileBackedTasksManager) {
        String[] taskFields = taskLine.split(",");
        try {
            TaskType taskType = TaskType.valueOf(taskFields[1]);
            switch (taskType) {
                case TASK -> {
                    Task task = new Task(taskLine);
                    fileBackedTasksManager.addTasksFromFile(task);
                }
                case EPIC -> {
                    Epic epic = new Epic(taskLine);
                    fileBackedTasksManager.addTasksFromFile(epic);
                }
                case SUBTASK -> {
                    Subtask Subtask = new Subtask(taskLine);
                    fileBackedTasksManager.addTasksFromFile(Subtask);
                }
            }
        } catch (IllegalArgumentException e) {
            throw new ManagerSaveException("Неверный тип задачи");
        }
    }

    private static void historyFromString(String historyLine, FileBackedTasksManager fileBackedTasksManager) {
        String[] historyFields = historyLine.split(",");
        for (String field : historyFields) {
            int id = Integer.parseInt(field);
            fileBackedTasksManager.getTask(id);
            fileBackedTasksManager.getEpic(id);
            fileBackedTasksManager.getSubtask(id);
        }
    }

    public void addTasksFromFile(Task taskFromFile) {
        if (taskFromFile.getTaskType() == TaskType.TASK) {
            tasks.put(taskFromFile.getId(), taskFromFile);
            generatorId = taskFromFile.getId();
        } else if (taskFromFile.getTaskType() == TaskType.EPIC) {
            epics.put(taskFromFile.getId(), (Epic) taskFromFile);
            generatorId = taskFromFile.getId();
        } else {
            subtasks.put(taskFromFile.getId(), (Subtask) taskFromFile);
            generatorId = taskFromFile.getId();
            Subtask subtask = (Subtask) taskFromFile;
            epics.get(subtask.getEpicId()).getSubtaskIds().add(subtask.getId());
        }
    }
}


/*
Сделал как смог, очень долго сидел на том, что не правильно передал метод и не замечал...
* */