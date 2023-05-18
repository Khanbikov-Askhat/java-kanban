package task;

import manager.TaskStatus;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected TaskStatus status;


    public Task(String name, String description) {

        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;

    }

    public Task(String name, String description, int id) {

        this.name = name;
        this.description = description;
        this.id = id;
        this.status = TaskStatus.NEW;

    }

    @Override
    public String toString() {
        return "Task{" + // имя класса
                "name='" + name + '\'' + // название
                ", description='" + description + '\'' + // описание
                ", id=" + id + // айди
                ", status=" + status + // статус
                '}';
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
        Task otherBook = (Task) obj;
        return Objects.equals(name, otherBook.name) &&
                Objects.equals(description, otherBook.description) &&
                (id == otherBook.id) &&
                Objects.equals(status, otherBook.status);
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


}
