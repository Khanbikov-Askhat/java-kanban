package task;
import exceptions.TaskCreateException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected TaskStatus status;
    protected long duration;
    protected LocalDateTime startTime;


    public Task(String name, String description) {

        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;

    }

    public Task(String name, String description, int id) {
        if (name == null || description == null) {
            throw new TaskCreateException("Can't create Task");
        }
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = TaskStatus.NEW;

    }

    public Task(int id, String name, String description, String startTime, long duration) {
        if (name == null || description == null || startTime == null ) {
            throw new TaskCreateException("Can't create Task");
        }
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = TaskStatus.NEW;
        this.duration = duration;
        this.startTime = LocalDateTime.parse(startTime);

    }

    public Task(String name, String description, long durationMinutes, String startTime) {
        if (name == null || description == null || startTime == null ) {
            throw new TaskCreateException("Can't create Task");
        }
        this.name = name;
        this.description = description;
        this.id = 0;
        this.status = TaskStatus.NEW;
        this.duration = durationMinutes;
        this.startTime = LocalDateTime.parse(startTime);
    }

    public Task(String taskLine) {
        String[] task = taskLine.split(",");
        this.id = Integer.parseInt(task[0]);
        this.name = task[2];
        this.status = status.valueOf(task[3]);
        this.description = task[4];
    }

    public Task(int id, String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    @Override
    public String toString() {
        return id +
                ",TASK" + "," +
                name + "," +
                status + "," +
                description
                ;
    }

    public void setId(int newId) {
        id = newId;
    }

    public void setStatus(String newStatus) {
        switch(newStatus) {
            case "NEW":
                this.status = TaskStatus.NEW;
                break;
            case "IN_PROGRESS":
                this.status = TaskStatus.IN_PROGRESS;
                break;
            case "DONE":
                this.status = TaskStatus.DONE;
                break;
            default:
                System.out.println("Такого статуса нет");
        }
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setDescription(String newDescription) {
        description = newDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false; // можно оформить и так
        Task task = (Task) obj;
        return Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                (id == task.id) &&
                Objects.equals(status, task.status) &&
                task.startTime.isEqual(startTime) &&
                task.duration == duration;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (name != null) {
            hash = hash + name.hashCode();
        }
        hash = hash * 31;

        if (description != null) {
            hash = hash + description.hashCode();
        }
        return hash;
    }

    public TaskType getTaskType() {
        return TaskType.TASK;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime.isEqual(LocalDateTime.MAX)) {
            return LocalDateTime.MIN;
        } else {
            return startTime.plus(Duration.ofMinutes(duration));
        }
    }
}
