import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        //Создание
        System.out.println("Создание");
        Task task1 = new Task("Task #1", "Task1 description");
        Task task2 = new Task("Task #2", "Task2 description");
        int taskId1 = manager.addTask(task1);
        int taskId2 = manager.addTask(task2);
        Epic epic1 = new Epic("Epic #1", "Epic1 description");
        Epic epic2 = new Epic("Epic #2", "Epic2 description");
        int epicId1 = manager.addEpic(epic1);
        int epicId2 = manager.addEpic(epic2);
        Subtask subtask1 = new Subtask("Subtask #1-1", "Subtask1 description", epicId1);
        Subtask subtask2 = new Subtask("Subtask #2-1", "Subtask2 description", epicId1);
        Subtask subtask3 = new Subtask("Subtask #3-1", "Subtask3 description", epicId1);

        Integer subtaskId1 = manager.addSubtask(subtask1);
        Integer subtaskId2 = manager.addSubtask(subtask2);
        Integer subtaskId3 = manager.addSubtask(subtask3);



        // Получение задач по ID
        System.out.println("Получение задач по ID в первый раз");
        System.out.println(manager.getTask(taskId1));
        System.out.println(manager.getTask(taskId2));
        System.out.println(manager.getEpic(epicId1));
        System.out.println(manager.getEpic(epicId2));
        System.out.println(manager.getSubtask(subtaskId1));
        System.out.println(manager.getSubtask(subtaskId2));
        System.out.println(manager.getSubtask(subtaskId3));
        System.out.println();
        // Получение истории задач
        System.out.println("Получение истории задач в первый раз");
        System.out.println(manager.getDefaultHistory());
        System.out.println();


        System.out.println("Получение задач по ID во второй раз");
        System.out.println(manager.getTask(taskId1));
        System.out.println(manager.getSubtask(subtaskId1));
        System.out.println(manager.getEpic(epicId1));
        System.out.println(manager.getSubtask(subtaskId2));
        System.out.println(manager.getEpic(epicId2));
        System.out.println(manager.getSubtask(subtaskId3));
        System.out.println(manager.getTask(taskId2));
        System.out.println();

        // Получение истории задач
        System.out.println("Получение истории задач во второй раз");
        System.out.println(manager.getDefaultHistory());
        System.out.println();

        // Удаление задачи
        manager.deleteTask(taskId1);
        System.out.println();
        // Получение истории задач
        System.out.println("Получение истории задач после удаления Таски");
        System.out.println(manager.getDefaultHistory());
        System.out.println();

        // Удаление подзадачи
        manager.deleteSubtask(subtaskId2);
        System.out.println();
        // Получение истории задач
        System.out.println("Получение истории задач после удаления Сабтаски");
        System.out.println(manager.getDefaultHistory());
        System.out.println();

        // Удаление эпика
        manager.deleteEpic(epicId1);
        System.out.println();
        // Получение истории задач
        System.out.println("Получение истории задач после удаления Эпика");
        System.out.println(manager.getDefaultHistory());
        System.out.println();

    }

}

/* Спасибо за ревью!
   Поправил в соответствии с ревью, вроде как подразобрался в теме, но это всё ещё под вопросом.*/
