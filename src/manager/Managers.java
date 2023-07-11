package manager;

import java.io.File;

public final class Managers {

    public static TaskManager getDefault() {
        return new FileBackedTasksManager(new File("resources/task.csv"));
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
    
}
