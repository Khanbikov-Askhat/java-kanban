package manager;


import adapters.LocalDateTimeAdapter;
import com.google.gson.*;
import server.KVTaskClient;
import task.Epic;
import task.Subtask;
import task.Task;


import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
    private final KVTaskClient kvTaskClient = new KVTaskClient(); // HttpTaskManager создаёт KVTaskClient, из которого можно получить исходное состояние менеджера

    public HttpTaskManager(boolean toLoad) {
        if (toLoad) {
            load();
        }
    }

    public HttpTaskManager() {
        this(false);
    }

    private void load() {
        JsonElement jsonTask = JsonParser.parseString(kvTaskClient.load("/tasks"));
        JsonArray jsonTaskArray = jsonTask.getAsJsonArray();
        for (JsonElement element : jsonTaskArray) {
            Task task = gson.fromJson(element, Task.class);
            int id = task.getId();
            tasks.put(id, task);
            add(task);
            if (id > generatorId) {
                generatorId = id;
            }
        }
        JsonElement jsonEpic = JsonParser.parseString(kvTaskClient.load("/epics"));
        JsonArray jsonEpicArray = jsonEpic.getAsJsonArray();
        for (JsonElement element : jsonEpicArray) {
            Epic epic = gson.fromJson(element, Epic.class);
            int id = epic.getId();
            epics.put(id, epic);
            if (id > generatorId) {
                generatorId = id;
            }
        }
        JsonElement jsonSubtask = JsonParser.parseString(kvTaskClient.load("/subtasks"));
        JsonArray jsonSubtaskArray = jsonSubtask.getAsJsonArray();
        for (JsonElement element : jsonSubtaskArray) {
            Subtask subtask = gson.fromJson(element, Subtask.class);
            int id = subtask.getId();
            subtasks.put(id, subtask);
            add(subtask);
            if (id > generatorId) {
                generatorId = id;
            }
        }
        JsonElement jsonHistory = JsonParser.parseString(kvTaskClient.load("/history"));
        JsonArray jsonHistoryArray = jsonHistory.getAsJsonArray();
        for (JsonElement jsonId : jsonHistoryArray) {
            int id = jsonId.getAsInt();
            if (tasks.containsKey(id)) {
                historyManager.add(tasks.get(id));
            } else if (epics.containsKey(id)) {
                historyManager.add(epics.get(id));
            } else if (subtasks.containsKey(id)) {
                historyManager.add(subtasks.get(id));
            }
        }
    }

    @Override
    public void save() {
        kvTaskClient.put("/tasks", gson.toJson(getTasks()));
        kvTaskClient.put("/epics", gson.toJson(getEpics()));
        kvTaskClient.put("/subtasks", gson.toJson(getSubtasks()));
        kvTaskClient.put("/history", gson.toJson(getDefaultHistory().stream().map(Task::getId).collect(Collectors.toList())));
    }
}