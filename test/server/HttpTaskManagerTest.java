package server;

import static org.junit.jupiter.api.Assertions.*;

import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptions.ManagerLoadException;
import manager.*;
import task.Epic;
import task.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    protected TaskManager manager;
    private static final int PORT = 8080;
    private static final String URL = "http://localhost:";
    private static final String KEY = "TEST";
    private KVServer server;
    private HttpTaskManager httpTaskManager;
    private Gson gson;
    private KVTaskClient kvTaskClient;

    @Override
    public HttpTaskManager createTaskManager() {
        server = new KVServer(PORT);
        server.start();
        return (HttpTaskManager) Managers.getDefault(URL, PORT, KEY);
    }

    @BeforeEach
    public void httpSetup() {
        httpTaskManager = (HttpTaskManager) Managers.getDefault(URL, PORT, KEY);
        gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).
                setPrettyPrinting().create();
        kvTaskClient = new KVTaskClient(URL, PORT);
    }

    @AfterEach
    public void after() {
        server.stop();
    }

    @Test
    void shouldNotLoadFromServer() {
        Task task1 = new Task(
                "Task1",
                "Task1",
                30,
                "2023-07-30T12:00:00");
        Task task2 = new Task(
                "Task2",
                "Task2",
                30,
                "2023-07-28T12:00:00");
        Epic epic1 = new Epic(
                "Epic1",
                "Epic1");
        httpTaskManager.addTask(task1);
        httpTaskManager.addTask(task2);
        httpTaskManager.addEpic(epic1);
        httpTaskManager.getTask(1);
        httpTaskManager.getTask(2);
        httpTaskManager.getEpic(3);
        Exception exception = assertThrows(ManagerLoadException.class, () -> kvTaskClient.load("&!"));
        assertEquals("Can't load from server", exception.getMessage(), "Exception not thrown");
    }
}
