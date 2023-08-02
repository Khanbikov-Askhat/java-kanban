package manager;

import java.io.File;

public final class Managers {

    public static TaskManager getDefault(String url, int port, String key) {
        return new HttpTaskManager(url, port, key);
    }

    public static TaskManager getDefaultFileBacked() {
        return new FileBackedTasksManager(new File("resources/task.csv"));
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getInMemoryHistoryManager() {
        return new InMemoryTaskManager();
    }


}
