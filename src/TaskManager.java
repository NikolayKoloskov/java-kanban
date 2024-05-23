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

    public void createTask(Task task){
        task.setId(id);
        tasks.put(id, task);
        id++;
    }

    public void createEpicTask(EpicTask epicTask){
        epicTask.setId(id);
        epicTasks.put(id, epicTask);
        autoSetEpicStatus(id);
        id++;
    }

    public void createSubTask(SubTask subTask){
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

    public void deleteTask(Task task) {
        int idDelete = task.getId();
        tasks.remove(idDelete);
    }

    public void deleteEpicTask(EpicTask epicTask) {
        int idDelete = epicTask.getId();
        if (epicTasks.containsKey(idDelete)) {
            EpicTask epicTaskToDelete = epicTasks.get(idDelete);
            ArrayList<SubTask> subTasksToDelete = epicTaskToDelete.getSubTasks();
            for (SubTask subTask : subTasksToDelete) {
                subTasks.remove(subTask.getId());
            }
            epicTasks.remove(idDelete);
        }
    }

    public void deleteSubTask(SubTask subTask) {
        int idDelete = subTask.getId();
        if (subTasks.containsKey(idDelete)) {
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
    //Метод удаляет вообще все подзадачи по этому ничего на вход не принимает
    public void deleteAllSubTasks() {
        for (EpicTask epicTask : epicTasks.values()) {
            //да - действительно еще вложенный цикл был лишним - не внимательно посмотрел
            epicTask.removeAllSubtasks();
            autoSetEpicStatus(epicTask.getId());

        }
        subTasks.clear();
    }
    //Добавил метод для удаления подзадач по эпику
    //сначала удаляю все подзадачи из мапы подзадач - потом чищу эти подзадачи в эпике и обновляю статус у эпика
    public void deleteAllSubTasksInEpic(int epicId) {
        EpicTask epicTask = epicTasks.get(epicId);
        if (epicTasks.containsKey(epicTask.getId())){
            for (SubTask subTask : epicTask.getSubTasks()) {
                subTasks.remove(subTask.getId());
            }
            epicTask.removeAllSubtasks();
            autoSetEpicStatus(epicTask.getId());
        }
    }


    public void updateTask(Task task) {
        int id = task.getId();
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
        }
    }

    public void updateEpicTask(EpicTask epicTask) {
        int id = epicTask.getId();
        if (epicTasks.containsKey(id)) {
            EpicTask epicTask1 = epicTasks.get(id);
            epicTask1.setName(epicTask.getName());
            epicTask1.setDescription(epicTask.getDescription());
            autoSetEpicStatus(id);
        }
    }

    public void updateSubTask(SubTask subTask) {
        int id = subTask.getId();
        int mainId = subTask.getMainId();//поменял на вытаскивание этого id из таски которая к нам пришла а не из мапы
        //обьединил if - в 1 так как один без другого не имет значения
        if ((subTasks.containsKey(id)) && (epicTasks.containsKey(mainId)) && (subTasks.get(id).getMainId() == subTask.getMainId())){
                SubTask subTaskToUpdate = subTasks.get(id);
                subTaskToUpdate.update(subTask.getName(), subTask.getDescription(), subTask.getStatus());
                EpicTask epicTask = epicTasks.get(mainId);
                SubTask subTask1 = epicTask.getSubTask(id);
                subTask1.update(subTask.getName(), subTask.getDescription(), subTask.getStatus());
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
