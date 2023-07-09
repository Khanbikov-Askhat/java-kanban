package task;

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
}

