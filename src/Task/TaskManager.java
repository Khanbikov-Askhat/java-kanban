package Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TaskManager {
    Scanner scanner = new Scanner(System.in);
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    int epicNumber = 1;
    int taskNumber = 1;


    //ДОБАВЛЕНИЕ НОВОГО ТАСКА
    public void addTask(String taskName, String taskDescription) {
        tasks.put(taskNumber, writeNewTask(taskNumber, taskName, taskDescription));

        System.out.println("Задача добавлена");
        taskNumber += 1;
    }


    //СОЗДАНИЕ НОВОГО ТАСКА
    public Task writeNewTask(int taskId, String taskName, String taskDescription) {

        String taskStatus = "NEW";
        Task currentTask = new Task(taskName, taskDescription, taskId, taskStatus);
        return currentTask;
    }


    //ДОБАВЛЕНИЕ НОВОГО ЭПИКА
    public void addEpic(String taskName, String taskDescription) {
        epics.put(epicNumber, writeNewEpic(epicNumber, taskName, taskDescription));
        System.out.println("Эпик добавлен");
        epicNumber += 1;
    }


    //СОЗДАНИЕ НОВОГО ЭПИКА
    public Epic writeNewEpic(int taskId, String taskName, String taskDescription) {
        ArrayList<Subtask> subtasks = new ArrayList<>();

        String taskStatus = "NEW";
        Epic currentEpic = new Epic(taskName, taskDescription, taskId, taskStatus, subtasks);
        return currentEpic;
    }


    //СОЗДАНИЕ ПОДЗАДАЧИ
    public Subtask addSubtask(int taskId, String taskName, String taskDescription){
        String taskStatus = "NEW";
        Subtask currentTask = new Subtask(taskName, taskDescription, taskId, taskStatus);
        return currentTask;
    }


    //ДОБАВЛЕНИЕ ПОДЗАДАЧИ В ЭПИК
    public void addSubtaskIntoEpic(int epicId, String taskName, String taskDescription) {
        int sizeEpic = 0;
        if (epics.isEmpty()) {
            System.out.println("Необходимо сначала создать эпик");
            return;
        } else {
            for (Integer key: epics.keySet()) {
                if (epics.get(key).taskId != epicId) {
                    continue;
                }
                for (int i = 0; i <= epics.get(key).subtasks.size(); i++) {
                    if (epics.get(key).subtasks.isEmpty()){
                        epics.get(key).subtasks.add(addSubtask(i + 1, taskName, taskDescription));
                        break;
                    } else  {
                        for (int j = 0; j <= epics.get(key).subtasks.size(); j++) {
                            sizeEpic += 1;
                        }
                        epics.get(key).subtasks.add(addSubtask(sizeEpic, taskName, taskDescription));
                        break;
                    }
                }
            }
        }
    }


    //ПРИВЕДЕНИЕ ТАСКА В ЭПИК И ВНЕСЕНИЕ ПОДЗАДАЧИ
    public void addSubtaskIntoTask(int taskId, String SubtaskName, String SubtaskDescription) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        if (tasks.isEmpty()) {
            System.out.println("Нет созданных задач");
            return;
        } else {
            for (Integer key : tasks.keySet()) {
                if (tasks.get(key).taskId != taskId) {
                    continue;
                }
                String taskName = tasks.get(key).taskName;
                String taskDescription = tasks.get(key).taskDescription;
                int Id = epicNumber;
                String taskStatus = tasks.get(key).taskStatus;
                subtasks.add(addSubtask(1, SubtaskName, SubtaskDescription));
                Epic currentEpic = new Epic(taskName, taskDescription, Id, taskStatus, subtasks);
                epics.put(epicNumber, currentEpic);

                epicNumber += 1;
            }
            tasks.remove(taskId);
        }
    }


    //ПРИНТ ВСЕХ ЗАДАЧ
    public void printAllTasks() {
        if(tasks.isEmpty()) {
            System.out.println("Нет созданных задач");
            return;
        }
        for (Integer key : tasks.keySet()) {
            System.out.println("Название задачи: " + tasks.get(key).taskName);
            System.out.println("Описание задачи: " + tasks.get(key).taskDescription);
            System.out.println("ID задачи: " + tasks.get(key).taskId);
            System.out.println("Статус задачи: " + tasks.get(key).taskStatus);
            System.out.println();
        }
    }


    //ПРИНТ ВСЕХ ЭПИКОВ
    public void printAllEpics() {
        if(epics.isEmpty()) {
            System.out.println("Нет созданных эпиков");
            return;
        }
        for (Integer key : epics.keySet()) {
            System.out.println("Название эпика: " + epics.get(key).taskName);
            System.out.println("Описание эпика: " + epics.get(key).taskDescription);
            System.out.println("ID эпика: " + epics.get(key).taskId);
            System.out.println("Статус эпика: " + epics.get(key).taskStatus);
            System.out.println();
        }
    }


    //ПРИНТ ВСЕХ ПОДЗАДАЧ
    public void printAllSubtasks() {
        int emptyTasks = 0;
        for (Integer key : epics.keySet()) {
            for(int i = 0; i < epics.get(key).subtasks.size(); i ++) {
                if(epics.get(key).subtasks.isEmpty()) {
                    emptyTasks += 1;

                }
            }
        }
        if(emptyTasks == epics.size()) {
            System.out.println("Нет созданных подзадач");
        } else {
            for (Integer key : epics.keySet()) {
                for(int i = 0; i < epics.get(key).subtasks.size(); i ++) {
                    if(!epics.get(key).subtasks.isEmpty()) {
                        System.out.println("Название подзадачи: " + epics.get(key).subtasks.get(i).taskName);
                        System.out.println("Описание подзадачи: " + epics.get(key).subtasks.get(i).taskDescription);
                        System.out.println("ID подзадачи: " + epics.get(key).subtasks.get(i).taskId);
                        System.out.println("Статус подзадачи: " + epics.get(key).subtasks.get(i).taskStatus);
                        System.out.println();

                    }
                }
            }
        }
    }


    //УДАЛЕНИЕ ВСЕХ ЗАДАЧ
    public void removeAllTasks() {
        ArrayList<Integer> keyTaskForRemove = new ArrayList<>();
        if(tasks.isEmpty()) {
            System.out.println("Нет созданных задач");
            return;
        }
        for (Integer key : tasks.keySet()) {
            keyTaskForRemove.add(key);
            //tasks.remove(key);
            //System.out.println("Все задачи удалены");
        }
        for(int i = 1; i <= keyTaskForRemove.size(); i ++) {
            tasks.remove(i);
        }
        System.out.println("Все задачи удалены");
    }


    //УДАЛЕНИЕ ВСЕХ ЭПИКОВ
    public void removeAllEpics() {
        ArrayList<Integer> keyEpicForRemove = new ArrayList<>();
        if(epics.isEmpty()) {
            System.out.println("Нет созданных эпиков");
            return;
        }
        for (Integer key : epics.keySet()) {
            keyEpicForRemove.add(key);
        }
        for(int i = 1; i <= keyEpicForRemove.size(); i ++) {
            epics.remove(i);
        }
        System.out.println("Все эпики удалены");
    }


    //УДАЛЕНИЕ ВСЕХ ПОДЗАДАЧ
    public void removeAllSubtasks() {
        int emptyTasks = 0;
        for (Integer key : epics.keySet()) {
            for(int i = 0; i < epics.get(key).subtasks.size(); i ++) {
                if(epics.get(key).subtasks.isEmpty()) {
                    emptyTasks += 1;

                }
            }
        }
        if(emptyTasks == epics.size()) {
            System.out.println("Нет созданных подзадач");
        } else {
            for (Integer key : epics.keySet()) {
                ArrayList<Integer> subtaskToRemove = new ArrayList<>();
                for (int i = 0; i < epics.get(key).subtasks.size() ; i=i) {
                    //subtaskToRemove.add(i);
                    epics.get(key).subtasks.remove(i);
                }
            }
            System.out.println("Все подзадачи удалены");
        }
    }


    //ПОЛУЧЕНИЕ ЗАДАЧИ ПО ИДЕНТИФИКАТОРУ
    public void printTask(int id) {
        if(tasks.isEmpty()) {
            System.out.println("Нет созданных задач");
            return;
        }
        for (Integer key : tasks.keySet()) {
            if (key != id) {
                continue;
            }
            System.out.println("Название задачи: " + tasks.get(key).taskName);
            System.out.println("Описание задачи: " + tasks.get(key).taskDescription);
            System.out.println("ID задачи: " + tasks.get(key).taskId);
            System.out.println("Статус задачи: " + tasks.get(key).taskStatus);
            System.out.println();
        }
    }


    //ПОЛУЧЕНИЕ ЭПИКА ПО ИДЕНТИФИКАТОРУ
    public void printEpic(int id) {
        if(epics.isEmpty()) {
            System.out.println("Нет созданных эпиков");
            return;
        }
        for (Integer key : epics.keySet()) {
            if (key != id) {
                continue;
            }
            System.out.println("Название задачи: " + epics.get(key).taskName);
            System.out.println("Описание задачи: " + epics.get(key).taskDescription);
            System.out.println("ID задачи: " + epics.get(key).taskId);
            System.out.println("Статус задачи: " + epics.get(key).taskStatus);
            System.out.println();
        }
    }


    //ПОЛУЧЕНИЕ ПОДЗАДАЧИ ПО ИДЕНТИФИКАТОРУ
    public void printSubtask(int epicId, int subtaskId) {
        int emptyTasks = 0;
        for (Integer key : epics.keySet()) {
            for(int i = 0; i < epics.get(key).subtasks.size(); i ++) {
                if(epics.get(key).subtasks.isEmpty()) {
                    emptyTasks += 1;

                }
            }
        }
        if(emptyTasks == epics.size()) {
            System.out.println("Нет созданных подзадач");
        } else {
            for (Integer key : epics.keySet()) {
                if (key != epicId) {
                    continue;
                }
                for(int i = 0; i < epics.get(key).subtasks.size(); i ++) {
                    if (epics.get(key).subtasks.get(i).taskId == subtaskId) {
                        System.out.println("Название подзадачи: " + epics.get(key).subtasks.get(i).taskName);
                        System.out.println("Описание подзадачи: " + epics.get(key).subtasks.get(i).taskDescription);
                        System.out.println("ID подзадачи: " + epics.get(key).subtasks.get(i).taskId);
                        System.out.println("Статус подзадачи: " + epics.get(key).subtasks.get(i).taskStatus);
                        System.out.println();
                    }
                }
            }
        }
    }


    //ОБНОВЛЕНИЕ ЗАДАЧИ
    public void updateTask(int taskId, int statusNumber, String newTaskName, String newTaskDescription) {
        if (tasks.isEmpty()) {
            System.out.println("Нет созданных задач");
            return;
        }
        for (Integer key: tasks.keySet()) {
            if (key != taskId) {
                continue;
            }
            System.out.println("Введите новое название задачи");
            tasks.get(key).taskName = newTaskName;
            System.out.println("Введите новое описание задачи");
            tasks.get(key).taskDescription = newTaskDescription;
            if (statusNumber == 1) {
                tasks.get(key).taskStatus = "IN_PROGRESS";
            } else if (statusNumber == 2) {
                tasks.get(key).taskStatus = "DONE";
            } else if (statusNumber == 3) {
                tasks.get(key).taskStatus = "NEW";
            } else {
                System.out.println("Такой команды нет");
            }
            System.out.println("Задача обновлена");
        }
    }


    //ОБНОВЛЕНИЕ ЭПИКА
    public void updateEpic(int taskId, String newEpicName, String newEpicDescription) {
        if (epics.isEmpty()) {
            System.out.println("Нет созданных эпиков");
            return;
        }
        for (Integer key: epics.keySet()) {
            if (key != taskId) {
                continue;
            }
            epics.get(key).taskName = newEpicName;
            epics.get(key).taskDescription = newEpicDescription;
            System.out.println("Задача обновлена");
        }
    }


    //ОБНОВЛЕНИЕ ПОДЗАДАЧИ
    public void updateSubtask(int epicId, int subtaskId, int statusNumber, String newSubtaskName, String newSubtaskDescription) {
        int emptyTasks = 0;
        for (Integer key : epics.keySet()) {
            for(int i = 0; i < epics.get(key).subtasks.size(); i ++) {
                if(epics.get(key).subtasks.isEmpty()) {
                    emptyTasks += 1;

                }
            }
        }
        if(emptyTasks == epics.size()) {
            System.out.println("Нет созданных подзадач");
        } else {
            for (Integer key : epics.keySet()) {
                if (key != epicId) {
                    continue;
                }
                for(int i = 0; i < epics.get(key).subtasks.size(); i ++) {
                    if (epics.get(key).subtasks.get(i).taskId == subtaskId) {

                        epics.get(key).subtasks.get(i).taskName = newSubtaskName;
                        epics.get(key).subtasks.get(i).taskDescription = newSubtaskDescription;

                        if (statusNumber == 1) {
                            epics.get(key).subtasks.get(i).taskStatus = "IN_PROGRESS";
                        } else if (statusNumber == 2) {
                            epics.get(key).subtasks.get(i).taskStatus = "DONE";
                        } else if (statusNumber == 3) {
                            epics.get(key).subtasks.get(i).taskStatus = "NEW";
                        } else {
                            System.out.println("Такой команды нет");
                        }
                        System.out.println("Задача обновлена");
                        System.out.println();
                    } else {
                        System.out.println("Такой подзадачи нет");
                    }
                }
            }
        }
    }


    //УДАЛЕНИЕ ЗАДАЧИ ПО ИНДЕНТИФИКАТОРУ
    public void removeTask(int taskId) {
        if(tasks.isEmpty()) {
            System.out.println("Нет созданных задач");
            return;
        }
        for (Integer key : tasks.keySet()) {
            if (key != taskId) {
                continue;
            }
            tasks.remove(key);
            System.out.println("Задача удалена");
        }
    }


    //УДАЛЕНИЕ ЭПИКА ПО ИНДЕНТИФИКАТОРУ
    public void removeEpic(int taskId) {
        if(epics.isEmpty()) {
            System.out.println("Нет созданных эпиков");
            return;
        }
        for (Integer key : epics.keySet()) {
            if (key != taskId) {
                continue;
            }
            epics.remove(key);
            System.out.println("Эпик удален");
        }
    }


    //УДАЛЕНИЕ ПОДЗАДАЧИ ПО ИНДЕНТИФИКАТОРУ
    public void removeSubtask(int epicId, int subtaskId) {
        int emptyTasks = 0;
        for (Integer key : epics.keySet()) {
            for(int i = 0; i < epics.get(key).subtasks.size(); i ++) {
                if(epics.get(key).subtasks.isEmpty()) {
                    emptyTasks += 1;

                }
            }
        }
        if(emptyTasks == epics.size()) {
            System.out.println("Нет созданных подзадач");
        } else {
            for (Integer key : epics.keySet()) {
                if (key != epicId) {
                    continue;
                }
                for(int i = 0; i < epics.get(key).subtasks.size(); i ++) {
                    if (epics.get(key).subtasks.get(i).taskId == subtaskId) {
                        epics.get(key).subtasks.remove(i);
                        System.out.println("Подзадача удалена");
                        System.out.println();
                    } else {
                        System.out.println("Такой подзадачи нет");
                    }
                }
            }
        }
    }


    //ПОЛУЧЕНИЕ ВСЕХ ПОДЗАДАЧ ОПРЕДЕЛЕННОГО ЭПИКА
    public void printAllSubtasksForEpic(int epicId) {
        int emptyTasks = 0;
        for (Integer key : epics.keySet()) {
            for(int i = 0; i < epics.get(key).subtasks.size(); i ++) {
                if(epics.get(key).subtasks.isEmpty()) {
                    emptyTasks += 1;

                }
            }
        }
        if(emptyTasks == epics.size()) {
            System.out.println("Нет созданных подзадач");
        } else {
            for (Integer key : epics.keySet()) {
                if (key != epicId) {
                    continue;
                }
                for(int i = 0; i < epics.get(key).subtasks.size(); i ++) {
                    System.out.println("Название подзадачи: " + epics.get(key).subtasks.get(i).taskName);
                    System.out.println("Описание подзадачи: " + epics.get(key).subtasks.get(i).taskDescription);
                    System.out.println("ID подзадачи: " + epics.get(key).subtasks.get(i).taskId);
                    System.out.println("Статус подзадачи: " + epics.get(key).subtasks.get(i).taskStatus);
                    System.out.println();
                }
            }
        }
    }


    //ПРОВЕРКА СТАТУСОВ У ЭПИКОВ
    public void checkEpicStatus(int epicId) {
        int emptyTasks = 0;
        for (Integer key : epics.keySet()) {
            for(int i = 0; i < epics.get(key).subtasks.size(); i ++) {
                if(epics.get(key).subtasks.isEmpty()) {
                    emptyTasks += 1;

                }
            }
        }
        if(emptyTasks == epics.size()) {
            return;
        }
        for(Integer key: epics.keySet()) {
            if (key != epicId) {
                continue;
            }
            int status = 0;
            for (int i = 0; i < epics.get(key).subtasks.size(); i ++) {
                if (epics.get(key).subtasks.get(i).taskStatus.equals("DONE")) {
                    status += 1;
                }
            }
            if (status == epics.get(key).subtasks.size()) {
                epics.get(key).taskStatus = "DONE";
            } else {
                epics.get(key).taskStatus = "IN_PROGRESS";
            }
        }
    }
}
