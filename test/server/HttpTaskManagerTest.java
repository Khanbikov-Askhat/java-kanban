package server;

import static org.junit.jupiter.api.Assertions.*;


import adapters.DurationAdapter;
import adapters.InMemoryHistoryManagerAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
import manager.HistoryManager;
import manager.HttpTaskManager;
import manager.Managers;
import manager.TaskManagerTest;
import task.Epic;
import task.Subtask;
import task.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    private static final int PORT = 8080;
    private static final String URL = "http://localhost:";
    private static final String KEY = "TEST";
    private KVServer server;
    private HttpTaskManager taskManager;
    private Gson gson;
    private KVTaskClient kvTaskClient;

    @Override
    public HttpTaskManager createTaskManager() throws IOException {
        server = new KVServer();
        server.start();
        return (HttpTaskManager) Managers.getDefault();
    }

    @BeforeEach
    public void httpSetup() {
        taskManager = (HttpTaskManager) Managers.getDefault();
        gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).
                registerTypeAdapter(HistoryManager.class, new InMemoryHistoryManagerAdapter(taskManager)).
                setPrettyPrinting().create();
        kvTaskClient = new KVTaskClient();
    }

    @AfterEach
    public void after() {
        server.stop();
    }

    @Test
    void testLoadWithEmptyListOfTasks() {
        String loadedFromServer;
        String expected = gson.toJson(taskManager);
        kvTaskClient.put(KEY, expected);
        loadedFromServer = kvTaskClient.load(KEY);
        assertEquals(expected, loadedFromServer, "Objects not equal");
    }

    @Test
    void testSaveAndLoadWitEpicsWithoutSubtasks() {
        Task task1 = new Task(
                "Task1",
                "Task1",
                30,
                "2022-08-30T12:00:00");
        Task task2 = new Task(
                "Task2",
                "Task2",
                30,
                "2022-08-28T12:00:00");
        Epic epic1 = new Epic(
                "Epic1",
                "Epic1");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getEpic(3);
        String loadedFromServer;
        String expected = gson.toJson(taskManager);
        loadedFromServer = kvTaskClient.load(KEY);
        assertEquals(expected, loadedFromServer, "Objects not equal");
    }

    @Test
    void testSaveAndLoadWithEmptyListOfHistory() {
        Task task1 = new Task("Task1",
                "Task1",
                30,
                "2022-08-30T12:00:00");
        Task task2 = new Task(
                "Task2",
                "Task2",
                30,
                "2022-08-28T12:00:00");
        Epic epic1 = new Epic(
                "Epic1",
                "Epic1");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        String loadedFromServer;
        String expected = gson.toJson(taskManager);
        loadedFromServer = kvTaskClient.load(KEY);
        assertEquals(expected, loadedFromServer, "Objects not equal");
    }

    @Test
    void loadWithTasksEpicsSubtasksHistory() {
        Task task1 = new Task(
                "Task1",
                "Task1",
                30,
                "2022-08-30T12:00:00");
        Task task2 = new Task(
                "Task2",
                "Task2",
                30,
                "2022-08-30T15:00:00");
        Epic epic1 = new Epic(
                "Epic1",
                "Epic1");
        Subtask subTask1 = new Subtask(
                3,
                "Subtask1",
                "Subtask1",
                30,
                "2022-08-30T07:00:00");
        Subtask subTask2 = new Subtask(
                3,
                "Subtask2",
                "Subtask2",
                30,
                "2022-08-30T09:00:00");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subTask1);
        taskManager.addSubtask(subTask2);
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getEpic(3);
        taskManager.getSubtask(4);
        taskManager.getSubtask(5);
        String loadedFromServer;
        String expected = gson.toJson(taskManager);
        loadedFromServer = kvTaskClient.load(KEY);
        assertEquals(expected, loadedFromServer, "Objects not equal");
    }

    @Test
    void shouldNotLoadFromServer() {
        Task task1 = new Task(
                "Task1",
                "Task1",
                30,
                "2022-08-30T12:00:00");
        Task task2 = new Task(
                "Task2",
                "Task2",
                30,
                "2022-08-28T12:00:00");
        Epic epic1 = new Epic(
                "Epic1",
                "Epic1");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getEpic(3);
        Exception exception = assertThrows(ManagerLoadException.class, () -> kvTaskClient.load("&!"));
        assertEquals("Can't load from server", exception.getMessage(), "Exception not thrown");
    }

}
