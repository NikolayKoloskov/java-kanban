import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> tasks;
    HashMap<Integer, EpicTask> epicTasks;
    HashMap<Integer, SubTask> subTasks;
    public int id = 1;


    public TaskManager() {
        tasks = new HashMap<>();
        epicTasks = new HashMap<>();
        subTasks = new HashMap<>();
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public EpicTask getEpicTask(int id) {
        return epicTasks.get(id);
    }

    public SubTask getSubTask(int id) {
        return subTasks.get(id);
    }

    public void createTask(String name, String description) {
        Task task = new Task(id, name, description);
        task.setStatus(Status.NEW);
        tasks.put(id, task);
        id++;
    }

    public void createEpicTask(String name, String description) {
        EpicTask epicTask = new EpicTask(id, name, description);
        epicTask.setStatus(Status.NEW);
        epicTasks.put(id, epicTask);
        autoSetEpicStatus(id);
        id++;
    }

    public void createSubTask(int mainId, String name, String description) {
        SubTask subTask = new SubTask(mainId, id, name, description);
        subTask.setSubTaskStatus(Status.NEW);
        EpicTask epicTask = epicTasks.get(mainId);
        epicTask.addSubtask(subTask);
        subTasks.put(id, subTask);
        autoSetEpicStatus(mainId);
        id++;
    }

    public void changeTaskStatus(int id, Status status) {
        if (tasks.containsKey(id)) {
            Task task = tasks.get(id);
            if (status != task.getStatus()) {
                task.setStatus(status);
            }
        }
    }

    public void changeSubTaskStatus(int id, Status status) {
        if (!subTasks.isEmpty()) {
            if (subTasks.containsKey(id)) {
                SubTask subTask = subTasks.get(id);
                if (status != subTask.getSubTaskStatus()) {
                    subTask.setSubTaskStatus(status);
                }
            }
            autoSetEpicStatus(subTasks.get(id).getMainId());
        }
    }

    public void changeEpicTaskStatus(int id, Status status) {
        if (!epicTasks.isEmpty()) {
            if (epicTasks.containsKey(id)) {
                EpicTask epicTask = epicTasks.get(id);
                if (epicTask.getSubTasks() == null) {
                    if (status != epicTask.getStatus()) {
                        epicTask.setStatus(status);
                    }
                }
            }
        }


    }


    public void autoSetEpicStatus(int id) {
        if (!epicTasks.isEmpty()) {
            boolean allNew = true;
            boolean anyInProgress = false;
            EpicTask epicTask = epicTasks.get(id);
            ArrayList<SubTask> subTasks = epicTask.getSubTasks();
            for (SubTask subTask : subTasks) {
                if (subTask.getSubTaskStatus() != Status.NEW) {
                    allNew = false;
                }
                if (subTask.getSubTaskStatus() == Status.IN_PROGRESS) {
                    anyInProgress = true;
                }
                if (!allNew && anyInProgress) {
                    break;
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

    public void deleteTask(int idDelete) {
        if (tasks.containsKey(idDelete)) {
            tasks.remove(idDelete);
        }
    }

    public void deleteEpicTask(int idDelete) {
        if (epicTasks.containsKey(idDelete)) {
            EpicTask epicTask = epicTasks.get(idDelete);
            ArrayList<SubTask> subTasksToDelete = epicTask.getSubTasks();
            ArrayList<Integer> idsToDelete = new ArrayList<>();
            for (SubTask subTask : subTasksToDelete) {
                subTasks.remove(subTask.getId());
            }
            epicTasks.remove(idDelete);
        }
    }

    public void deleteSubTask(int idDelete) {
        if (subTasks.containsKey(idDelete)) {
            SubTask subTask = subTasks.get(idDelete);
            EpicTask epicTask = epicTasks.get(subTask.getMainId());
            epicTask.removeSubtask(subTask);
            subTasks.remove(idDelete);

        }
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public void setTasks(HashMap<Integer, Task> tasks) {
        this.tasks = tasks;
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(HashMap<Integer, SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public HashMap<Integer, EpicTask> getEpicTasks() {
        return epicTasks;
    }

    public void setEpicTasks(HashMap<Integer, EpicTask> epicTasks) {
        this.epicTasks = epicTasks;
    }
}
