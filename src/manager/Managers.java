package manager;
import task.Epic;
import task.Subtask;
import task.Task;
import java.util.ArrayList;


public final class Managers {



    public static TaskManager getDefault() {
        return new TaskManager() {
            @Override
            public int addTask(Task task) {
                return 0;
            }

            @Override
            public int addEpic(Epic epic) {
                return 0;
            }

            @Override
            public Integer addSubtask(Subtask subtask) {
                return null;
            }

            @Override
            public ArrayList<Task> getTasks() {
                return null;
            }

            @Override
            public ArrayList<Epic> getEpics() {
                return null;
            }

            @Override
            public ArrayList<Subtask> getSubtasks() {
                return null;
            }

            @Override
            public void deleteTasks() {

            }

            @Override
            public void deleteEpics() {

            }

            @Override
            public void deleteSubtasks() {

            }

            @Override
            public Task getTask(int id) {
                return null;
            }

            @Override
            public Epic getEpic(int id) {
                return null;
            }

            @Override
            public Subtask getSubtask(int id) {
                return null;
            }

            @Override
            public void updateTask(Task task, String newStatus) {

            }

            @Override
            public void updateEpic(Epic epic) {

            }

            @Override
            public void updateSubtask(Subtask subtask, String newStatus) {

            }

            @Override
            public void deleteTask(int id) {

            }

            @Override
            public void deleteEpic(int id) {

            }

            @Override
            public void deleteSubtask(int id) {

            }

            @Override
            public ArrayList<Subtask> getEpicsSubtasks(int epicId) {
                return null;
            }

        };
    }

    static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }


}
