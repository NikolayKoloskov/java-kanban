import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

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
        Assertions.assertNull(manager.getSubTask(2),  "Подзадача не удалена");
    }

    @Test
    void removeAllSubtasks() {
        EpicTask epicTask1 = new EpicTask("Эпик 1", "Описание эпика 1");
        manager.createEpicTask(epicTask1);
        SubTask subTask1 = new SubTask(1, "Подзадача 1", "Подзадача эпика 1", Status.NEW);
        manager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask(1, "Подзадача 2",  "Подзадача эпика 2", Status.NEW);
        manager.createSubTask(subTask2);
        manager.deleteAllSubTasks();
        Assertions.assertNull(manager.getSubTask(2),   "Подзадача не удалена");
        Assertions.assertNull(manager.getSubTask(3),    "Подзадача не удалена");
    }

    @Test
    void getSubTasks() {
        EpicTask epicTask1 = new EpicTask("Эпик 1", "Описание эпика 1");
        manager.createEpicTask(epicTask1);
        SubTask subTask1 = new SubTask(1, "Подзадача 1", "Подзадача эпика 1", Status.NEW);
        manager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask(1, "Подзадача 2",  "Подзадача эпика 2", Status.NEW);
        manager.createSubTask(subTask2);
        List<SubTask> subTasks  = new ArrayList<>();
        subTasks.add(subTask1);
        subTasks.add(subTask2);
        List<SubTask> actualSubTasks = manager.getAllSubTasks();
        assertArrayEquals(actualSubTasks.toArray(), subTasks.toArray());
    }

    @Test
    void failToAddEpicTask() {
        //пока совсем не понимаю как проверить что нельзя вставить эпик как подзадачу - так как это даже не компилируется....
        //тоже самое с Subtask->epicTast
//        EpicTask epicTask = new EpicTask("Эпик 1", "Описание эпика 1");
//        manager.createEpicTask(epicTask);
//        Assertions.assertFalse(manager.createSubTask((SubTask) epicTask)); //тут ошибка типа данных
    }
}