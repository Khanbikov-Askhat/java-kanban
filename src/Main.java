import Task.OutToMenu;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        OutToMenu outToMenu = new OutToMenu();


        while(true) {
            printMenu();
            int i = scanner.nextInt();
            if (i == 1) {
                String taskNameFirst = "Дописать методы";
                String taskDescriptionFirst = "Дописать методы ввода информации";
                outToMenu.addTaskForMenu(taskNameFirst, taskDescriptionFirst);// Добавить первый таск

                String taskNameSecond = "Создать второй тестовый таск";
                String taskDescriptionSecond = "Создать описание второго тестового таска";
                outToMenu.addTaskForMenu(taskNameSecond, taskDescriptionSecond);// Добавить второй таск
            } else if (i == 2) {
                String epicNameFirst = "Доделать код";
                String epicDescriptionFirst = "Дописать автоматический ввод информации в методы";
                outToMenu.addEpicForMenu(epicNameFirst, epicDescriptionFirst);// Добавить эпик

                String epicNameSecond = "Создать тестовый эпик";
                String epicDescriptionSecond = "Создать тестовый эпик с одной подзадачей";
                outToMenu.addEpicForMenu(epicNameSecond, epicDescriptionSecond);// Добавить эпик

            } else if (i == 3) {
                int numberOfChoiceFirst = 1;// 1 - Эпик | 2 - Задача
                int epicIDFirst = 1;
                String subtaskNameFirst = "Сабтаск первого эпика: Изменить методы";
                String subtaskDescriptionFirst = "Закоментить чтение данных";
                outToMenu.addSubtask(numberOfChoiceFirst, epicIDFirst, subtaskNameFirst, subtaskDescriptionFirst);// Добавить подзадачу

                int numberOfChoiceSecond = 1;// 1 - Эпик | 2 - Задача
                String subtaskNameSecond = "Сабтаск первого эпика: Изменить метод addSubtask";
                String subtaskDescriptionSecond = "Добавить параметры в addSubtask";
                outToMenu.addSubtask(numberOfChoiceSecond, epicIDFirst, subtaskNameSecond, subtaskDescriptionSecond);// Добавить подзадачу

                int numberOfChoiceThird = 1;// 1 - Эпик | 2 - Задача
                int epicIDThird = 2;
                String subtaskNameThird  = "Сабтаск второго эпика: Изменить метод addSubtask";
                String subtaskDescriptionThird  = "Добавить параметры в addSubtask";
                outToMenu.addSubtask(numberOfChoiceThird, epicIDThird, subtaskNameThird, subtaskDescriptionThird);// Добавить подзадачу

            } else if (i == 4) {
                outToMenu.printAllTasksEpicsSubtasks(1);// Вывод ВСЕХ задач
                outToMenu.printAllTasksEpicsSubtasks(2);// Вывод ВСЕХ эпиков
                outToMenu.printAllTasksEpicsSubtasks(3);// Вывод ВСЕХ подзадач
            } else if (i == 5) {
                outToMenu.removeTasksEpicsSubtasks(1);// Удаление ВСЕХ задач
                outToMenu.removeTasksEpicsSubtasks(3);// Удаление ВСЕХ подзадач
                outToMenu.removeTasksEpicsSubtasks(2);// Удаление ВСЕХ эпиков
            } else if (i == 6) {
                outToMenu.printTaskById(1, 1);// Вывод задачи по ID
                outToMenu.printEpicById(2,1);// Вывод эпика по ID
                outToMenu.printSubtaskById(3,1,1);// Вывод подзадачи по ID
            } else if (i == 7) {
                outToMenu.updateTaskById(1, 1, 2);// Обновление задачи
                outToMenu.updateEpicById(2,1);// Обновление эпика
                outToMenu.updateSubtaskById(3,2,1,1);// Обновление подзадачи
            } else if (i == 8) {
                outToMenu.removeTask(1,2);// Удаление задачи по ID
                outToMenu.removeEpicById(2,2);// Удаление эпика по ID
                outToMenu.removeSubtaskById(3,1, 1);// Удаление подзадачи по ID
            } else if (i == 9) {
                outToMenu.printAllSubtasksByEpic();// Вывод всех подзадач определенного эпика
            } else if (i == 10) {
                System.out.println("Пока!");
                scanner.close();
            } else {
                System.out.println("Такой команды нет");
            }
        }
    }



    //ПРИНТ МЕНЮ
    static void printMenu() {
        System.out.println("1 - Добавить задачу.");
        System.out.println("2 - Добавить эпик.");
        System.out.println("3 - Добавить подзадачу.");
        System.out.println("4 - Вывод всех задач/эпиков/подзадач.");
        System.out.println("5 - Удаление всех задач/эпиков/подзадач.");
        System.out.println("6 - Получение задачи/эпика/подзадачи по идентификатору.");
        System.out.println("7 - Обновление задачи/эпика/подзадачи.");
        System.out.println("8 - Удаление задачи/эпика/подзадачи по индентификатору");
        System.out.println("9 - Получение всех подзадач определенного эпика.");
        System.out.println("10 - Выйти из приложения.");
    }
}

