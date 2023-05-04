import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        //Создание
        System.out.println("Создание");
        Task task1 = new Task("Task #1", "Task1 description", "NEW");
        Task task2 = new Task("Task #2", "Task2 description", "IN_PROGRESS");
        int taskId1 = manager.addTask(task1);
        int taskId2 = manager.addTask(task2);
        Epic epic1 = new Epic("Epic #1", "Epic1 description","NEW");
        Epic epic2 = new Epic("Epic #2", "Epic2 description","NEW");
        int epicId1 = manager.addEpic(epic1);
        int epicId2 = manager.addEpic(epic2);
        Subtask subtask1 = new Subtask("Subtask #1-1", "Subtask1 description", "NEW", epicId1);
        Subtask subtask2 = new Subtask("Subtask #2-1", "Subtask1 description", "NEW", epicId1);
        Subtask subtask3 = new Subtask("Subtask #3-2", "Subtask1 description", "DONE", epicId2);
        Integer subtaskId1 = manager.addSubtask(subtask1);
        Integer subtaskId2 = manager.addSubtask(subtask2);
        Integer subtaskId3 = manager.addSubtask(subtask3);
        System.out.println(task1);
        System.out.println(task2);
        System.out.println(epic1);
        System.out.println(epic2);
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println(subtask3);
        System.out.println();

        // Получение всех задач
        System.out.println("Получение всех задач");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
        System.out.println();

        // Получение задач по ID
        System.out.println("Получение задач по ID");
        System.out.println(manager.getTask(taskId2));
        System.out.println(manager.getEpic(epicId2));
        System.out.println(manager.getSubtask(subtaskId1));
        System.out.println();

        // Получение задач определенного эпика
        System.out.println("Получение задач определенного эпика");
        System.out.println(manager.getEpicsSubtasks(epicId1));
        System.out.println();

        // Обновление задачи по ID
        System.out.println("Обновление задачи по ID");
        Task task3 = new Task("Task #3", "Task3 description", "IN_PROGRESS");
        Epic epic3 = new Epic("Epic #3", "Epic3 description","NEW");
        Subtask subtask4 = new Subtask("Subtask #4-1", "Subtask4 description", "NEW", epicId1);
        manager.updateTask(task3, taskId1);
        manager.updateEpic(epic3, epicId1);
        manager.updateSubtask(subtask4, subtaskId1);
        System.out.println(manager.getTask(taskId1));
        System.out.println(manager.getEpic(epicId1));
        System.out.println(manager.getSubtask(subtaskId1));
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
        System.out.println();

        // Удаление задачи по ID
        System.out.println("Удаление задачи по ID");
        manager.deleteTask(taskId1);
        manager.deleteEpic(epicId2);
        manager.deleteSubtask( subtaskId1);
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
        System.out.println();

        //Удаление всех задач
        System.out.println("Удаление всех задач");
        manager.deleteTasks();
        manager.deleteEpics();
        manager.deleteSubtasks();
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
    }

}

