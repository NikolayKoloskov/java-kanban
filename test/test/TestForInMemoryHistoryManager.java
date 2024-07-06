package test;


import main.java.manager.InMemoryHistoryManager;
import main.java.manager.InMemoryTaskManager;
import main.java.manager.Managers;
import main.java.tasks.EpicTask;
import main.java.tasks.Status;
import main.java.tasks.SubTask;
import main.java.tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    public void addAndDeleteEpicTaskInMemoryHistoryManager() throws ManagerSaveException {
        InMemoryHistoryManager historyManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
        EpicTask task = new EpicTask("epic1", "task1");
        EpicTask task2 = new EpicTask("epic2", "task2");
        EpicTask task3 = new EpicTask("epic3", "task3");
        manager.createEpicTask(task);
        manager.createEpicTask(task2);
        manager.createEpicTask(task3);
        System.out.println(task.getClass());

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
    public void objectToCompareInMemoryHistoryManager() throws ManagerSaveException {
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
    public void addAndDeleteInMemoryHistoryManager() throws ManagerSaveException {
        InMemoryHistoryManager historyManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
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