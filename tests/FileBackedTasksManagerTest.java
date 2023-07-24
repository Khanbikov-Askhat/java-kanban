import exceptions.ManagerSaveException;
import manager.FileBackedTasksManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<TaskManager> {

    private TaskManager fileBackedTaskManager;
    private File file;

    @Override
    TaskManager createTaskManager() {
        return Managers.getDefault();
    }



    @BeforeEach
    public void filedBackedLoad() {
        fileBackedTaskManager = createTaskManager();
        file = new File("resources/task.csv");
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file))) {
            fileWriter.write(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //ДОЛЖЕН ЗАГРУЗИТЬ С ПУСТНЫМ СПИСКОМ
    @Test
    void shouldLoadWithEmptyList() {
        TaskManager loadedFromFile;
        try {
            loadedFromFile = FileBackedTasksManager.loadFromFile(file);
        } catch (ManagerSaveException e) {
            loadedFromFile = null;
        }
        assertEquals(fileBackedTaskManager.getSubtasks().size(), loadedFromFile.getSubtasks().size(), "Objects not equal");
    }
    //ДОЛЖЕН СОХРАНИТЬ С ЭПИКАМИ БЕЗ САБТАСКОВ
    @Test
    void shouldSaveAndLoadWitEpicsWithoutSubtasks() {
        Task task1 = new Task(
                "Task1",
                "Task1",
                "PT30M",
                "2023-07-24T12:00:00");
        Task task2 = new Task(
                "Task2",
                "Task2",
                "PT30M",
                "2023-07-22T12:00:00");
        Epic epic1 = new Epic(
                "Epic1",
                "Epic1");

        fileBackedTaskManager.addTask(task1);
        fileBackedTaskManager.addTask(task2);
        fileBackedTaskManager.addEpic(epic1);
        fileBackedTaskManager.getTask(1);
        fileBackedTaskManager.getTask(2);
        fileBackedTaskManager.getTask(3);

        TaskManager loadedFromFile = FileBackedTasksManager.loadFromFile(file);
        assertEquals(fileBackedTaskManager.getTasks().size(), loadedFromFile.getTasks().size(), "Size not equal");
        assertEquals(fileBackedTaskManager.getEpics().size(), loadedFromFile.getEpics().size(), "Size not equal");
    }
    //ДОЛЖЕН СОХРАНИТЬ И ЗАГРУЗИТЬ С ПУСТОЙ ИСТОРИЕЙ
    @Test
    void shouldSaveAndLoadWithEmptyListOfHistory() {
        Task task1 = new Task(
                "Task1",
                "Task1",
                "PT30M",
                "2023-07-24T12:00:00");
        Task task2 = new Task(
                "Task2",
                "Task2",
                "PT30M",
                "2023-07-22T12:00:00");
        Epic epic1 = new Epic(
                "Epic1",
                "Epic1");

        fileBackedTaskManager.addTask(task1);
        fileBackedTaskManager.addTask(task2);
        fileBackedTaskManager.addTask(epic1);

        TaskManager loadedFromFile = FileBackedTasksManager.loadFromFile(file);
        assertEquals(fileBackedTaskManager.getSubtasks().size(), loadedFromFile.getSubtasks().size(), "Size not equal");
    }
    //ДОЛЖЕН ЗАГРУЗИТЬ С ИСТОРИЕЙ
    @Test
    void shouldLoadWithTasksEpicsSubtasksHistory() {
        Task task1 = new Task(
                "Task1",
                "Task1",
                "PT30M",
                "2023-07-24T12:00:00");
        Task task2 = new Task(
                "Task2",
                "Task2",
                "PT30M",
                "2023-07-24T15:00:00");
        Epic epic1 = new Epic(
                "Epic1",
                "Epic1");
        Subtask subTask1 = new Subtask(
                3,
                "Subtask1",
                "Subtask1",
                "PT30M",
                "2023-07-24T07:00:00");
        Subtask subTask2 = new Subtask(
                3,
                "Subtask2",
                "Subtask2",
                "PT30M",
                "2023-07-24T09:00:00");
        fileBackedTaskManager.addTask(task1);
        fileBackedTaskManager.addTask(task2);
        fileBackedTaskManager.addEpic(epic1);
        fileBackedTaskManager.addSubtask(subTask1);
        fileBackedTaskManager.addSubtask(subTask2);
        fileBackedTaskManager.getTask(1);
        fileBackedTaskManager.getTask(2);
        fileBackedTaskManager.getEpic(3);
        fileBackedTaskManager.getSubtask(4);
        fileBackedTaskManager.getSubtask(5);
        TaskManager loadedFromFile = FileBackedTasksManager.loadFromFile(file);
        assertEquals(fileBackedTaskManager.getTasks().size(), loadedFromFile.getTasks().size(), "Objects not equal");
        assertEquals(fileBackedTaskManager.getEpics().size(), loadedFromFile.getEpics().size(), "Objects not equal");
        assertEquals(fileBackedTaskManager.getSubtasks().size(), loadedFromFile.getSubtasks().size(), "Objects not equal");
    }
    //НЕ ДОЛЖЕН ЗАГРУЖАТЬ ТАСКИ
    @Test
    void shouldNotLoadFromFile() {
        File failFile = new File("fail.csv");
        Task task1 = new Task(
                "Task1",
                "Task1",
                "PT30M",
                "2023-07-24T12:00:00");
        Task task2 = new Task(
                "Task2",
                "Task2",
                "PT30M",
                "2023-07-22T12:00:00");
        Epic epic1 = new Epic(
                "Epic1",
                "Epic1");
        fileBackedTaskManager.addTask(task1);
        fileBackedTaskManager.addTask(task2);
        fileBackedTaskManager.addTask(epic1);
        fileBackedTaskManager.getTask(1);
        fileBackedTaskManager.getTask(2);
        fileBackedTaskManager.getEpic(3);
        Exception exception = assertThrows(ManagerSaveException.class, () -> FileBackedTasksManager.loadFromFile(failFile));
        assertEquals("Can't read from file: " + failFile.getName(), exception.getMessage(), "Exception not thrown");
    }
    //НЕ ДОЛЖЕН СОХРАНЯТЬ В ФАЙЛ
    @Test
    void shouldNotSaveToFile() {
        Task task1 = new Task(
                "Task1",
                "Task1",
                "PT30M",
                "2023-07-24T12:00:00");
        FileBackedTasksManager newBackedTaskManager = new FileBackedTasksManager(new File(""));
        Exception exception = assertThrows(ManagerSaveException.class, () -> newBackedTaskManager.addTask(task1));
        assertEquals("Can't save to file: ", exception.getMessage(), "Exception not thrown");
    }


}
