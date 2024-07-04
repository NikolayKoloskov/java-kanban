package test;

import main.java.manager.FileBackedTaskManager;
import main.java.manager.ManagerSaveException;
import main.java.tasks.EpicTask;
import main.java.tasks.Status;
import main.java.tasks.SubTask;
import main.java.tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class TestForBackedTaskManager {
    FileBackedTaskManager manager = new FileBackedTaskManager();
    File file = new File("./src/main/java/manager/StorageOfTasks.cvs");

    public TestForBackedTaskManager() throws ManagerSaveException {
    }

    @BeforeEach()
    public void setUp() throws ManagerSaveException {
        manager = new FileBackedTaskManager();

    }

    @Test
    public void checkCreateFile() throws ManagerSaveException {
        File file = new File("./src/main/java/manager/StorageOfTasks.cvs");
        file.delete();
        Assertions.assertFalse(file.exists());
        manager = new FileBackedTaskManager();
        Assertions.assertTrue(file.exists());
    }

    @Test
    public void checkLoadFromFile() throws ManagerSaveException, IOException {
        Task task1 = new Task("task1", "task1Description", Status.NEW);
        EpicTask task2 = new EpicTask("epicTask1", "epicTaskDescription");
        SubTask task3 = new SubTask(2, "subTask1", "subTask1Description", Status.NEW);
        SubTask task4 = new SubTask(2, "subTask2", "subTask2Description", Status.NEW);
        manager.createTask(task1);
        manager.createEpicTask(task2);
        manager.createSubTask(task3);
        manager.createSubTask(task4);
        FileBackedTaskManager manager2 = FileBackedTaskManager.loadFromFile(file);
        Assertions.assertEquals(task1, manager2.getTask(1));
        Assertions.assertEquals(task2, manager2.getEpicTask(2));
        Assertions.assertEquals(task3, manager2.getSubTask(3));
        Assertions.assertEquals(task4, manager2.getSubTask(4));
    }

    @Test
    public void checkHistory() throws ManagerSaveException, IOException {
        Task task1 = new Task("task1", "task1Description", Status.NEW);
        EpicTask task2 = new EpicTask("epicTask1", "epicTaskDescription");
        SubTask task3 = new SubTask(2, "subTask1", "subTask1Description", Status.NEW);
        SubTask task4 = new SubTask(2, "subTask2", "subTask2Description", Status.NEW);
        manager.createTask(task1);
        manager.createEpicTask(task2);
        manager.createSubTask(task3);
        manager.createSubTask(task4);
        FileBackedTaskManager manager2 = FileBackedTaskManager.loadFromFile(file);
        Assertions.assertEquals(manager.getHistory(), manager2.getHistory());
    }
}
