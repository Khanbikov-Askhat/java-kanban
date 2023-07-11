package manager;

import task.*;

import java.util.ArrayList;
import java.util.List;

public class CSVTaskFormat {
    public static String getHeader() {
        return "id,type,name,status,description,epic";
    }

    public static String toString(HistoryManager historyManager) {
        final List<Task> history = historyManager.getHistory();
        if (history.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(history.get(0).getId());
        for (int i = 1; i < history.size(); i++) {
            Task task = history.get(i);
            sb.append(",");
            sb.append(task.getId());
        }
        return sb.toString();
    }

    public static String toString(Task task) {
        return task.getId() + "," + task.getTaskType() + "," + task.getName() + "," +
                task.getStatus() + "," + task.getDescription() + "," +
                (task.getTaskType().equals(TaskType.SUBTASK) ? ((Subtask)task).getEpicId() : "");
    }

    static List<Integer> historyFromString(String historyLine) {
        List<Integer> ids = new ArrayList<>();
        String[] historyFields = historyLine.split(",");
        for (String field : historyFields) {
            int id = Integer.parseInt(field);
            ids.add(id);
        }
        return ids;
    }

    public static Task tasksFromString(String value) {
        final String[] values = value.split(",");
        final int id = Integer.parseInt(values[0]);
        final TaskType type = TaskType.valueOf(values[1]);
        final String name = values[2];
        final TaskStatus status = TaskStatus.valueOf(values[3]);
        final String description = values[4];
        if (type == TaskType.TASK) {
            return new Task(id, name, description, status);
        }
        if (type == TaskType.SUBTASK) {
            final int epicId = Integer.parseInt(values[5]);
            return new Subtask(id, name, description, status, epicId);
        }
        return new Epic(id, name, description, status);
    }


}
