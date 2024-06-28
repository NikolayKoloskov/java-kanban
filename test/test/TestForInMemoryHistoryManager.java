package test;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import tasks.EpicTask;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class TestForInMemoryHistoryManager {
    Managers managers = new Managers();
    InMemoryTaskManager manager = (InMemoryTaskManager) managers.getDefault();


    @BeforeEach
    void init() {
        Managers managers = new Managers();
        InMemoryTaskManager manager = (InMemoryTaskManager) managers.getDefault();

    }

    @Test
    public void addAndDeleteEpicTaskInMemoryHistoryManager() {
        InMemoryHistoryManager historyManager = (InMemoryHistoryManager) managers.getDefaultHistory();
        EpicTask task = new EpicTask("epic1", "task1");
        EpicTask task2 = new EpicTask("epic2", "task2");
        EpicTask task3 = new EpicTask("epic3", "task3");
        manager.createEpicTask(task);
        manager.createEpicTask(task2);
        manager.createEpicTask(task3);

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
    public void objectToCompareInMemoryHistoryManager() {
        InMemoryHistoryManager historyManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
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
    public void addAndDeleteInMemoryHistoryManager() {
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
}