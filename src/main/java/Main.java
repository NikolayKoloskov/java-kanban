package main.java;

import main.java.manager.FileBackedTaskManager;
import main.java.manager.ManagerSaveException;
import main.java.tasks.Status;
import main.java.tasks.Task;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException, ManagerSaveException {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(Paths.get(""), Paths.get(""));
        taskManager.createTask(new Task("1", "desc", Status.NEW));

    }
}
