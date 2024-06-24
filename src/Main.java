import Manager.InMemoryHistoryManager;
import Manager.InMemoryTaskManager;
import Manager.Managers;
import Tasks.EpicTask;
import Tasks.Status;
import Tasks.SubTask;
import Tasks.Task;

public class Main {

    public static void main(String[] args) {
        //Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.
        //Распечатайте списки эпиков, задач и подзадач через System.out.println(..).
        //Измените статусы созданных объектов, распечатайте их. Проверьте, что статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач.
        //И, наконец, попробуйте удалить одну из задач и один из эпиков.
        //Воспользуйтесь дебаггером среды разработки, чтобы понять логику работы программы и отладить её.3

        Managers managers  = new Managers();
        InMemoryTaskManager manager = (InMemoryTaskManager) managers.getDefault();
        InMemoryHistoryManager history =(InMemoryHistoryManager) managers.getDefaultHistory();

        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW);
        manager.createTask(task1);
        manager.createTask(task2);

        EpicTask epicTask1 = new EpicTask("Эпик 1", "Описание эпика 1");
        manager.createEpicTask(epicTask1);
        SubTask subTask1 = new SubTask(3, "Подзадача 1", "Подзадача эпика 1", Status.NEW);
        manager.createSubTask(subTask1);
        SubTask subTask2 = new SubTask(3, "Подзадача 2", "Подзадача эпика 1", Status.NEW);
        manager.createSubTask(subTask2);
        SubTask subTask3 = new SubTask(3, "Подзадача 3", "Подзадача эпика 1", Status.NEW);
        manager.createSubTask(subTask3);
        EpicTask epicTask2 = new EpicTask("Эпик 2", "Описание эпика 2");
        manager.createEpicTask(epicTask2);


            manager.getTask(1);
            manager.getEpicTask(3);
            manager.getTask(1);
            manager.getSubTask(4);
        manager.getSubTask(6);
        manager.getSubTask(5);
        manager.getTask(2);
        manager.getEpicTask(7);
        System.out.println(manager.getHistory());
        System.out.println("_______________________________________________________________");
        manager.deleteTask(task1);
        System.out.println(manager.getHistory());

        System.out.println("_______________________________________________________________");
        manager.deleteEpicTask(epicTask1);
        System.out.println(manager.getHistory());
    }
}
