package task;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected String status;


    public Task(String name, String description, int id, String status) {

        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;

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
        status = newStatus;
    }

    public String getStatus() {
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
