package manager;

import adapters.LocalDateTimeAdapter;
import com.google.gson.*;
import server.KVTaskClient;
import task.Task;


import java.time.LocalDateTime;
import java.util.stream.Collectors;


public class HttpTaskManager extends FileBackedTasksManager {
    private final Gson gson = new GsonBuilder().
            registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).
            create();
    private final KVTaskClient kvTaskClient;
    private String key;

    public HttpTaskManager(String url, int port, String key) {
        this.kvTaskClient = new KVTaskClient(url, port);
        this.key = key;
    }

    @Override
    protected void save() {

        kvTaskClient.put("/tasks", gson.toJson(getTasks()));
        kvTaskClient.put("/epics", gson.toJson(getEpics()));
        kvTaskClient.put("/subtasks", gson.toJson(getSubtasks()));
        kvTaskClient.put("/history", gson.toJson(getDefaultHistory().stream()
                .map(Task::getId)
                .collect(Collectors.toList())));
    }

}