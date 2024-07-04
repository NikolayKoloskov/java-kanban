package main.java.manager;


import main.java.tasks.EpicTask;
import main.java.tasks.Status;
import main.java.tasks.SubTask;
import main.java.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasks;
    private Map<Integer, EpicTask> epicTasks;
    private Map<Integer, SubTask> subTasks;
    private int id = 1;
    private HistoryManager history;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epicTasks = new HashMap<>();
        subTasks = new HashMap<>();
        history = Managers.getDefaultHistory();
    }

    public InMemoryHistoryManager getHistoryManager() {
        return (InMemoryHistoryManager) history;
    }

    @Override
    public Task getTask(int id) {
        if (tasks.containsKey(id)) {
            history.add(tasks.get(id));
            return tasks.get(id);
        } else return null;
    }

    @Override
    public EpicTask getEpicTask(int id) {
        if (epicTasks.containsKey(id)) {
            history.add(epicTasks.get(id));
            return epicTasks.get(id);
        } else return null;
    }

    @Override
    public SubTask getSubTask(int id) {
        if (subTasks.containsKey(id)) {
            history.add(subTasks.get(id));
            return subTasks.get(id);
        } else return null;
    }

    @Override
    public void createTask(Task task) throws ManagerSaveException {
        task.setId(id);
        tasks.put(id, task);
        id++;
    }

    @Override
    public void createEpicTask(EpicTask epicTask) throws ManagerSaveException {
        epicTask.setId(id);
        epicTasks.put(id, epicTask);
        autoSetEpicStatus(id);
        id++;
    }

    @Override
    public void createSubTask(SubTask subTask) throws ManagerSaveException {
        int mainId = subTask.getEpicId();
        if (epicTasks.containsKey(mainId)) {
            subTask.setId(id);
            EpicTask epicTask = epicTasks.get(mainId);
            epicTask.addSubtask(subTask);
            subTasks.put(id, subTask);
            autoSetEpicStatus(mainId);
            id++;
        }
    }

    @Override
    public List<SubTask> getAllSubTasksByEpicId(int epicId) {
        List<SubTask> subTask;
        if (epicTasks.containsKey(epicId)) {
            subTask = epicTasks.get(epicId).getSubTasks();
        } else {
            subTask = new ArrayList<>();
        }
        return subTask;
    }

    private void autoSetEpicStatus(int id) {
        if (!epicTasks.isEmpty()) {
            boolean allNew = true;
            boolean anyInProgress = false;
            EpicTask epicTask = epicTasks.get(id);
            ArrayList<SubTask> subTasks = epicTask.getSubTasks();
            if (!subTasks.isEmpty()) {
                for (SubTask subTask : subTasks) {
                    if (subTask.getStatus() != Status.NEW) {
                        allNew = false;
                    }
                    if (subTask.getStatus() == Status.IN_PROGRESS) {
                        anyInProgress = true;
                    }
                    if (!allNew && anyInProgress) {
                        break;
                    }
                }
            }
            if (allNew) {
                epicTask.setStatus(Status.NEW);
            } else if (anyInProgress) {
                epicTask.setStatus(Status.IN_PROGRESS);
            } else {
                epicTask.setStatus(Status.DONE);
            }
        }
    }

    @Override
    public void deleteTask(Task task) throws ManagerSaveException {
        int idDelete = task.getId();
        tasks.remove(idDelete);
        history.remove(idDelete);
    }

    @Override
    public void deleteEpicTask(EpicTask epicTask) throws ManagerSaveException {
        int idDelete = epicTask.getId();
        if (epicTasks.containsKey(idDelete)) {
            EpicTask epicTaskToDelete = epicTasks.get(idDelete);
            ArrayList<SubTask> subTasksToDelete = epicTaskToDelete.getSubTasks();
            for (SubTask subTask : subTasksToDelete) {
                subTasks.remove(subTask.getId());
                history.remove(subTask.getId());
            }

            history.remove(idDelete);
            epicTasks.remove(idDelete);
        }
    }

    @Override
    public void deleteSubTask(SubTask subTask) throws ManagerSaveException {
        int idDelete = subTask.getId();
        if (subTasks.containsKey(idDelete)) {
            EpicTask epicTask = epicTasks.get(subTask.getEpicId());
            epicTask.removeSubtask(subTask);
            subTasks.remove(idDelete);
            autoSetEpicStatus(epicTask.getId());
            history.remove(idDelete);
        }
    }

    @Override
    public void deleteAllTasks() throws ManagerSaveException {
        for (Integer task : tasks.keySet()) {
            history.remove(task);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpicTasks() throws ManagerSaveException {
        for (Integer epicTask : epicTasks.keySet()) {
            history.remove(epicTask);
        }
        epicTasks.clear();
        for (Integer subTask : subTasks.keySet()) {
            history.remove(subTask);
        }
        subTasks.clear();

    }

    //Метод удаляет вообще все подзадачи по этому ничего на вход не принимает
    @Override
    public void deleteAllSubTasks() throws ManagerSaveException {
        for (EpicTask epicTask : epicTasks.values()) {
            //да - действительно еще вложенный цикл был лишним - не внимательно посмотрел
            epicTask.removeAllSubtasks();
            autoSetEpicStatus(epicTask.getId());
        }
        for (Integer subTask : subTasks.keySet()) {
            history.remove(subTask);
        }
        subTasks.clear();
    }

    @Override
    public void deleteAllSubTasksInEpic(int epicId) throws ManagerSaveException {
        if (epicTasks.containsKey(epicId)) {
            EpicTask epicTask = epicTasks.get(epicId);
            for (SubTask subTask : epicTask.getSubTasks()) {
                subTasks.remove(subTask.getId());
                history.remove(subTask.getId());
            }
            epicTask.removeAllSubtasks();
            autoSetEpicStatus(epicTask.getId());
        }
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException {
        int id = task.getId();
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
        }
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) throws ManagerSaveException {
        int id = epicTask.getId();
        if (epicTasks.containsKey(id)) {
            EpicTask epicTask1 = epicTasks.get(id);
            epicTask1.setName(epicTask.getName());
            epicTask1.setDescription(epicTask.getDescription());
            autoSetEpicStatus(id);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) throws ManagerSaveException {
        int id = subTask.getId();
        int mainId = subTask.getEpicId();
        if ((subTasks.containsKey(id)) && (epicTasks.containsKey(mainId)) && (subTasks.get(id).getEpicId() == subTask.getEpicId())) {
            SubTask subTaskToUpdate = subTasks.get(id);
            subTaskToUpdate.update(subTask.getName(), subTask.getDescription(), subTask.getStatus());
            EpicTask epicTask = epicTasks.get(mainId);
            SubTask subTask1 = epicTask.getSubTask(id);
            subTask1.update(subTask.getName(), subTask.getDescription(), subTask.getStatus());
            autoSetEpicStatus(mainId);

        }
    }

    @Override
    public List<Task> getAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            allTasks.add(task);
            history.add(task);
        }
        return allTasks;
    }

    @Override
    public List<EpicTask> getAllEpicTasks() {
        ArrayList<EpicTask> allEpicTasks = new ArrayList<>();
        for (EpicTask epicTask : epicTasks.values()) {
            allEpicTasks.add(epicTask);
            history.add(epicTask);
        }
        return allEpicTasks;
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        ArrayList<SubTask> allSubTasks = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            allSubTasks.add(subTask);
            history.add(subTask);
        }
        return allSubTasks;
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    @Override
    public void setTask(Task task) {
        if (task.getClass().equals(Task.class)) {
            tasks.put(task.getId(), task);
        } else if (task.getClass().equals(EpicTask.class)) {
            epicTasks.put(task.getId(), (EpicTask) task);
        } else if (task.getClass().equals(SubTask.class)) {
            SubTask subTask = (SubTask) task;
            subTasks.put(subTask.getId(), subTask);
            epicTasks.get(subTask.getEpicId()).addSubtask(subTask);
        }

    }

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public Map<Integer, EpicTask> getEpicTasks() {
        return epicTasks;
    }

    public Map<Integer, SubTask> getSubTasks() {
        return subTasks;
    }
}