package Task;
import java.util.ArrayList;


public class Epic extends Task {
    ArrayList<Subtask> subtasks;

    public Epic(String taskName, String taskDescription, int taskId, String taskStatus, ArrayList<Subtask> subtasks) {
        super(taskName, taskDescription, taskId, taskStatus);
        this.subtasks = subtasks;
    }
}
