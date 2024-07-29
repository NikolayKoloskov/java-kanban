package main.java;

import main.java.manager.FileBackedTaskManager;
import main.java.manager.InMemoryTaskManager;
import main.java.manager.ManagerSaveException;
import main.java.tasks.EpicTask;
import main.java.tasks.Status;
import main.java.tasks.SubTask;
import main.java.tasks.Task;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException, ManagerSaveException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        LocalDateTime time = LocalDateTime.of(2024, 7, 22, 19, 00);
        LocalDateTime time2 = LocalDateTime.of(2024, 7, 22, 20, 00);

        taskManager.createTask(new Task("Task 1", "task 2 desc", Status.NEW, time, Duration.ofMinutes(30)));

        taskManager.createTask(new Task("Task 2", "task 3 desc", Status.NEW, time2, Duration.ofMinutes(15)));
        taskManager.getPrioritizedTasks().forEach(System.out::println);
        Task task = taskManager.getTask(1);
        task.setDuration(Duration.ofMinutes(60));
        taskManager.updateTask(task);

        taskManager.getPrioritizedTasks().forEach(System.out::println);


        FileBackedTaskManager manager = new FileBackedTaskManager(Paths.get(""), Paths.get(""));
        manager.createTask(new Task("name1", "desc1", Status.NEW));
        manager.createTask(new Task("name2", "desc2", Status.IN_PROGRESS));
        manager.createTask(new Task("name3", "desc3", Status.NEW, time, Duration.ofMinutes(60)));
        System.out.println("____________________________________________________");
        manager.getPrioritizedTasks().forEach(System.out::println);
        System.out.println("____________________________________________________");

        manager.createEpicTask(new EpicTask("epic2", "epic 2 desc"));
        manager.createSubTask(new SubTask(4, "sub3", "sub3 desc",
                Status.NEW, time, Duration.ofMinutes(20)));
        manager.createSubTask(new SubTask(4, "sub4", "sub4 desc", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30)));
        System.out.println("____________________________________________________");
        manager.getPrioritizedTasks().forEach(System.out::println);


    }
}
