public class Main {

    public static void main(String[] args) {
        //Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.
        //Распечатайте списки эпиков, задач и подзадач через System.out.println(..).
        //Измените статусы созданных объектов, распечатайте их. Проверьте, что статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач.
        //И, наконец, попробуйте удалить одну из задач и один из эпиков.
        //Воспользуйтесь дебаггером среды разработки, чтобы понять логику работы программы и отладить её.3

        TaskManager manager = new TaskManager();
        manager.createTask("Задача 1", "Описание задачи 1");
        manager.createTask("Задача 2", "Описание задачи 2");
        manager.createEpicTask("Эпик 1", "Описание эпика 1");
        manager.createSubTask(3, "Подзадача 1", "Подзадача эпика 1");
        manager.createSubTask(3, "Подзадача 2", "Подзадача эпика 1");
        manager.createSubTask(3, "Подзадача 3", "Подзадача эпика 1");
        manager.createEpicTask("Эпик 2", "Описание эпика 2)");
        manager.createSubTask(7, "Подзадача 1", "Подзадача эпика 2");
        System.out.println("Созданные задачи:");
        System.out.println(manager.getTasks());
        System.out.println("созданные эпики:");
        System.out.println(manager.getEpicTasks());
        System.out.println("Созданные подзадачи:");
        System.out.println(manager.getSubTasks());

        manager.changeTaskStatus(1, Status.IN_PROGRESS);
        manager.changeTaskStatus(2, Status.DONE);
        manager.changeSubTaskStatus(4, Status.DONE);
        manager.changeSubTaskStatus(5, Status.IN_PROGRESS);
        manager.changeSubTaskStatus(6, Status.IN_PROGRESS);
        manager.changeSubTaskStatus(8, Status.DONE);
        System.out.println("Отредактированные задачи:");
        System.out.println(manager.getTasks());
        System.out.println("Отредактированные эпики:");
        System.out.println(manager.getEpicTasks());
        System.out.println("Отредактированные подзадачи:");
        System.out.println(manager.getSubTasks());

        manager.deleteTask(1);
        manager.deleteEpicTask(3);

        System.out.println("Итоговые задачи:");
        System.out.println(manager.getTasks());
        System.out.println("Итоговые эпики:");
        System.out.println(manager.getEpicTasks());
        System.out.println("Итоговые подзадачи:");
        System.out.println(manager.getSubTasks());
    }
}
