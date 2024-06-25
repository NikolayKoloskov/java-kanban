package manager;

import tasks.EpicTask;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

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
    public void createTask(Task task) {
        task.setId(id);
        tasks.put(id, task);
        id++;
    }

    @Override
    public void createEpicTask(EpicTask epicTask) {
        epicTask.setId(id);
        epicTasks.put(id, epicTask);
        autoSetEpicStatus(id);
        id++;
    }

    @Override
    public void createSubTask(SubTask subTask) {
        int mainId = subTask.getMainId();
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
    public ArrayList<SubTask> getAllSubTasksByEpicId(int epicId) {
        ArrayList<SubTask> subTask;
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
    public void deleteTask(Task task) {
        int idDelete = task.getId();
        tasks.remove(idDelete);
        history.remove(idDelete);
    }

    @Override
    public void deleteEpicTask(EpicTask epicTask) {
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
    public void deleteSubTask(SubTask subTask) {
        int idDelete = subTask.getId();
        if (subTasks.containsKey(idDelete)) {
            EpicTask epicTask = epicTasks.get(subTask.getMainId());
            epicTask.removeSubtask(subTask);
            subTasks.remove(idDelete);
            autoSetEpicStatus(epicTask.getId());
            history.remove(idDelete);
        }
    }

    @Override
    public void deleteAllTasks() {
        for (Integer task : tasks.keySet()) {
            history.remove(task);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpicTasks() {
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
    public void deleteAllSubTasks() {
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
    public void deleteAllSubTasksInEpic(int epicId) {
        if (epicTasks.containsKey(epicId)) {
            EpicTask epicTask = epicTasks.get(epicId);
            for (SubTask subTask : epicTask.getSubTasks()) {
                subTasks.remove(subTask.getId());
            }
            epicTask.removeAllSubtasks();
            autoSetEpicStatus(epicTask.getId());
        }
    }

    @Override
    public void updateTask(Task task) {
        int id = task.getId();
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
        }
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        int id = epicTask.getId();
        if (epicTasks.containsKey(id)) {
            EpicTask epicTask1 = epicTasks.get(id);
            epicTask1.setName(epicTask.getName());
            epicTask1.setDescription(epicTask.getDescription());
            autoSetEpicStatus(id);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        int id = subTask.getId();
        int mainId = subTask.getMainId();
        if ((subTasks.containsKey(id)) && (epicTasks.containsKey(mainId)) && (subTasks.get(id).getMainId() == subTask.getMainId())) {
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
}