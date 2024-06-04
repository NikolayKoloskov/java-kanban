import java.util.List;

public interface TaskManager {
    Task getTask(int id);

    EpicTask getEpicTask(int id);

    SubTask getSubTask(int id);

    void createTask(Task task);

    void createEpicTask(EpicTask epicTask);

    void createSubTask(SubTask subTask);

    List<SubTask> getAllSubTasksByEpicId(int epicId);

    void deleteTask(Task task);

    void deleteEpicTask(EpicTask epicTask);

    void deleteSubTask(SubTask subTask);

    void deleteAllTasks();

    void deleteAllEpicTasks();

    //Метод удаляет вообще все подзадачи по этому ничего на вход не принимает
    void deleteAllSubTasks();

    //Добавил метод для удаления подзадач по эпику
    //сначала удаляю все подзадачи из мапы подзадач - потом чищу эти подзадачи в эпике и обновляю статус у эпика
    void deleteAllSubTasksInEpic(int epicId);

    void updateTask(Task task);

    void updateEpicTask(EpicTask epicTask);

    void updateSubTask(SubTask subTask);

    List<Task> getAllTasks();

    List<EpicTask> getAllEpicTasks();

    List<SubTask> getAllSubTasks();

    List<Task> getHistory();
}
