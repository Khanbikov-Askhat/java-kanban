import manager.InMemoryTaskManager;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        //Создание
        System.out.println("Создание");
        Task task1 = new Task("Task #1", "Task1 description");
        Task task2 = new Task("Task #2", "Task2 description");
        Task task3 = new Task("Task #3", "Task2 description");
        int taskId1 = manager.addTask(task1);
        int taskId2 = manager.addTask(task2);
        int taskId3 = manager.addTask(task3);
        Epic epic1 = new Epic("Epic #1", "Epic1 description");
        Epic epic2 = new Epic("Epic #2", "Epic2 description");
        Epic epic3 = new Epic("Epic #3", "Epic2 description");
        int epicId1 = manager.addEpic(epic1);
        int epicId2 = manager.addEpic(epic2);
        int epicId3 = manager.addEpic(epic3);
        Subtask subtask1 = new Subtask("Subtask #1-1", "Subtask1 description", epicId1);
        Subtask subtask2 = new Subtask("Subtask #2-1", "Subtask1 description", epicId1);
        Subtask subtask3 = new Subtask("Subtask #3-2", "Subtask1 description", epicId2);
        Subtask subtask4 = new Subtask("Subtask #4-2", "Subtask1 description", epicId2);
        Subtask subtask5 = new Subtask("Subtask #5-3", "Subtask1 description", epicId3);
        Subtask subtask6 = new Subtask("Subtask #6-3", "Subtask1 description", epicId3);
        Integer subtaskId1 = manager.addSubtask(subtask1);
        Integer subtaskId2 = manager.addSubtask(subtask2);
        Integer subtaskId3 = manager.addSubtask(subtask3);
        Integer subtaskId4 = manager.addSubtask(subtask4);
        Integer subtaskId5 = manager.addSubtask(subtask5);
        Integer subtaskId6 = manager.addSubtask(subtask6);

        // Обновление задачи по ID
        System.out.println("Обновление задачи по ID");
        Task task4 = new Task("Task #4", "Task4 description", taskId1);
        Epic epic4 = new Epic("Epic #4", "Epic3 description", epicId1);
        Subtask subtask7 = new Subtask("Subtask #4-1", "Subtask4 description", subtaskId1, epicId1);
        manager.updateTask(task4, "DONE");
        manager.updateEpic(epic4);
        manager.updateSubtask(subtask7, "IN_PROGRESS");
        System.out.println(manager.getTask(taskId1));
        System.out.println(manager.getEpic(epicId1));
        System.out.println(manager.getSubtask(subtaskId1));
        System.out.println();

        // Получение задач по ID
        System.out.println("Получение задач по ID");
        System.out.println(manager.getTask(taskId1));
        System.out.println(manager.getTask(taskId2));
        System.out.println(manager.getTask(taskId3));
        System.out.println(manager.getEpic(epicId1));
        System.out.println(manager.getEpic(epicId2));
        System.out.println(manager.getEpic(epicId3));
        System.out.println(manager.getSubtask(subtaskId1));
        System.out.println(manager.getSubtask(subtaskId2));
        System.out.println(manager.getSubtask(subtaskId3));
        System.out.println(manager.getSubtask(subtaskId4));
        System.out.println();

        // Получение истории задач
        System.out.println("Получение истории задач");
        System.out.println(manager.getDefaultHistory());
        System.out.println();

        // Проверка на замещение
        System.out.println("Проверка на замещение");
        System.out.println(manager.getSubtask(subtaskId5));
        System.out.println(manager.getSubtask(subtaskId6));
        System.out.println(manager.getDefaultHistory());
        System.out.println();


    }

}

/* Понятия не имею что я вообще сделал и почему это отрабатывает
* Я абсолютно не понял задания, я сделал первую часть с историей, хорошо, оно понятно,
* что можно создавать список внутри InMemoryTaskManager и отткуда его выводить, а вот дальше с этими историями,
* с кучей интерфейсов и классов, не знаю, сложно. Это конечно работает, но я не знаю почемуXD

* */