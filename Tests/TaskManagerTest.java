import exceptions.TaskOverlapAnotherTaskException;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest <T extends TaskManager>{

    private T taskManager;

    abstract T createTaskManager();

    @BeforeEach
    public void setup() {
        taskManager = createTaskManager();
        Task task1 = new Task(
                "Task1",
                "Task1",
                "PT30M",
                "2023-07-24T06:00:00");

        Task task2 = new Task(
                "Task2",
                "Task2",
                "PT30M",
                "2023-07-24T14:00:00");

        Epic epic1 = new Epic(
                "Epic1",
                "Epic1");

        Epic epic2 = new Epic(
                "Epic2",
                "Epic2");

        Epic epic3 = new Epic(
                "Epic3",
                "Epic3");

        Subtask subtask1 = new Subtask(
                3, "SubTask1",
                "SubTask1",
                "PT30M",
                "2023-07-24T09:00:00");

        Subtask subtask2 = new Subtask(
                3, "SubTask2",
                "SubTask2",
                "PT30M",
                "2023-07-24T10:00:00");

        Subtask subtask3 = new Subtask(
                4, "SubTask3",
                "SubTask3",
                "PT30M",
                "2023-07-23T09:00:00");

        Subtask subtask4 = new Subtask(
                4, "SubTask4",
                "SubTask4",
                "PT30M",
                "2023-07-22T12:00:00");

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        taskManager.addSubtask(subtask4);
    }


    //ДОБАВЛЕНИЕ ЗАДАЧ__________________________________________________________________________________________________
    //ДОЛЖЕН ДОБАВИТЬ НОВУЮ ТАСКУ
    @Test
    void shouldAddNewTask() {
        Task expectedTask = new Task(
                "Test Task",
                "Test Task",
                "PT30M",
                "2023-07-25T12:00:00");
        taskManager.addTask(expectedTask);
        Task testedTask = taskManager.getTask(10);
        assertNotNull(testedTask, "Task not found");
        assertEquals(expectedTask, testedTask, "Tasks not equal");
    }
    //ДОЛЖЕН ДОБАВИТЬ НОВЫЙ ЭПИК
    @Test
    void shouldAddNewEpic() {
        Epic expectedEpic = new Epic("Test Epic", "Test Epic");
        taskManager.addEpic(expectedEpic);
        Epic testedEpic = taskManager.getEpic(10);
        assertNotNull(testedEpic, "Epic not found");
        assertEquals(expectedEpic, testedEpic, "Epics not equal");
    }
    //ДОЛЖЕН ДОБАВИТЬ НОВЫЙ САБТАСК
    @Test
    void shouldAddNewSubtask() {
        Subtask expectedSubtask = new Subtask(
                3,
                "Test Subtask",
                "Test Subtask",
                "PT30M",
                "2023-07-24T07:00:00");
        taskManager.addSubtask(expectedSubtask);
        Subtask testedSubtask = taskManager.getSubtask(10);
        assertNotNull(testedSubtask, "Subtask not found");
        assertEquals(expectedSubtask, testedSubtask, "Subtask not equal");
    }
    //НЕ ДОЛЖЕН ДОБАВИТЬ САБТАСК С НЕВЕРНЫМ АЙДИ ЭПИКА В САБТАСКЕ
    @Test
    void shouldNotAddSubtaskWithWrongIdOfEpic() {
        Subtask expectedSubtask = new Subtask(
                10,
                "New Subtask",
                "New Subtask",
                "PT30M",
                "2023-07-24T07:00:00");
        taskManager.addSubtask(expectedSubtask);
        Subtask testedSubtask = taskManager.getSubtask(6);
        assertNotNull(testedSubtask, "Subtask not found");
        assertNotEquals(expectedSubtask, testedSubtask, "Subtasks is equal");
    }


    //ПОЛУЧЕНИЕ ЗАДАЧ___________________________________________________________________________________________________
    //ДОЛЖЕН ВЕРНУТЬ ТАСК С АЙДИ 1
    @Test
    void shouldReturnTaskWithId1() {
        Task testedTask = taskManager.getTask(1);
        int expectedId = 1;
        assertNotNull(testedTask, "Task not found");
        assertEquals(expectedId, testedTask.getId(), "Tasks not equal");
    }
    //ДОЛЖЕН ВЕРНУТЬ САБТАСК С АЙДИ 6
    @Test
    void shouldReturnSubtaskWithId6() {
        Subtask expectedSubtask = taskManager.getSubtask(6);
        int expectedId = 6;
        assertNotNull(expectedSubtask, "Subtask not found");
        assertEquals(expectedId, expectedSubtask.getId(), "Epics not equal");
    }
    //ДОЛЖЕН ВЕРНУТЬ ЭПИК С АЙДИ 3
    @Test
    void shouldReturnEpicWithId3() {
        Epic testedEpic = taskManager.getEpic(3);
        int expectedId = 3;
        assertNotNull(testedEpic, "Epic not found");
        assertEquals(expectedId, testedEpic.getId(), "Epics not equal");
    }
    //ДОЛЖЕН ВЕРНУТЬ СПИСОК ТАСКОВ С РАЗМЕРОМ 2
    @Test
    void shouldReturnListOfTasksWithSize2() {
        List<Task> testedListOfTasks = taskManager.getTasks();
        Task expectedTask = taskManager.getTask(1);
        int expectedSize = 2;
        assertNotNull(testedListOfTasks, "Tasks don't return");
        assertEquals(expectedSize, testedListOfTasks.size(), "Wrong tasks size");
        assertEquals(expectedTask, testedListOfTasks.get(0), "Tasks not equal");
    }
    //ДОЛЖЕН ВЕРНУТЬ СПИСОК ЭПИКОВ С РАЗМЕРОМ 3
    @Test
    void shouldReturnListOfEpicsWithSize3() {
        List<Epic> testedListOfEpics = taskManager.getEpics();
        int expectedSize = 3;
        Epic expectedEpic = taskManager.getEpic(3);
        assertNotNull(testedListOfEpics, "Epics don't return");
        assertEquals(expectedSize, testedListOfEpics.size(), "Wrong epics size");
        assertEquals(expectedEpic, testedListOfEpics.get(0), "Epics not equal");
    }
    //ДОЛЖЕН ВЕРНУТЬ СПИСОК САБТАСКОВ С РАЗМЕРОВ 4
    @Test
    void shouldReturnListOfSubtasksWithSize4() {
        List<Subtask> testedListOfSubtasks = taskManager.getSubtasks();
        int expectedSize = 4;
        Subtask expectedSubtask = taskManager.getSubtask(6);
        assertNotNull(testedListOfSubtasks, "Subtasks don't return");
        assertEquals(expectedSize, testedListOfSubtasks.size(), "Wrong subtasks size");
        assertEquals(expectedSubtask, testedListOfSubtasks.get(0), "Subtasks not equal");
    }
    //ДОЛЖЕН ВЕРНУТЬ СПИСОК САБТАСКОВ ЭПИКА С РАЗМЕРОМ 2
    @Test
    void shouldReturnListSubtasksOfEpicWithSize2() {
        Epic testEpic = taskManager.getEpic(3);
        Subtask expectedSubtask1 = taskManager.getSubtask(6);
        Subtask expectedSubtask2 = taskManager.getSubtask(7);
        List<Subtask> testedSubtasksOfEpic = taskManager.getEpicsSubtasks(testEpic.getEpicId());
        int expectedSize = 2;
        assertNotNull(testedSubtasksOfEpic, "Subtasks don't return");
        assertEquals(expectedSize, testedSubtasksOfEpic.size(), "Wrong subtasks size");
        assertEquals(expectedSubtask1, testedSubtasksOfEpic.get(0), "subtasks not equal");
        assertEquals(expectedSubtask2, testedSubtasksOfEpic.get(1), "subtasks not equal");
    }
    //ДОЛЖЕН ВЕРНУТЬ ЛИСТ САБТАСКОВ ЭПИКА С РАЗМЕРОМ 0
    @Test
    void shouldReturnListSubtasksOfEpicWithSize0() {
        Epic epicWithoutSubTasks = taskManager.getEpic(5);
        List<Subtask> testedSubtasksOfEpic = taskManager.getEpicsSubtasks(epicWithoutSubTasks.getEpicId());
        int expectedSize = 0;
        assertNotNull(testedSubtasksOfEpic, "Subtasks don't return");
        assertEquals(expectedSize, testedSubtasksOfEpic.size(), "Wrong subtasks size");
    }
    //ДОЛЖЕН ВЕРНУТЬ ПУСТОЙ СПИСОК, ЕСЛИ ЭПИКА НЕТ
    @Test
    void shouldReturnEmptyListIfEpicNotExists() {
        Epic testEpic = new Epic(
                "TestEpic",
                "TestEpic");
        List<Subtask> subTasksOfEpic = taskManager.getEpicsSubtasks(testEpic.getEpicId());
        int expectedSize = 0;
        assertEquals(expectedSize, subTasksOfEpic.size());
    }


    //ОБНОВЛЕНИЕ ЗАДАЧ__________________________________________________________________________________________________
    //ДОЛЖЕН ОБНОВИТЬ ТАСКУ
    @Test
    void shouldUpdateTask() {
        Task expectedTask = new Task(
                "Updated Task",
                "Updated Task",
                "PT30M",
                "2023-07-24T12:00:00");
        expectedTask.setId(1);
        taskManager.updateTask(expectedTask, "NEW");
        Task testedTask = taskManager.getTask(1);
        assertNotNull(testedTask, "Task not found");
        assertEquals(expectedTask, testedTask, "Tasks not equal");
    }
    //ДОЛЖЕН ОБНОВИТЬ ЭПИК
    @Test
    void shouldUpdateEpic() {
        Epic expectedEpic = new Epic("Updated Epic", "Updated Epic");
        expectedEpic.setId(3);
        taskManager.updateEpic(expectedEpic);
        Epic testedEpic = taskManager.getEpic(3);
        assertNotNull(testedEpic, "Epic not found");
        assertEquals(expectedEpic, testedEpic, "Epics not equal");
    }
    //ДОЛЖЕН ОБНОВИТЬ САБТАСКУ
    @Test
    void shouldUpdateSubtask() {
        Subtask expectedSubtask = new Subtask(
                3,
                "Updated Subtask",
                "Updated Subtask",
                "PT30M",
                "2023-07-24T07:00:00");
        expectedSubtask.setId(6);
        taskManager.updateSubtask(expectedSubtask, "NEW");
        Subtask testedSubtask = taskManager.getSubtask(6);
        assertNotNull(testedSubtask, "Subtask not found");
        assertEquals(expectedSubtask, testedSubtask, "Subtasks not equal");
    }
    //НЕ ДОЛЖЕН ОБНОВЛЯТЬ ТАСКУ
    @Test
    void shouldNotUpdateTask() {
        Task expectedTask = taskManager.getTask(1);
        taskManager.updateTask(null, "NEW");
        Task testedTask = taskManager.getTask(1);
        assertNotNull(testedTask, "Task not found");
        assertEquals(expectedTask, testedTask, "Tasks not equal");
    }
    //НЕ ДОЛЖЕН ОБНОВЛЯТЬ ЭПИК
    @Test
    void shouldNotUpdateEpic() {
        Epic expectedEpic = taskManager.getEpic(3);
        taskManager.updateEpic(null);
        Epic testedEpic = taskManager.getEpic(3);
        assertNotNull(testedEpic, "Epic not found");
        assertEquals(expectedEpic, testedEpic, "Epics not equal");
    }
    //НЕ ДОЛЖЕН ОБНОВЛЯТЬ САБТАСК
    @Test
    void shouldNotUpdateSubtask() {
        Subtask expectedSubtask = taskManager.getSubtask(6);
        taskManager.updateSubtask(null, "NEW");
        Subtask testedSubtask = taskManager.getSubtask(6);
        assertNotNull(testedSubtask, "Subtask not found");
        assertEquals(expectedSubtask, testedSubtask, "Subtasks not equal");
    }


    //УДАЛЕНИЕ ЗАДАЧ____________________________________________________________________________________________________
    //ДОЛЖЕН УДАЛИТЬ ТАСКУ С АЙДИ 1
    @Test
    void shouldDeleteTaskWithId1() {
        taskManager.deleteTask(1);
        Task expectedTask = taskManager.getTask(1);
        assertNull(expectedTask, "Task found");
    }
    //ДОЛЖЕН УДАЛИТЬ ЭПИК С АЙДИ 3
    @Test
    void shouldDeleteEpicWithId3() {
        taskManager.deleteEpic(3);
        Epic expectedEpic = taskManager.getEpic(3);
        assertNull(expectedEpic, "Epic was not deleted");
    }
    //ДОЛЖЕН УДАЛЯТЬ САБТАСКУ С АЙДИ 6
    @Test
    void shouldDeleteSubtaskWithId6() {
        taskManager.deleteSubtask(6);
        Subtask expectedSubtask = taskManager.getSubtask(6);
        assertNull(expectedSubtask, "Subtask was not deleted");
    }


    //УДАЛЕНИЕ ВСЕХ ЗАДАЧ_______________________________________________________________________________________________
    //ДОЛЖЕН УДАЛИТЬ ВСЕ ТАСКИ
    @Test
    void shouldDeleteAllTasks() {
        List<Task> beforeDeleteListOfTasks = taskManager.getTasks();
        int beforeDeleteExpectedSize = 2;
        int afterDeleteExpectedSize = 0;
        taskManager.deleteTasks();
        List<Task> afterDeleteListOfTasks = taskManager.getTasks();
        assertEquals(beforeDeleteExpectedSize, beforeDeleteListOfTasks.size(), "Wrong tasks size");
        assertEquals(afterDeleteExpectedSize, afterDeleteListOfTasks.size(), "Wrong tasks size");
    }
    //ДОЛЖЕН УДАЛИТЬ ВСЕ ЭПИКИ
    @Test
    void shouldDeleteAllEpics() {
        List<Epic> beforeDeleteListOfEpics = taskManager.getEpics();
        int beforeDeleteExpectedSize = 3;
        int afterDeleteExpectedSize = 0;
        taskManager.deleteEpics();
        List<Epic> afterDeleteListOfEpics = taskManager.getEpics();
        assertEquals(beforeDeleteExpectedSize, beforeDeleteListOfEpics.size(), "Wrong epics size");
        assertEquals(afterDeleteExpectedSize, afterDeleteListOfEpics.size(), "Wrong epics size");
    }
    //ДОЛЖЕН УДАЛИТЬ ВСЕ САБТАСКИ
    @Test
    void shouldDeleteAllSubtasks() {
        List<Subtask> beforeDeleteListOfSubtasks = taskManager.getSubtasks();
        int beforeDeleteExpectedSize = 4;
        int afterDeleteExpectedSize = 0;
        taskManager.deleteSubtasks();
        List<Subtask> afterDeleteListOfSubtasks = taskManager.getSubtasks();
        assertEquals(beforeDeleteExpectedSize, beforeDeleteListOfSubtasks.size(), "Wrong subtasks size");
        assertEquals(afterDeleteExpectedSize, afterDeleteListOfSubtasks.size(), "Wrong subtasks size");
    }


    //ВОЗВРАТ ИСТОРИИ___________________________________________________________________________________________________
    //ДОЛЖЕН ВЕРНУТЬ ИСТОРИЮ С РАЗМЕРОМ 2
    @Test
    void shouldReturnHistoryWithSize2() {
        taskManager.getTask(1);
        taskManager.getEpic(3);
        List<Task> testedListOfHistory = taskManager.getDefaultHistory();
        int expectedSize = 2;
        Epic expectedEpic = taskManager.getEpic(3);
        assertNotNull(testedListOfHistory, "History don't return");
        assertEquals(expectedSize, testedListOfHistory.size(), "Wrong history size");
        assertEquals(expectedEpic, testedListOfHistory.get(1), "Epics not equal");
    }
    //ДОЛЖЕН ВЕРНУТЬ ИСТОРИЮ С РАЗМЕРОМ 0
    @Test
    void shouldReturnHistoryWithSize0() {
        List<Task> testedListOfHistory = taskManager.getDefaultHistory();
        int expectedSize = 0;
        assertNotNull(testedListOfHistory, "History don't return");
        assertEquals(expectedSize, testedListOfHistory.size(), "Wrong history size");
    }
    //ДОЛЖЕН ВЕРНУТЬ ИСТОРИЮ ОТСОРТИРОВАННУЮ ПО ВРЕМЕНИ ПОСЛЕ ДОБАВЛЕНИЯ ТАСКИ
    @Test
    void shouldReturnPrioritizedTasksByStartTimeAfterAddNewTask() {
        List<Task> beforeAddNewTaskSortByPriorityTasks = taskManager.getPrioritizedTasks();
        int beforeAddNewTaskSize = 6;
        Subtask beforeAddNewTask = taskManager.getSubtask(9);
        Task beforeAddNewTaskFirstTask = beforeAddNewTaskSortByPriorityTasks.get(0);
        assertEquals(beforeAddNewTaskSize, beforeAddNewTaskSortByPriorityTasks.size(), "Size not equal");
        assertEquals(beforeAddNewTask, beforeAddNewTaskFirstTask, "Task not equal");
        Task expectedTask = new Task(
                "Test Task",
                "Test Task",
                "PT30M",
                "2023-07-21T12:00:00");
        taskManager.addTask(expectedTask);
        List<Task> afterAddNewTaskSortByPriorityTasks = taskManager.getPrioritizedTasks();
        Task afterAddNewTask = taskManager.getTask(10);
        Task afterAddNewTaskFirstTask = afterAddNewTaskSortByPriorityTasks.get(0);
        int afterAddNewTaskSize = 7;
        assertEquals(afterAddNewTaskSize, afterAddNewTaskSortByPriorityTasks.size(), "Size not equal");
        assertEquals(afterAddNewTask, afterAddNewTaskFirstTask, "Task not equal");
    }


    //ОБНОВЛЕНИЕ СТАРТОВ У ЭПИКОВ_______________________________________________________________________________________
    //ДОЛЖЕН ОБНОВИТЬ ВРЕМЯ СТАРТА ЭПИКА, ПОСЛЕ ДОБАВЛЕНИЯ НОВОЙ САБТАСКИ
    @Test
    void shouldUpdateStartTimeOfEpicAfterAddNewSubtask() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Subtask expectedSubtask = new Subtask(
                3,
                "Test Subtask",
                "Test Subtask",
                "PT300M",
                "2023-07-23T10:00:00");
        taskManager.addSubtask(expectedSubtask);
        assertEquals(expectedSubtask.getStartTime().format(dateTimeFormatter), taskManager.getEpic(3).getStartTime().format(dateTimeFormatter), "Start time of epic don't update");
    }
    //ДОЛЖЕН ОБНОВИТЬ ВРЕМЯ СТАРТА ЭПИКА, ПОСЛЕ ОБНОВЛЕНИЯ САБТАСКИ
    @Test
    void shouldUpdateStartTimeOfEpicAfterUpdateSubtask() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Subtask expectedSubtask = new Subtask(
                3,
                "Test Subtask",
                "Test Subtask",
                "PT30M",
                "2023-07-24T08:00:00");
        expectedSubtask.setId(6);
        taskManager.updateSubtask(expectedSubtask, "NEW");
        assertEquals(expectedSubtask.getStartTime().format(dateTimeFormatter), taskManager.getEpic(3).getStartTime().format(dateTimeFormatter), "Start time of epic don't update");
    }
    //ДОЛЖЕН ОБНОВИТЬ ВРЕМЯ СТАРТА ЭПИКА ПОСЛЕ УДАЛЕНИЯ САБТАСКИ
    @Test
    void shouldUpdateStartTimeOfEpicAfterDeleteSubtask() {
        Subtask removedSubtask = taskManager.getSubtask(6);
        Subtask expectedSubtask = taskManager.getSubtask(7);
        assertEquals(removedSubtask.getStartTime(),
                taskManager.getEpic(3).getStartTime(),
                "Start time of epic don't update first time");
        taskManager.deleteSubtask(6);
        assertEquals(expectedSubtask.getStartTime(),
                taskManager.getEpic(3).getStartTime(),
                "Start time of epic don't update second time");
    }
}

/*
    Не думал, что будет так душно с таким количеством тестов.
    Часть тестов по типу получения тасков с неверным ID я не добавлял, так как они проверяются в начале метода
    и просто прекращаются, если такой таски нет. Я спросил у куратора как быть с такими методами, мне сказали,
    что добавлять тест ради теста не нужно. Но если всё же нужно они у меня есть и я у себя сохранил,
    при необходимости я их просто добавлю.
* */