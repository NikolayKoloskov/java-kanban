package main.java;

import main.java.manager.FileBackedTaskManager;
import main.java.manager.ManagerSaveException;
import main.java.tasks.Status;
import main.java.tasks.Task;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, ManagerSaveException {
        FileBackedTaskManager taskManager = new FileBackedTaskManager();
        taskManager.createTask(new Task("1", "desc", Status.NEW));

    }
}
