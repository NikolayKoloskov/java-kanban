import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, EpicTask> epicTasks;
    private HashMap<Integer, SubTask> subTasks;
    private int id = 1;


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

    public void createTask(String name, String description, Status status) {
        Task task = new Task(name, description, status);
        task.setId(id);
        tasks.put(id, task);
        id++;
    }

    public void createEpicTask(String name, String description) {
        EpicTask epicTask = new EpicTask(name, description);
        epicTask.setId(id);
        epicTasks.put(id, epicTask);
        autoSetEpicStatus(id);
        id++;
    }

    public void createSubTask(int mainId, String name, String description, Status status) {
        SubTask subTask = new SubTask(mainId, name, description, status);
        subTask.setId(id);
        if (epicTasks.containsKey(mainId)) {
            EpicTask epicTask = epicTasks.get(mainId);
            epicTask.addSubtask(subTask);
            subTasks.put(id, subTask);
            autoSetEpicStatus(mainId);
            id++;
        }
    }

    public ArrayList<SubTask> getAllSubTasks(int epicId) {
        return epicTasks.get(epicId).getSubTasks();
    }

    public void autoSetEpicStatus(int id) {
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

    public void deleteTask(int idDelete) {
        tasks.remove(idDelete);
    }

    public void deleteEpicTask(int idDelete) {
        if (epicTasks.containsKey(idDelete)) {
            EpicTask epicTask = epicTasks.get(idDelete);
            ArrayList<SubTask> subTasksToDelete = epicTask.getSubTasks();
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
            autoSetEpicStatus(epicTask.getId());
        }
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpicTasks() {
        epicTasks.clear();
        subTasks.clear();
    }

    public void deleteAllSubTasks() {
        for (EpicTask epicTask : epicTasks.values()) {
            epicTask.removeAllSubtasks();
        }
        subTasks.clear();
    }

    public void updateTask(int id, String name, String description, Status status) {
        if (tasks.containsKey(id)) {
            Task task = tasks.get(id);
            task.setName(name);
            task.setDescription(description);
            task.setStatus(status);
        }
    }

    public void updateEpicTask(int id, String name, String description) {
        if (epicTasks.containsKey(id)) {
            EpicTask epicTask = epicTasks.get(id);
            epicTask.setName(name);
            epicTask.setDescription(description);
        }
    }

    public void updateSubTask(int id, String name, String description, Status status) {
        int mainId = subTasks.get(id).getMainId();
        if ((subTasks.containsKey(id)) && (epicTasks.containsKey(mainId))) {
            SubTask subTask = subTasks.get(id);
            subTask.update(name, description, status);
            EpicTask epicTask = epicTasks.get(mainId);
            SubTask subTask1 = epicTask.getSubTask(mainId);
            subTask1.update(name, description, status);
            autoSetEpicStatus(mainId);
        }
    }

    public List<Task> getAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            allTasks.add(task);
        }
        return allTasks;
    }

    public List<EpicTask> getAllEpicTasks() {
        ArrayList<EpicTask> allEpicTasks = new ArrayList<>();
        for (EpicTask epicTask : epicTasks.values()) {
            allEpicTasks.add(epicTask);
        }
        return allEpicTasks;
    }

    public List<SubTask> getAllSubTasks() {
        ArrayList<SubTask> allSubTasks = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            allSubTasks.add(subTask);
        }
        return allSubTasks;
    }
}
