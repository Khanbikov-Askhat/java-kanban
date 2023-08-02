package manager;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private TaskManager manager;

    @BeforeEach
    public void createTaskManager() {
        manager = Managers.getInMemoryHistoryManager();
        Task task1 = new Task(
                "Task1",
                "Task1",
                30,
                "2023-07-24T06:00:00");
        Task task2 = new Task(
                "Task2",
                "Task2",
                30,
                "2023-07-24T08:00:00");
        Epic epic1 = new Epic(
                "Epic1",
                "Epic1");
        Subtask subTask1 = new Subtask(
                3, "SubTask1",
                "SubTask1",
                30,
                "2023-07-24T10:00:00");
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addSubtask(subTask1);
    }
    //ДОЛЖЕН ДОБАВИТЬ ТАСКУ В ПУСТУЮ ИСТОРИЮ
    @Test
    void shouldAddTaskToEmptyHistory() {
        manager.getTask(1);
        manager.getSubtask(4);
        List<Task> history = manager.getDefaultHistory();
        assertNotNull(history, "History don't return");
        int expected = 2;
        assertEquals(expected, history.size(), "History size not equal");
    }
    //ПРОВЕРКА НА ДУБЛИ
    @Test
    void addTaskWithSameTaskInHistory() {
        manager.getTask(1);
        manager.getTask(1);
        List<Task> history = manager.getDefaultHistory();
        assertNotNull(history, "History don't return");
        int expected = 1;
        assertEquals(expected, history.size(), "History size not equal");
    }
    //УДАЛЕНИЕ ИЗ НАЧАЛА
    @Test
    void shouldDeleteFromBegin() {
        manager.getTask(1);
        manager.getTask(2);
        manager.getEpic(3);
        manager.getHistoryManager().remove(1);
        List<Task> history = manager.getDefaultHistory();
        assertNotNull(history, "History don't return");
        int expected = 2;
        assertEquals(expected, history.size(), "History size not equal");
    }
    //УДАЛЕНИЕ ИЗ СЕРЕДИНЫ
    @Test
    void shouldDeleteFromMiddle() {
        manager.getTask(1);
        manager.getTask(2);
        manager.getEpic(3);
        manager.getHistoryManager().remove(2);
        List<Task> history = manager.getDefaultHistory();
        assertNotNull(history, "History don't return");
        int expected = 2;
        assertEquals(expected, history.size(), "History size not equal");
    }
    //УДАЛЕНИЕ ИЗ КОНЦА
    @Test
    void shouldDeleteFromEnd() {
        manager.getTask(1);
        manager.getTask(2);
        manager.getEpic(3);
        manager.getHistoryManager().remove(3);
        List<Task> history = manager.getDefaultHistory();
        assertNotNull(history, "History don't return");
        int expected = 2;
        assertEquals(expected, history.size(), "History size not equal");
    }
    //ДОЛЖЕН ВЕРНУТЬ ИСТОРИЮ
    @Test
    void shouldReturnHistory() {
        manager.getTask(1);
        List<Task> history = manager.getDefaultHistory();
        assertNotNull(history, "History don't return");
    }
}