package task;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds = new ArrayList<>();
    private LocalDateTime endTime;
    public Epic(String name,
                String description) {

        super(name, description, null, LocalDateTime.MAX.toString());
        endTime = LocalDateTime.MIN;
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

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void increaseDuration(Long duration) {
        this.duration = this.duration + duration;
    }

    public void subtractionDuration(Long duration) {
        this.duration = this.duration - duration;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false; // можно оформить и так
        Epic epic = (Epic) obj;
        return Objects.equals(name, epic.name) &&
                Objects.equals(description, epic.description) &&
                Objects.equals(status, epic.status);
    }
}
