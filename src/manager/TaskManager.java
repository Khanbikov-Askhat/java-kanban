package manager;
import task.Epic;
import task.Subtask;
import task.Task;
import java.util.List;

public interface TaskManager {


    //ДОБАВЛЕНИЕ НОВОГО ТАСКА
    Integer addTask(Task task);


    //ДОБАВЛЕНИЕ НОВОГО ЭПИКА
    Integer addEpic(Epic epic);


    //ДОБАВЛЕНИЕ ПОДЗАДАЧИ
    Integer addSubtask(Subtask subtask);


    //ПОЛУЧЕНИЕ ВСЕХ ЗАДАЧ
    List<Task> getTasks();


    //ПОЛУЧЕНИЕ ВСЕХ ЭПИКОВ
    List<Epic> getEpics();


    //ПОЛУЧЕНИЕ ВСЕХ ПОДЗАДАЧ
    List<Subtask> getSubtasks();


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
    boolean updateTask(Task task, String newStatus);


    //ОБНОВЛЕНИЕ ЭПИКА
    boolean updateEpic(Epic epic);


    //ОБНОВЛЕНИЕ ПОДЗАДАЧИ
    boolean updateSubtask(Subtask subtask, String newStatus);


    //УДАЛЕНИЕ ЗАДАЧИ ПО ИНДЕНТИФИКАТОРУ
    boolean deleteTask(int id);


    //УДАЛЕНИЕ ЭПИКА ПО ИНДЕНТИФИКАТОРУ
    boolean deleteEpic(int id);


    //УДАЛЕНИЕ ПОДЗАДАЧИ ПО ИНДЕНТИФИКАТОРУ
    boolean deleteSubtask(int id);


    //ПОЛУЧЕНИЕ ВСЕХ ПОДЗАДАЧ ОПРЕДЕЛЕННОГО ЭПИКА
    List<Subtask> getEpicsSubtasks(int epicId);

    List<Task> getDefaultHistory();

    List<Task> getPrioritizedTasks();

    HistoryManager getHistoryManager();
}
