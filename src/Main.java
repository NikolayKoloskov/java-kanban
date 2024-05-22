public class Main {

    public static void main(String[] args) {
        //Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.
        //Распечатайте списки эпиков, задач и подзадач через System.out.println(..).
        //Измените статусы созданных объектов, распечатайте их. Проверьте, что статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач.
        //И, наконец, попробуйте удалить одну из задач и один из эпиков.
        //Воспользуйтесь дебаггером среды разработки, чтобы понять логику работы программы и отладить её.3

        TaskManager manager = new TaskManager();
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
        SubTask subTask4 = new SubTask(7, "Подзадача 1", "Подзадача эпика 2", Status.NEW);
        manager.createSubTask(subTask4);

        System.out.println("Созданные задачи:");
        System.out.println(manager.getAllTasks());
        System.out.println("созданные эпики:");
        System.out.println(manager.getAllEpicTasks());
        System.out.println("Созданные подзадачи:");
        System.out.println(manager.getAllSubTasks());

        task1.update("Задача 1-1", "Описание задачи 1-1", Status.IN_PROGRESS);
        task2.update("Задача 2-1", "Описание задачи 2-1", Status.DONE);
        manager.updateTask(task1);
        manager.updateTask(task2);
        System.out.println("Обновленные задачи:");
        System.out.println(manager.getAllTasks());

        subTask1.update("Подзадача 1-1", "Подзадача эпика 1-1", Status.IN_PROGRESS);
        subTask2.update("Подзадача 2-1", "Подзадача эпика 1-1", Status.DONE);
        subTask3.update("Подзадача 3-1", "Подзадача эпика 1-1", Status.IN_PROGRESS);
        subTask4.update("Подзадача 1-1", "Подзадача эпика 2-1", Status.DONE);

        manager.updateSubTask(subTask1);
        manager.updateSubTask(subTask2);
        manager.updateSubTask(subTask3);
        manager.updateSubTask(subTask4);


        System.out.println("Отредактированные задачи:");
        System.out.println(manager.getAllTasks());
        System.out.println("Отредактированные эпики:");
        System.out.println(manager.getAllEpicTasks());
        System.out.println("Отредактированные подзадачи:");
        System.out.println(manager.getAllSubTasks());

        manager.deleteTask(task1);
        manager.deleteEpicTask(epicTask2);
        manager.deleteSubTask(subTask3);

        System.out.println("Итоговые задачи:");
        System.out.println(manager.getAllTasks());
        System.out.println("Итоговые эпики:");
        System.out.println(manager.getAllEpicTasks());
        System.out.println("Итоговые подзадачи:");
        System.out.println(manager.getAllSubTasks());

        manager.deleteAllSubTasks();
        System.out.println("После удаления подзадач:");
        System.out.println(manager.getAllEpicTasks());
        System.out.println(manager.getAllSubTasks());
    }
}
