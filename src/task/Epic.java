package task;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name,
                String description) {

        super(name, description);
    }

    public Epic(String name,
                String description,
                int id) {

        super(name, description, id);
    }


    public Epic(String taskLine) {
        super(taskLine);
    }

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }

    @Override
    public String toString() {
        return id +
                ",EPIC" + "," +
                name + "," +
                status + "," +
                description
                ;
    }

    public int getEpicId() {
        return id;
    }

    public void setSubtaskIds(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public int getSubtaskIdsSize() {
        return subtaskIds.size();
    }

    public void setId(int newId) {
        id = newId;
    }

    public void cleanSubtaskIds() {
        subtaskIds.clear();
    }

    public ArrayList<Integer> getSubtaskIds () {
        return subtaskIds;
    }

    public void removeSubtask(Object o) {
        subtaskIds.remove(o);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

}
