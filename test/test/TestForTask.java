package test;

import main.java.manager.*;
import main.java.tasks.EpicTask;
import main.java.tasks.Status;
import main.java.tasks.SubTask;
import main.java.tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThrows;


public class TestForTask {

    Managers managers = new Managers();
    InMemoryTaskManager manager = (InMemoryTaskManager) managers.getDefault();

    @BeforeEach
    void init() {
        Managers managers = new Managers();
        InMemoryTaskManager manager = (InMemoryTaskManager) managers.getDefault();
    }

    @Test
    void checkTaskById() {
        Task task = new Task("task1", "task1", Status.NEW);
        Task task2 = new Task("task1", "task1", Status.NEW);
        manager.createTask(task);
        manager.createTask(task2);
        Assertions.assertNotEquals(manager.getTask(1), manager.getTask(2));
    }

    @Test
    void checkEqualsTaskById() {
        Task task = new Task("task1", "task1", Status.NEW);
        manager.createTask(task);
        Assertions.assertEquals(task, manager.getTask(1));
    }

    @Test
    void validCreationOfObjects() {
        Task task = new Task("task1", "task1", Status.NEW);
        EpicTask epicTask = new EpicTask("epicTask1", "epicTask1");
        SubTask subTask = new SubTask(2, "subTask1", "subTask1", Status.NEW);
        manager.createTask(task);
        manager.createEpicTask(epicTask);
        manager.createSubTask(subTask);
        Task task2 = new Task("task1", "task1", Status.NEW);
        Assertions.assertInstanceOf(Task.class, manager.getTask(1), "Обьекты не совпадают");
        Assertions.assertInstanceOf(EpicTask.class, manager.getEpicTask(2), "Обьекты не совпадают");
        Assertions.assertInstanceOf(SubTask.class, manager.getSubTask(3), "Обьекты не совпадают");
    }

    @Test
    void searchTestByIDInMemoryTaskManager() {
        Task task = new Task("task1", "task1", Status.NEW);
        EpicTask epicTask = new EpicTask("epicTask1", "epicTask1");
        SubTask subTask = new SubTask(2, "subTask1", "subTask1", Status.NEW);
        manager.createTask(task);
        manager.createEpicTask(epicTask);
        manager.createSubTask(subTask);
        Assertions.assertEquals(manager.getTask(1), task);
        Assertions.assertEquals(manager.getEpicTask(2), epicTask);
        Assertions.assertEquals(manager.getSubTask(3), subTask);
    }

    @Test
    void objectToCompareInMemoryHistoryManager() {
        InMemoryHistoryManager historyManager = (InMemoryHistoryManager) managers.getDefaultHistory();
        Task task = new Task("task1", "task1", Status.NEW);
        EpicTask epicTask = new EpicTask("epicTask1", "epicTask1");
        SubTask subTask = new SubTask(2, "subTask1", "subTask1", Status.NEW);
        manager.createTask(task);
        manager.createEpicTask(epicTask);
        manager.createSubTask(subTask);
        historyManager.add(task);
        historyManager.add(epicTask);
        List<Task> history = new ArrayList<>();
        history.add(task);
        history.add(epicTask);
        List<Task> historyToCompare = historyManager.getHistory();
        Assertions.assertEquals(history, historyToCompare);
        history.add(subTask);
        historyManager.add(subTask);
        historyToCompare = historyManager.getHistory();
        Assertions.assertEquals(history, historyToCompare);
    }

    @Test
    void addAndDeleteInMemoryHistoryManager() {
        InMemoryHistoryManager historyManager = (InMemoryHistoryManager) managers.getDefaultHistory();
        Task task = new Task("task1", "task1", Status.NEW);
        Task task2 = new Task("task2", "task2", Status.NEW);
        Task task3 = new Task("task3", "task3", Status.NEW);
        manager.createTask(task);
        manager.createTask(task2);
        manager.createTask(task3);

        List<Task> history = new ArrayList<>();
        history.add(task);
        history.add(task2);
        history.add(task3);
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(task3);
        List<Task> historyToCompare = historyManager.getHistory();
        Assertions.assertEquals(history, historyToCompare);

        historyManager.remove(1);
        history.remove(task);
        historyToCompare = historyManager.getHistory();
        Assertions.assertEquals(history, historyToCompare);
    }

    @Test
    public void canselCreationOfTask() {
        Task task = new Task("task1", "task1", Status.NEW, LocalDateTime.of(2024, 1, 1, 1, 0), Duration.ofMinutes(60));
        manager.createTask(task);
        Assertions.assertEquals(task, manager.getTask(1));
        Task task2 = new Task("task2", "task2", Status.NEW, LocalDateTime.of(2024, 1, 1, 1, 0), Duration.ofMinutes(30));
        assertThrows(ManagerSortedSaveException.class, () -> manager.createTask(task2));
    }
}
