package manager;
import task.Epic;
import task.Subtask;
import task.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public interface TaskManager {



    //ДОБАВЛЕНИЕ НОВОГО ТАСКА
    int addTask(Task task);


    //ДОБАВЛЕНИЕ НОВОГО ЭПИКА
    int addEpic(Epic epic);


    //ДОБАВЛЕНИЕ ПОДЗАДАЧИ
    Integer addSubtask(Subtask subtask);


    //ПОЛУЧЕНИЕ ВСЕХ ЗАДАЧ
    ArrayList<Task> getTasks();


    //ПОЛУЧЕНИЕ ВСЕХ ЭПИКОВ
    ArrayList<Epic> getEpics();


    //ПОЛУЧЕНИЕ ВСЕХ ПОДЗАДАЧ
    ArrayList<Subtask> getSubtasks();


    //УДАЛЕНИЕ ВСЕХ ЗАДАЧ
    void deleteTasks();


    //УДАЛЕНИЕ ВСЕХ ЭПИКОВ
    void deleteEpics();


    //УДАЛЕНИЕ ВСЕХ ПОДЗАДАЧ
    void deleteSubtasks();


    //ПОЛУЧЕНИЕ ЗАДАЧИ ПО ИДЕНТИФИКАТОРУ
    Task getTask(int id);


    //ПОЛУЧЕНИЕ ЭПИКА ПО ИДЕНТИФИКАТОРУ
    Epic getEpic(int id);


    //ПОЛУЧЕНИЕ ПОДЗАДАЧИ ПО ИДЕНТИФИКАТОРУ
    Subtask getSubtask(int id);


    //ОБНОВЛЕНИЕ ЗАДАЧИ
    void updateTask(Task task, String newStatus);


    //ОБНОВЛЕНИЕ ЭПИКА
    void updateEpic(Epic epic);


    //ОБНОВЛЕНИЕ ПОДЗАДАЧИ
    void updateSubtask(Subtask subtask, String newStatus);


    //УДАЛЕНИЕ ЗАДАЧИ ПО ИНДЕНТИФИКАТОРУ
    void deleteTask(int id);


    //УДАЛЕНИЕ ЭПИКА ПО ИНДЕНТИФИКАТОРУ
    void deleteEpic(int id);


    //УДАЛЕНИЕ ПОДЗАДАЧИ ПО ИНДЕНТИФИКАТОРУ
    void deleteSubtask(int id);


    //ПОЛУЧЕНИЕ ВСЕХ ПОДЗАДАЧ ОПРЕДЕЛЕННОГО ЭПИКА
     ArrayList<Subtask> getEpicsSubtasks(int epicId);



}
