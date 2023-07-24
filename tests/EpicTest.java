import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    TaskManager testManager;
    @BeforeEach
    public void createTaskManager() {
        testManager = Managers.getDefault();
    }

    @Test
    public void createEpicWithEmptySubtasksList() {
        Epic epic = new Epic("Epic #1", "Epic1 description");
        int epicId = testManager.addEpic(epic);
        assertEquals(TaskStatus.NEW, testManager.getEpic(epicId).getStatus());
    }

    @Test
    public void createEpicWithSubtasksListWithStatusNew() {
        Epic epic = new Epic("Epic #1", "Epic1 description");
        int epicId = testManager.addEpic(epic);
        Subtask subtask = new Subtask("Subtask #1-1", "Subtask1 description", epicId);
        int subtaskId = testManager.addSubtask(subtask);
        assertEquals(TaskStatus.NEW, testManager.getEpic(epicId).getStatus());
    }

    @Test
    public void createEpicWithSubtasksListWithStatusDone() {
        Epic epic = new Epic("Epic #1", "Epic1 description");
        int epicId = testManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Subtask #1-1", "Subtask1 description", epicId);
        int subtaskId1 = testManager.addSubtask(subtask1);
        testManager.updateSubtask(subtask1, "DONE");
        assertEquals(TaskStatus.DONE, testManager.getEpic(epicId).getStatus());
    }

    @Test
    public void createEpicWithSubtasksListWithStatusNewAndDone() {
        Epic epic = new Epic("Epic #1", "Epic1 description");
        int epicId = testManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Subtask #1-1", "Subtask1 description", epicId);
        int subtaskId1 = testManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask #2-1", "Subtask2 description", epicId);
        int subtaskId2 = testManager.addSubtask(subtask2);
        testManager.updateSubtask(subtask1, "DONE");
        assertEquals(TaskStatus.IN_PROGRESS, testManager.getEpic(epicId).getStatus());
    }

    @Test
    public void createEpicWithSubtasksListWithStatusInProgress() {
        Epic epic = new Epic("Epic #1", "Epic1 description");
        int epicId = testManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Subtask #1-1", "Subtask1 description", epicId);
        int subtaskId1 = testManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask #2-1", "Subtask2 description", epicId);
        int subtaskId2 = testManager.addSubtask(subtask2);
        testManager.updateSubtask(subtask1, "IN_PROGRESS");
        assertEquals(TaskStatus.IN_PROGRESS, testManager.getEpic(epicId).getStatus());
    }
}