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

    @Override
    public String toString() {
        String result = "Epic{" + // имя класса
                "name='" + name + '\'' + // Название
                ", description='" + description + '\'' + // Описание
                ", id=" + id + // айди
                ", status=" + status + // статус
                '}';

        if (subtaskIds != null) {
            result = result + ", subtaskIds.length=" + subtaskIds.size();
        } else {
            result = result + ", subtaskIds.length=null";
        }
        return result + '}';
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
}
