package task;

public class Task {
    public String name;
    public String description;
    private int id;
    public String status;


    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        //this.id = id;
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
}
