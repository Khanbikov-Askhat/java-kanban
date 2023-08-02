package task;

import exceptions.TaskCreateException;

import java.util.Objects;

public class Subtask extends Task {

    private int epicId;


    public Subtask(String name,
                   String description,
                   int epicId) {

        super(name, description);

        this.epicId = epicId;
    }

    public Subtask(String taskLine) {
        super(taskLine);
        String[] subTask = taskLine.split(",");
        this.epicId = Integer.parseInt(subTask[5]);
    }

    public Subtask(int id, String name, String description, TaskStatus status, Integer epicId) {
        super(id, name, description, status);
        if (name == null || description == null || epicId == null || epicId == 0) {
            throw new TaskCreateException("Can't create Subtask");
        }
        this.epicId = epicId;
    }

    public Subtask(Integer epicId, String name, String description, long durationMinutes, String startTime) {
        super(name, description, durationMinutes, startTime);
        if (name == null || description == null || startTime == null || epicId == null || epicId == 0) {
            throw new TaskCreateException("Can't create Subtask");
        }
        this.epicId = epicId;
    }

    public Subtask(int id, Integer epicId, String name, String description, long durationMinutes, String startTime) {
        super(name, description, durationMinutes, startTime);
        if (name == null || description == null || startTime == null || epicId == null || epicId == 0) {
            throw new TaskCreateException("Can't create Subtask");
        }
        this.id = id;
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return id +
                ",SUBTASK" + "," +
                name + "," +
                status + "," +
                description + "," + epicId
                ;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int newEpicId) {
        epicId = newEpicId;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false; // можно оформить и так
        Subtask subtask = (Subtask) obj;
        return Objects.equals(name, subtask.name) &&
                Objects.equals(description, subtask.description) &&
                Objects.equals(status, subtask.status) &&
                subtask.startTime.isEqual(startTime) &&
                subtask.duration == duration;
    }
}

