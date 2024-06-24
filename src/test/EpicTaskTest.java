package test;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.EpicTask;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTaskTest {
    Managers managers = new Managers();
    InMemoryTaskManager manager = (InMemoryTaskManager) managers.getDefault();

    @BeforeEach
    void init() {
        Managers managers = new Managers();
        InMemoryTaskManager manager = (InMemoryTaskManager) managers.getDefault();
    }

    @Test
    void addSubtask() {
        EpicTask epicTask1 = new EpicTask("Эпик 1", "Описание эпика 1");
        manager.createEpicTask(epicTask1);
        SubTask subTask1 = new SubTask(1, "Подзадача 1", "Подзадача эпика 1", Status.NEW);
        manager.createSubTask(subTask1);
        Assertions.assertNotNull(subTask1, "Подзадача не добавлена");
    }

    @Test
    void removeSubtask() {
        EpicTask epicTask1 = new EpicTask("Эпик 1", "Описание эпика 1");
        manager.createEpicTask(epicTask1);
        SubTask subTask1 = new SubTask(1, "Подзадача 1", "Подзадача эпика 1", Status.NEW);
        manager.createSubTask(subTask1);
        manager.deleteSubTask(subTask1);
        Assertions.assertNull(manager.getSubTask(2), "Подзадача не удалена");
    }

    @Test
    void removeAllSubtasks() {
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
    void getSubTasks() {
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
        assertArrayEquals(actualSubTasks.toArray(), subTasks.toArray());
    }

    @Test
    void failToAddEpicTask() {
        //пока совсем не понимаю как проверить что нельзя вставить эпик как подзадачу - так как это даже не компилируется....
        //тоже самое с Subtask->epicTast
//        Tasks.EpicTask epicTask = new Tasks.EpicTask("Эпик 1", "Описание эпика 1");
//        manager.createEpicTask(epicTask);
//        Assertions.assertFalse(manager.createSubTask((Tasks.SubTask) epicTask)); //тут ошибка типа данных
    }

    @Test
    void addAndDeleteEpicTaskInMemoryHistoryManager() {
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
        assertEquals(history, historyToCompare);

        historyManager.remove(1);
        history.remove(task);
        historyToCompare = historyManager.getHistory();
        assertEquals(history, historyToCompare);
    }
}