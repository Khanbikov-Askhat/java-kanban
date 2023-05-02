package Task;
import java.util.Scanner;

public class OutToMenu {
    TaskManager taskManager = new TaskManager();
    Scanner scanner = new Scanner(System.in);
    public void addTaskForMenu(String taskName, String taskDescription) {
        taskManager.addTask(taskName, taskDescription);
    }



    public void addEpicForMenu(String taskName, String taskDescription) {

        taskManager.addEpic(taskName, taskDescription);
    }


    public void addSubtask(int a, int taskId, String taskName, String taskDescription) {
        if (a == 1) {
            taskManager.addSubtaskIntoEpic(taskId, taskName, taskDescription);
            System.out.println("Подзадача добавлена.");
        } else if (a == 2) {
            taskManager.addSubtaskIntoTask(taskId, taskName, taskDescription);
            System.out.println("Задача перенесена в эпики");
            System.out.println("Подзадача добавлена.");
        } else {
            System.out.println("Такой команды нет.");
        }
    }


    public void printAllTasksEpicsSubtasks(int a) {

        if (a == 1) {
            taskManager.printAllTasks();
        } else if (a == 2) {
            taskManager.printAllEpics();
        } else if (a == 3) {
            taskManager.printAllSubtasks();
        } else {
            System.out.println("Такой команды нет");
        }
    }

    public void removeTasksEpicsSubtasks(int a) {

        if (a == 1) {
            taskManager.removeAllTasks();
        } else if (a == 2) {
            taskManager.removeAllEpics();
        } else if (a == 3) {
            taskManager.removeAllSubtasks();
        } else {
            System.out.println("Такой команды нет");
        }
    }

    //ПРИНТ ЗАДАНИЯ ПО АЙДИ
    public void printTaskById(int a, int taskId ) {

        if(a == 1) {
            taskManager.printTask(taskId);
        }  else {
            System.out.println("Такой команды нет");
        }
    }

    //ПРИНТ ЭПИКА ПО АЙДИ
    public void printEpicById(int a, int epicId ) {

        if(a == 2) {
            taskManager.printEpic(epicId);
        }  else {
            System.out.println("Такой команды нет");
        }
    }

    //ПРИНТ ПОДЗАДАНИЯ ПО АЙДИ
    public void printSubtaskById(int a, int epicIdForSubtask, int subtaskId) {

        if(a == 3) {
            taskManager.printSubtask(epicIdForSubtask, subtaskId);
        }  else {
            System.out.println("Такой команды нет");
        }
    }

    //ИЗМЕНЕНИЕ ЗАДАНИЯ ПО АЙДИ
    public void updateTaskById(int a, int taskId, int statusNumber) {
        if(a == 1) {
            String newTaskName = "Новое название задачи";
            String newTaskDescription = "Новое описание задачи";
            taskManager.updateTask(taskId, statusNumber, newTaskName, newTaskDescription);
        }  else {
            System.out.println("Такой команды нет");
        }
    }

    //ИЗМЕНЕНИЕ ЭПИКА ПО АЙДИ
    public void updateEpicById(int a, int epicId) {
        if(a == 2) {
            String newEpicName = "Новое название эпика";
            String newEpicDescription = "Новое описание эпика";
            taskManager.updateEpic(epicId, newEpicName, newEpicDescription);
        }  else {
            System.out.println("Такой команды нет");
        }
    }

    //ИЗМЕНЕНИЕ ПОДЗАДАНИЯ ПО АЙДИ
    public void updateSubtaskById(int a, int epicId, int subtaskId, int statusNumber) {
        if(a == 3) {
            String newSubtaskName = "Я устал";
            String newSubtaskDescription = "Потом ещё и работать";
            taskManager.updateSubtask(epicId, subtaskId, statusNumber, newSubtaskName, newSubtaskDescription);
            taskManager.checkEpicStatus(epicId);
        }  else {
            System.out.println("Такой команды нет");
        }
    }


    //УДАЛЕНИЕ ЗАДАЧИ ПО АЙДИ
    public void removeTask(int a, int taskId) {
        if(a == 1) {
            taskManager.removeTask(taskId);
        } else {
            System.out.println("Такой команды нет");
        }
    }

    //УДАЛЕНИЕ ЭПИКА ПО АЙДИ
    public void removeEpicById(int a, int epicId) {
        if(a == 2) {
            taskManager.removeEpic(epicId);
        } else {
            System.out.println("Такой команды нет");
        }
    }

    //УДАЛЕНИЕ ПОДЗАДАНИЯ ПО АЙДИ
    public void removeSubtaskById(int a, int epicId, int subtaskId) {
        if(a == 3) {
            taskManager.removeSubtask(epicId, subtaskId);
        } else {
            System.out.println("Такой команды нет");
        }
    }

    public void printAllSubtasksByEpic() {
        System.out.println("Введите ID эпика по которому нужно вывести подзадачи");
        int epicId = scanner.nextInt();
        taskManager.printAllSubtasksForEpic(epicId);
    }


}
