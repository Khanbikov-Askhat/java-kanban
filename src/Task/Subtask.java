package task;

public class Subtask extends Task {

    private final int epicId;
    private int id;
    public Subtask(String name, String description, String status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
        //this.id = id;
    }

    @Override
    public String toString() {
        return "Subtask{" + // имя класса
                "name='" + name + '\'' + // Название подзадачи
                ", description='" + description + '\'' + // Описание задачи
                ", id=" + id + // ID подзадачи
                ", status=" + status + // Статус подзадачи
                ", epicId=" + epicId + // ID эпика
                '}';
    }

    public int getEpicId() {
        return epicId;
    }

    public void setId(int newId) {
        id = newId;
    }
}

