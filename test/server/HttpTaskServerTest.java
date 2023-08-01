package server;

import static org.junit.jupiter.api.Assertions.*;
import static server.TaskResponseState.*;
import static server.TaskResponseState.NOT_FOUND;
import static task.TaskCollectionType.*;
import static task.TaskType.*;

import com.google.gson.*;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.HttpTaskManager;
import manager.Managers;
import server.exception.ResponseException;
import task.Epic;
import task.Subtask;
import task.Task;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;


public class HttpTaskServerTest {

    private HttpTaskServer httpTaskServer;
    private HttpTaskManager manager;
    private Gson gson;
    private KVServer server;
    private Task task1;
    private Epic epic1;
    private Subtask subtask1;

    @BeforeEach
    public void setup() throws IOException {
        server = new KVServer();
        server.start();
        manager = (HttpTaskManager) Managers.getDefault();
        httpTaskServer = new HttpTaskServer(manager);

        manager.deleteTasks();
        manager.deleteEpics();
        manager.deleteTasks();

        task1 = new Task(
                "Task1",
                "Task1",
                30,
                "2022-08-30T06:00:00");
        Task task2 = new Task(
                "Task2",
                "Task2",
                30,
                "2022-09-30T06:00:00");

        epic1 = new Epic(
                "Epic1",
                "Epic1");

        subtask1 = new Subtask(
                2,
                "SubTask1",
                "SubTask1",
                30,
                "2022-08-30T09:00:00");

        int taskId1 = manager.addTask(task1);
        int taskId2 = manager.addTask(task2);
        int epicId = manager.addEpic(epic1);
        int subtaskId = manager.addSubtask(subtask1);
        manager.getTask(taskId1);
        manager.getTask(taskId2);
        manager.getEpic(epicId);
        manager.getSubtask(subtaskId);
        httpTaskServer.start();
        gson = new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @AfterEach
    public void after() {
        server.stop();
        httpTaskServer.stop();
    }

    @Test
    void shouldReturnListOfTasksWithSize1() {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        int expectedSize = 1;
        String body = getBodyResponse(request);
        JsonElement jsonElement = JsonParser.parseString(body);
        //assertTrue(jsonElement.isJsonObject(), "Wrong answer from server");
        //JsonObject jsonObject = jsonElement.getAsJsonObject();
        //JsonArray jsonArray = jsonElement.getAsJsonArray(String.valueOf(TASKS));
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        assertNotNull(jsonArray, "Tasks don't return");
        assertEquals(expectedSize, jsonArray.size(), "Wrong tasks size");
    }

    @Test
    void shouldRemoveAllTasks() {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        int expectedSize = 0;
        String body = getBodyResponse(request);
        int testSize = Integer.parseInt(body);
        assertEquals(expectedSize, testSize, "Wrong tasks size");
    }

    @Test
    void shouldReturnTaskWithId1() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        URI url = URI.create("http://localhost:8080/tasks/task?id=1");
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(manager.getTasks().get(0), task1, "Неверная задача.");
    }

    @Test
    void shouldNotReturnTaskWithId5() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        URI url = URI.create("http://localhost:8080/tasks/task/?id=5");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String expected = TASK.name() + " " + NOT_FOUND;
        assertEquals(200, response.statusCode());
        String responseBody = getBodyResponse(request);
        assertEquals(expected, responseBody, "Task found");
    }

    @Test
    void shouldAddNewTask() {
        Task newTask = new Task(
                "New Task",
                "New Task",
                30,
                "2022-08-24T06:00:00");
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        String expected = TASK.name() + " " + CREATED;
        String responseBody = getBodyResponse(request);
        assertEquals(expected, responseBody, "Task not created");
    }

    @Test
    void shouldNotAddSameTask() {
        Task newTask = new Task(
                "Task1",
                "Task1",
                30,
                "2022-08-30T06:00:00");
        newTask.setId(1);
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        String expected = TASK.name() + " " + ALREADY_EXISTS;
        String responseBody = getBodyResponse(request);
        assertEquals(expected, responseBody, "Task created");
    }

    @Test
    void shouldUpdateTask() {
        Task newTask = new Task(
                "Updated Task",
                "Updated Task",
                30,
                "2022-08-30T12:00:00");
        newTask.setId(1);
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        String expected = TASK.name() + " " + UPDATED;
        String responseBody = getBodyResponse(request);
        assertEquals(expected, responseBody, "Task not updated");
    }

    @Test
    void shouldNotUpdateTask() {
        URI url = URI.create("http://localhost:8080/tasks/task");
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString("""
                {
                  "name": "Updated Task",
                  "description": "Updated Task",
                  "id": 1,
                  "state": "NEW",
                  "startTime": "2022-08-28T12:00"
                }""");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        String expected = TASK.name() + " " + HAS_NULL_FIELDS;
        String responseBody = getBodyResponse(request);
        assertEquals(expected, responseBody, "Task updated");
    }

    @Test
    void shouldRemoveTaskWithId1() {
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        String responseBody = getBodyResponse(request);
        String expected = TASK.name() + " " + DELETED;
        assertEquals(expected, responseBody, "Task not deleted");
    }

    @Test
    void shouldNotRemoveTaskWithId3() {
        URI url = URI.create("http://localhost:8080/tasks/task/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        String responseBody = getBodyResponse(request);
        String expected = TASK.name() + " " + NOT_DELETED;
        assertEquals(expected, responseBody, "Task deleted");
    }

    @Test
    void shouldReturnListOfEpicsWithSize3() {
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        int expectedSize = 3;
        String body = getBodyResponse(request);
        JsonElement jsonElement = JsonParser.parseString(body);
        assertTrue(jsonElement.isJsonObject(), "Wrong answer from server");
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray(String.valueOf(EPICS));
        assertNotNull(jsonArray, "Epics don't return");
        assertEquals(expectedSize, jsonArray.size(), "Wrong epics size");
    }

    @Test
    void shouldRemoveAllEpics() {
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        int expectedSize = 0;
        String body = getBodyResponse(request);
        int testSize = Integer.parseInt(body);
        assertEquals(expectedSize, testSize, "Wrong epics size");
    }

    @Test
    void shouldReturnEpicWithId3() {
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        int expectedId = 3;
        String body = getBodyResponse(request);
        JsonElement jsonElement = JsonParser.parseString(body);
        assertTrue(jsonElement.isJsonObject(), "Wrong answer from server");
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int testId = jsonObject.get("id").getAsInt();
        assertEquals(expectedId, testId, "Wrong epic");
    }

    @Test
    void shouldNotReturnEpicWithId1() {
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        String expected = EPIC.name() + " " + NOT_FOUND;
        String responseBody = getBodyResponse(request);
        assertEquals(expected, responseBody, "Epic found");
    }

    @Test
    void shouldAddNewEpic() {
        Epic newEpic = new Epic("New Epic", "New Epic");
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(newEpic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        String expected = EPIC.name() + " " + CREATED;
        String responseBody = getBodyResponse(request);
        assertEquals(expected, responseBody, "Epic not created");
    }

    @Test
    void shouldNotAddSameEpic() {
        Epic newEpic = new Epic("Epic1", "Epic1");
        newEpic.setId(3);
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(newEpic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        String expected = EPIC.name() + " " + ALREADY_EXISTS;
        String responseBody = getBodyResponse(request);
        assertEquals(expected, responseBody, "Epic created");
    }

    @Test
    void shouldNotAddSameSubTask() {
        Subtask newSubTask = new Subtask(
                3,
                "SubTask1",
                "SubTask1",
                30,
                "2022-08-30T09:00:00");
        newSubTask.setId(6);
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String json = gson.toJson(newSubTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        String expected = SUBTASK.name() + " " + ALREADY_EXISTS;
        String responseBody = getBodyResponse(request);
        assertEquals(expected, responseBody, "Subtask created");
    }

    @Test
    void shouldUpdateEpic() {
        List<Integer> subTasksIds = List.of(6, 7);
        Epic newEpic = new Epic(
                "Updated Task",
                "Updated Task");
        newEpic.setId(3);
        newEpic.setStartTime(LocalDateTime.parse("2022-08-30T09:00:00"));
        newEpic.setDuration(60L);
        newEpic.setEndTime(LocalDateTime.parse("2022-08-30T10:30:00"));
        newEpic.setSubtasksIds(subTasksIds);
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = gson.toJson(newEpic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        String expected = EPIC.name() + " " + UPDATED;
        String responseBody = getBodyResponse(request);
        assertEquals(expected, responseBody, "Epic not updated");
    }

    @Test
    void shouldNotUpdateEpic() {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString("""
                {
                      "endTime": "2022-08-30T10:30",
                      "name": "Epic1",
                      "description": "Epic1",
                      "id": 3,
                      "state": "NEW",
                      "duration": "PT1H",
                      "startTime": "2022-08-30T09:00"
                    }""");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        String expected = EPIC.name() + " " + HAS_NULL_FIELDS;
        String responseBody = getBodyResponse(request);
        assertEquals(expected, responseBody, "Epic updated");
    }

    @Test
    void shouldDeleteEpicWithId3() {
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        String responseBody = getBodyResponse(request);
        String expected = EPIC.name() + " " + DELETED;
        assertEquals(expected, responseBody, "Epic not deleted");
    }

    @Test
    void shouldNotRemoveEpicWithId1() {
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        String responseBody = getBodyResponse(request);
        String expected = EPIC.name() + " " + NOT_DELETED;
        assertEquals(expected, responseBody, "Epic deleted");
    }

    @Test
    void shouldReturnListSubtasksOfEpicWithSize2() {
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        int expectedSize = 2;
        String body = getBodyResponse(request);
        JsonElement jsonElement = JsonParser.parseString(body);
        assertTrue(jsonElement.isJsonObject(), "Wrong answer from server");
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray(SUBTASKS.name());
        assertNotNull(jsonArray, "Subtasks don't return");
        assertEquals(expectedSize, jsonArray.size(), "Wrong subtasks size");
    }

    @Test
    void shouldNotReturnListSubtasksOfEpicWithWrongEpicId() {
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic?id=10");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        String responseBody = getBodyResponse(request);
        String expected = EPIC.name() + " " + NOT_FOUND;
        assertEquals(expected, responseBody, "SubTasks of epic returned");
    }

    @Test
    void shouldReturnListSubtasksOfEpicWithSize0() {
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic?id=5");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        int expectedSize = 0;
        String body = getBodyResponse(request);
        int testSize = Integer.parseInt(body);
        assertEquals(expectedSize, testSize, "Wrong subtasks size");
    }

    @Test
    void shouldReturnListOfSubtasksWithSize4() {
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        int expectedSize = 4;
        String body = getBodyResponse(request);
        JsonElement jsonElement = JsonParser.parseString(body);
        assertTrue(jsonElement.isJsonObject(), "Wrong answer from server");
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray(String.valueOf(SUBTASKS));
        assertNotNull(jsonArray, "SubTasks don't return");
        assertEquals(expectedSize, jsonArray.size(), "Wrong subtasks size");
    }

    @Test
    void shouldRemoveAllSubtasks() {
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        int expectedSize = 0;
        String body = getBodyResponse(request);
        int testSize = Integer.parseInt(body);
        assertEquals(expectedSize, testSize, "Wrong subtasks size");
    }

    @Test
    void shouldReturnSubtaskWithId6() {
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=6");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        int expectedId = 6;
        String body = getBodyResponse(request);
        JsonElement jsonElement = JsonParser.parseString(body);
        assertTrue(jsonElement.isJsonObject(), "Wrong answer from server");
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        int testId = jsonObject.get("id").getAsInt();
        assertEquals(expectedId, testId, "Subtasks not equal");
    }

    @Test
    void shouldNotReturnSubtaskWithId1() {
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        String expected = SUBTASK.name() + " " + NOT_FOUND;
        String responseBody = getBodyResponse(request);
        assertEquals(expected, responseBody, "Subtask found");
    }

    @Test
    void shouldAddNewSubtask() {
        Subtask newSubTask = new Subtask(
                "NewSubTask",
                "NewSubTask",
                3);
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String json = gson.toJson(newSubTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        String expected = SUBTASK.name() + " " + CREATED;
        String responseBody = getBodyResponse(request);
        assertEquals(expected, responseBody, "Subtask not created");
    }

    @Test
    void shouldUpdateSubtask() {
        Subtask newSubtask = new Subtask(
                "Updated SubTask",
                "Updated SubTask",
                3);
        newSubtask.setId(6);
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String json = gson.toJson(newSubtask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        String expected = SUBTASK.name() + " " + UPDATED;
        String responseBody = getBodyResponse(request);
        assertEquals(expected, responseBody, "Subtask not updated");
    }

    @Test
    void shouldNotUpdateSubtask() {
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString("""
                {
                      "name": "SubTask1",
                      "description": "SubTask1",
                      "id": 6,
                      "state": "NEW",
                      "duration": 30,
                      "startTime": "2022-08-30T09:00"
                    }""");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        String expected = SUBTASK.name() + " " + HAS_NULL_FIELDS;
        String responseBody = getBodyResponse(request);
        assertEquals(expected, responseBody, "Subtask updated");
    }

    @Test
    void shouldReturnResponseNotJsonObject() {
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString("""
                {
                      "name": "SubTask1",
                      "description": "SubTask1",
                  """);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        Exception exception = assertThrows(ResponseException.class, () -> getBodyResponse(request));
        assertEquals("No response from server", exception.getMessage(), "Exception not thrown");
    }

    @Test
    void shouldRemoveSubtaskWithId6() {
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=6");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        String responseBody = getBodyResponse(request);
        String expected = SUBTASK.name() + " " + DELETED;
        assertEquals(expected, responseBody, "Subtask not deleted");
    }

    @Test
    void shouldNotRemoveSubtaskWithId1() {
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        String responseBody = getBodyResponse(request);
        String expected = SUBTASK.name() + " " + NOT_DELETED;
        assertEquals(expected, responseBody, "Subtask deleted");
    }

    @Test
    void shouldReturnHistoryWithSize2() {
        URI url1 = URI.create("http://localhost:8080/tasks/task/?id=1");
        URI url2 = URI.create("http://localhost:8080/tasks/task/?id=2");
        HttpRequest getRequest = HttpRequest.newBuilder().uri(url1).GET().build();
        getBodyResponse(getRequest);
        getRequest = HttpRequest.newBuilder().uri(url2).GET().build();
        getBodyResponse(getRequest);

        URI url = URI.create("http://localhost:8080/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        int expectedSize = 2;
        String body = getBodyResponse(request);
        JsonElement jsonElement = JsonParser.parseString(body);
        assertTrue(jsonElement.isJsonObject(), "Wrong answer from server");
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray(String.valueOf(HISTORY_TASKS));
        assertNotNull(jsonArray, "History don't return");
        assertEquals(expectedSize, jsonArray.size(), "Wrong history size");
    }

    @Test
    void shouldReturnHistoryWithSize0() {
        URI url = URI.create("http://localhost:8080/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        int expectedSize = 0;
        String body = getBodyResponse(request);
        int testSize = Integer.parseInt(body);
        assertEquals(expectedSize, testSize, "Wrong history size");
    }

    @Test
    void shouldReturnPrioritizedTasksWithSize6() {
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        int expectedSize = 6;
        String body = getBodyResponse(request);
        JsonElement jsonElement = JsonParser.parseString(body);
        assertTrue(jsonElement.isJsonObject(), "Wrong answer from server");
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray(String.valueOf(PRIORITIZED_TASKS));
        assertNotNull(jsonArray, "PrioritizedTasks don't return");
        assertEquals(expectedSize, jsonArray.size(), "Wrong prioritizedTasks size");
    }

    @Test
    void shouldReturnPrioritizedTasksWithSize0() {
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        getBodyResponse(request);

        url = URI.create("http://localhost:8080/tasks/task/");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        getBodyResponse(request);

        url = URI.create("http://localhost:8080/tasks/epic/");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        getBodyResponse(request);

        url = URI.create("http://localhost:8080/tasks");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        int expectedSize = 0;
        String body = getBodyResponse(request);
        int testSize = Integer.parseInt(body);
        assertEquals(expectedSize, testSize, "Wrong prioritizedTasks size");
    }

    @Test
    void shouldReturnTaskWrongMethod() {
        Subtask newSubtask = new Subtask(
                "Updated SubTask",
                "Updated SubTask",
                3);
        String json = gson.toJson(newSubtask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(body).build();
        String responseBody = getBodyResponse(request);
        String expected = "Wrong method";
        assertEquals(expected, responseBody, "Put method exists in Task");
    }

    @Test
    void shouldReturnSubTaskWrongMethod() {
        Subtask newSubtask = new Subtask(
                "Updated SubTask",
                "Updated SubTask",
                3);
        String json = gson.toJson(newSubtask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(body).build();
        String responseBody = getBodyResponse(request);
        String expected = "Wrong method";
        assertEquals(expected, responseBody, "Put method exists in Subtask");
    }

    @Test
    void shouldReturnEpicWrongMethod() {
        Subtask newSubtask = new Subtask(
                "Updated SubTask",
                "Updated SubTask",
                3);
        String json = gson.toJson(newSubtask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(body).build();
        String responseBody = getBodyResponse(request);
        String expected = "Wrong method";
        assertEquals(expected, responseBody, "Put method exists in Epic");
    }

    @Test
    void shouldReturnHistoryWrongMethod() {
        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        String responseBody = getBodyResponse(request);
        String expected = "Wrong method";
        assertEquals(expected, responseBody, "Delete method exists in History");
    }

    @Test
    void shouldNotReturnPrioritizedTasksWithWrongMethod() {
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        String responseBody = getBodyResponse(request);
        String expected = "Wrong method";
        assertEquals(expected, responseBody, "PrioritizedTasks return");
    }

    @Test
    void shouldNotAddTaskOverlapByTime() {
        Task newTask = new Task(
                "TestTask",
                "TestTask",
                30,
                "2022-08-30T06:00:00");
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        String expected = TASK.name() + " " + OVERLAP_BY_TIME;
        String responseBody = getBodyResponse(request);
        assertEquals(expected, responseBody, "Task created");
    }

    @Test
    void shouldNotAddSubTaskOverlapByTime() {
        Subtask subTask = new Subtask(
                3,
                "TestSubTask",
                "TestSubTask",
                30,
                "2022-08-30T06:00:00");
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String json = gson.toJson(subTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        String expected = SUBTASK.name() + " " + OVERLAP_BY_TIME;
        String responseBody = getBodyResponse(request);
        assertEquals(expected, responseBody, "Subtask created");
    }

    @Test
    void shouldReturnNoResponse() {
        URI url = URI.create("http://localhost:8055/ta");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        Exception exception = assertThrows(ResponseException.class, () -> getBodyResponse(request));
        assertEquals("No response from server", exception.getMessage(), "Exception not thrown");
    }

    public String getBodyResponse(HttpRequest httpRequest) {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ResponseException("No response from server");
        }
        return response.body();
    }

}