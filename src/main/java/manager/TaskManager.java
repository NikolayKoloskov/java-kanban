package main.java.manager;


import main.java.tasks.EpicTask;
import main.java.tasks.SubTask;
import main.java.tasks.Task;

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

    void deleteAllSubTasks();

    void deleteAllSubTasksInEpic(int epicId);

    void updateTask(Task task) throws ManagerSaveException;

    void updateEpicTask(EpicTask epicTask);

    void updateSubTask(SubTask subTask);

    List<Task> getAllTasks();

    List<EpicTask> getAllEpicTasks();

    List<SubTask> getAllSubTasks();

    List<Task> getHistory();
}
