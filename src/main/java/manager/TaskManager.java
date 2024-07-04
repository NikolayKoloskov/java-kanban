package main.java.manager;


import main.java.tasks.EpicTask;
import main.java.tasks.SubTask;
import main.java.tasks.Task;

import java.util.List;

public interface TaskManager {
    Task getTask(int id);

    EpicTask getEpicTask(int id);

    SubTask getSubTask(int id);

    void createTask(Task task) throws ManagerSaveException;

    void createEpicTask(EpicTask epicTask) throws ManagerSaveException;

    void createSubTask(SubTask subTask) throws ManagerSaveException;

    List<SubTask> getAllSubTasksByEpicId(int epicId);

    void deleteTask(Task task) throws ManagerSaveException;

    void deleteEpicTask(EpicTask epicTask) throws ManagerSaveException;

    void deleteSubTask(SubTask subTask) throws ManagerSaveException;

    void deleteAllTasks() throws ManagerSaveException;

    void deleteAllEpicTasks() throws ManagerSaveException;

    //Метод удаляет вообще все подзадачи по этому ничего на вход не принимает
    void deleteAllSubTasks() throws ManagerSaveException;

    //Добавил метод для удаления подзадач по эпику
    //сначала удаляю все подзадачи из мапы подзадач - потом чищу эти подзадачи в эпике и обновляю статус у эпика
    void deleteAllSubTasksInEpic(int epicId) throws ManagerSaveException;

    void updateTask(Task task) throws ManagerSaveException;

    void updateEpicTask(EpicTask epicTask) throws ManagerSaveException;

    void updateSubTask(SubTask subTask) throws ManagerSaveException;

    List<Task> getAllTasks();

    List<EpicTask> getAllEpicTasks();

    List<SubTask> getAllSubTasks();

    List<Task> getHistory();

    void setTask(Task task);
}
