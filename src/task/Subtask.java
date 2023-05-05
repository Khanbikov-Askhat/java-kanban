package task;

public class Subtask extends Task {

    private int epicId;

    public Subtask(String name,
                   String description,
                   int id,
                   String status,
                   int epicId) {

        super(name, description, id, status);

        this.epicId = epicId;
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

    public void setEpicId(int newEpicId) {
        epicId = newEpicId;
    }

}

