package adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import manager.HistoryManager;
import manager.HttpTaskManager;
import manager.InMemoryHistoryManager;
import manager.Managers;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.util.List;

public class InMemoryHistoryManagerAdapter extends TypeAdapter<InMemoryHistoryManager> {

    HttpTaskManager taskManager;

    public InMemoryHistoryManagerAdapter(HttpTaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public InMemoryHistoryManager read(JsonReader reader) throws IOException {
        HistoryManager historyManager = Managers.getDefaultHistory();
        List<Task> tasks = taskManager.getTasks();
        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubtasks();

        String jsonHistory = reader.nextString();
        String[] parts = jsonHistory.split(",");
        for (int i = 0; i < parts.length; i++) {
            if (tasks.get(i) != null) {
                historyManager.add(tasks.get(i));
            }
            if (epics.get(i) != null) {
                historyManager.add(epics.get(i));
            }
            if (subtasks.get(i) != null) {
                historyManager.add(subtasks.get(i));
            }
        }
        return (InMemoryHistoryManager) historyManager;
    }

    public void write(JsonWriter writer, InMemoryHistoryManager value) throws IOException {
        StringBuilder result = new StringBuilder();
        List<Task> historyTasks = taskManager.getDefaultHistory();
        for (Task task : historyTasks) {
            result.append(task.getId()).append(",");
        }
        writer.value(result.toString());
    }
}
