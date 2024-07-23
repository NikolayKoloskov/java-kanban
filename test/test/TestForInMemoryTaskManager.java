package test;

import main.java.manager.InMemoryTaskManager;
import main.java.manager.Managers;
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

public class TestForInMemoryTaskManager {
    Managers managers = new Managers();
    InMemoryTaskManager manager = (InMemoryTaskManager) managers.getDefault();
    String taskName = "Имя Задачи";
    String subTaskName = "Имя SubTask";
    String epicTaskName = "Имя EpicTask";
    String description = "Описание Задачи";


    @BeforeEach
    void init() {
        Managers managers = new Managers();
        InMemoryTaskManager manager = (InMemoryTaskManager) managers.getDefault();
        taskName = "Имя Задачи";
        subTaskName = "Имя SubTask";
        epicTaskName = "Имя EpicTask";
        description = "Описание Задачи";
    }

    @Test
    public void checkGetTask() {
        Task task = new Task(taskName, description, Status.NEW);
        Task taskToCheck = null;
        Assertions.assertNull(manager.getTask(0));
        manager.createTask(task);
        taskToCheck = manager.getTask(task.getId());
        Assertions.assertEquals(task, taskToCheck);
    }

    @Test
    public void checkGetEpicTask() {
        EpicTask epicTask = new EpicTask(epicTaskName, description);
        EpicTask taskToCheck = null;
        Assertions.assertNull(manager.getEpicTask(0));
        manager.createEpicTask(epicTask);
        taskToCheck = manager.getEpicTask(epicTask.getId());
        Assertions.assertEquals(epicTask, taskToCheck);
    }

    @Test
    public void checkTaskById() {
        Task task = new Task(taskName, description, Status.NEW);
        Task task2 = new Task(taskName, description, Status.NEW);
        manager.createTask(task);
        manager.createTask(task2);
        Assertions.assertNotEquals(manager.getTask(1), manager.getTask(2));
    }

    @Test
    public void checkEqualsTaskById() {
        Task task = new Task(taskName, description, Status.NEW);
        manager.createTask(task);
        Assertions.assertEquals(task, manager.getTask(1));
    }

    @Test
    public void validCreationOfObjects() {
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
    public void addSubtask() {
        EpicTask epicTask1 = new EpicTask("Эпик 1", "Описание эпика 1");
        manager.createEpicTask(epicTask1);
        SubTask subTask1 = new SubTask(1, "Подзадача 1", "Подзадача эпика 1", Status.NEW);
        manager.createSubTask(subTask1);
        Assertions.assertNotNull(subTask1, "Подзадача не добавлена");
    }

    @Test
    public void getSubtask() {
        EpicTask epicTask1 = new EpicTask("Эпик 1", "Описание эпика 1");
        manager.createEpicTask(epicTask1);
        SubTask subTask1 = new SubTask(1, "Подзадача 1", "Подзадача эпика 1", Status.NEW);
        manager.createSubTask(subTask1);
        SubTask subTask2 = manager.getSubTask(2);
        Assertions.assertEquals(subTask1, subTask2);
    }

    @Test
    public void removeSubtask() {
        EpicTask epicTask1 = new EpicTask("Эпик 1", "Описание эпика 1");
        manager.createEpicTask(epicTask1);
        SubTask subTask1 = new SubTask(1, "Подзадача 1", "Подзадача эпика 1", Status.NEW);
        manager.createSubTask(subTask1);
        manager.deleteSubTask(subTask1);
        Assertions.assertNull(manager.getSubTask(2), "Подзадача не удалена");
        epicTask1.removeSubtask(null);
        Assertions.assertEquals(epicTask1, manager.getEpicTask(1));
    }

    @Test
    public void removeAllSubtasks() {
        EpicTask epicTask1 = new EpicTask("Эпик 1", "Описание эпика 1");
        manager.createEpicTask(epicTask1);
        SubTask subTask1 = new SubTask(1, "Подзадача 1", "Подзадача эпика 1", Status.NEW);
        manager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask(1, "Подзадача 2", "Подзадача эпика 2", Status.NEW);
        manager.createSubTask(subTask2);
        manager.deleteAllSubTasks();
        Assertions.assertNull(manager.getSubTask(2), "Подзадача не удалена");
        Assertions.assertNull(manager.getSubTask(3), "Подзадача не удалена");
    }

    @Test
    public void getSubTasks() {
        EpicTask epicTask1 = new EpicTask("Эпик 1", "Описание эпика 1");
        manager.createEpicTask(epicTask1);
        SubTask subTask1 = new SubTask(1, "Подзадача 1", "Подзадача эпика 1", Status.NEW);
        manager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask(1, "Подзадача 2", "Подзадача эпика 2", Status.NEW);
        manager.createSubTask(subTask2);
        List<SubTask> subTasks = new ArrayList<>();
        subTasks.add(subTask1);
        subTasks.add(subTask2);
        List<SubTask> actualSubTasks = manager.getAllSubTasks();
        Assertions.assertArrayEquals(actualSubTasks.toArray(), subTasks.toArray());
    }

    @Test
    public void searchTestByIDInMemoryTaskManager() {
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
    public void checkUpdateTask() {
        Task task = new Task("task1", "task1", Status.NEW);
        manager.createTask(task);
        task.update("updated", "updated", Status.IN_PROGRESS);
        Assertions.assertEquals(task, manager.getTask(1));
        task.setName("updated1");
        Assertions.assertEquals(task.getName(), manager.getTask(1).getName());
    }

    @Test
    public void checkDescriptionInTask() {
        Task task = new Task("task1", "task1", Status.NEW);
        manager.createTask(task);
        task.setDescription("updated1");
        Assertions.assertEquals("updated1", manager.getTask(1).getDescription());
    }

    @Test
    public void checkStatusInTask() {
        Task task = new Task("task1", "task1", Status.NEW);
        manager.createTask(task);
        task.setStatus(Status.IN_PROGRESS);
        Assertions.assertEquals(Status.IN_PROGRESS, manager.getTask(1).getStatus());
    }

    @Test
    public void checkStatusInEpicNEW() {
        EpicTask task = new EpicTask("task1", "task1");
        SubTask subTask = new SubTask(1, "subTask1", "subTask1", Status.NEW);
        SubTask subTask2 = new SubTask(1, "subTask2", "subTask2", Status.NEW);
        manager.createEpicTask(task);
        manager.createSubTask(subTask);
        manager.createSubTask(subTask2);
        Assertions.assertEquals(Status.NEW, manager.getEpicTask(1).getStatus());
    }

    @Test
    public void checkStatusInEpicDONE() {
        EpicTask task = new EpicTask("task1", "task1");
        SubTask subTask = new SubTask(1, "subTask1", "subTask1", Status.DONE);
        SubTask subTask2 = new SubTask(1, "subTask2", "subTask2", Status.DONE);
        manager.createEpicTask(task);
        manager.createSubTask(subTask);
        manager.createSubTask(subTask2);
        Assertions.assertEquals(Status.DONE, manager.getEpicTask(1).getStatus());
    }

    @Test
    public void checkStatusInEpicDoneAndNew() {
        EpicTask task = new EpicTask("task1", "task1");
        SubTask subTask = new SubTask(1, "subTask1", "subTask1", Status.NEW);
        SubTask subTask2 = new SubTask(1, "subTask2", "subTask2", Status.DONE);
        manager.createEpicTask(task);
        manager.createSubTask(subTask);
        manager.createSubTask(subTask2);
        Assertions.assertEquals(Status.IN_PROGRESS, manager.getEpicTask(1).getStatus());
    }

    @Test
    public void checkStatusInEpicInProgress() {
        EpicTask task = new EpicTask("task1", "task1");
        SubTask subTask = new SubTask(1, "subTask1", "subTask1", Status.IN_PROGRESS);
        SubTask subTask2 = new SubTask(1, "subTask2", "subTask2", Status.IN_PROGRESS);
        manager.createEpicTask(task);
        manager.createSubTask(subTask);
        manager.createSubTask(subTask2);
        Assertions.assertEquals(Status.IN_PROGRESS, manager.getEpicTask(1).getStatus());
    }

    @Test
    public void getAllSubTasksByEpicIdTest() {
        List<SubTask> subTasks = new ArrayList<>();
        EpicTask task = new EpicTask("task1", "task1");
        SubTask subTask = new SubTask(1, "subTask1", "subTask1", Status.IN_PROGRESS);
        SubTask subTask2 = new SubTask(1, "subTask2", "subTask2", Status.IN_PROGRESS);
        subTasks.add(subTask);
        subTasks.add(subTask2);
        manager.createEpicTask(task);
        manager.createSubTask(subTask);
        manager.createSubTask(subTask2);
        Assertions.assertEquals(subTasks, manager.getAllSubTasksByEpicId(1));
        List<SubTask> actualSubTasks = new ArrayList<>();
        EpicTask task2 = new EpicTask("task2", "task2");
        manager.createEpicTask(task2);
        Assertions.assertEquals(actualSubTasks, manager.getAllSubTasksByEpicId(4));
    }

    @Test
    public void getPrioritizedTasksTest() {
        List<Task> tasks = new ArrayList<>();
        Task task = new Task("task1", "task1", Status.NEW, LocalDateTime.of(2024, 1, 1, 1, 0), Duration.ofMinutes(60));
        Task task2 = new Task("task2", "task2", Status.NEW, LocalDateTime.of(2024, 1, 2, 1, 0), Duration.ofMinutes(60));
        manager.createTask(task);
        manager.createTask(task2);
        tasks.add(task2);
        tasks.add(task);
        Assertions.assertEquals(tasks, manager.getPrioritizedTasks());
    }

    @Test
    public void deleteTaskTest() {
        Task task = new Task("task1", "task1", Status.NEW, LocalDateTime.of(2024, 1, 1, 1, 0), Duration.ofMinutes(60));
        manager.createTask(task);
        Assertions.assertEquals(task, manager.getTask(1));
        manager.deleteTask(task);
        Assertions.assertNull(manager.getTask(1));
        Assertions.assertEquals(new ArrayList<Task>(), manager.getPrioritizedTasks());
    }

    @Test
    public void deleteEpicTaskTest() {
        EpicTask task = new EpicTask("task1", "task1");
        manager.createEpicTask(task);
        Assertions.assertEquals(task, manager.getEpicTask(1));
        manager.deleteEpicTask(task);
        Assertions.assertNull(manager.getEpicTask(1));
        Assertions.assertEquals(new ArrayList<EpicTask>(), manager.getAllEpicTasks());
    }

    @Test
    public void deleteAllTasksTest() {
        Task task = new Task("task1", "task1", Status.NEW, LocalDateTime.of(2024, 1, 1, 1, 0), Duration.ofMinutes(60));
        Task task2 = new Task("task1", "task1", Status.NEW, LocalDateTime.of(2024, 1, 1, 3, 0), Duration.ofMinutes(60));
        manager.createTask(task);
        manager.createTask(task2);
        manager.deleteAllTasks();
        Assertions.assertEquals(new ArrayList<Task>(), manager.getPrioritizedTasks());
        Assertions.assertEquals(new ArrayList<Task>(), manager.getAllTasks());
    }

    @Test
    public void deleteAllEpicTasksTest() {
        EpicTask task = new EpicTask("task1", "task1");
        manager.createEpicTask(task);
        EpicTask task2 = new EpicTask("task1", "task1");
        manager.createEpicTask(task2);
        manager.deleteAllEpicTasks();
        Assertions.assertEquals(new ArrayList<EpicTask>(), manager.getAllEpicTasks());
    }

    @Test
    public void deleteAllSubTasksInEpicTest() {
        EpicTask task = new EpicTask("task1", "task1");
        SubTask subTask = new SubTask(1, "subTask1", "subTask1", Status.NEW);
        SubTask subTask2 = new SubTask(1, "subTask2", "subTask2", Status.NEW);
        List<SubTask> subTasks = List.of(subTask, subTask2);
        manager.createEpicTask(task);
        manager.createSubTask(subTask);
        manager.createSubTask(subTask2);
        Assertions.assertEquals(subTasks, manager.getAllSubTasksByEpicId(1));
        manager.deleteAllSubTasksInEpic(1);
        Assertions.assertEquals(new ArrayList<SubTask>(), manager.getAllSubTasksByEpicId(1));
    }

    @Test
    public void updateTaskTest() {
        Task task = new Task("task1", "task1", Status.NEW, LocalDateTime.of(2024, 1, 1, 1, 0), Duration.ofMinutes(60));
        manager.createTask(task);
        Assertions.assertEquals(task, manager.getTask(1));
        Assertions.assertEquals(List.of(task), manager.getPrioritizedTasks());
        Task task2 = new Task("task2", "task2", Status.NEW, LocalDateTime.of(2024, 1, 2, 1, 0), Duration.ofMinutes(60));
        task2.setId(1);
        manager.updateTask(task2);
        Assertions.assertEquals(task2, manager.getTask(1));
        Assertions.assertEquals(List.of(task2), manager.getPrioritizedTasks());

    }
}