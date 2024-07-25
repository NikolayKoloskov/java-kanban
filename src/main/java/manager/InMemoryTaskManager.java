package main.java.manager;


import main.java.tasks.EpicTask;
import main.java.tasks.Status;
import main.java.tasks.SubTask;
import main.java.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasks;
    private Map<Integer, EpicTask> epicTasks;
    private Map<Integer, SubTask> subTasks;
    private int id = 1;
    private HistoryManager history;
    private Set<Task> tasksSortedByTime;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epicTasks = new HashMap<>();
        subTasks = new HashMap<>();
        history = new InMemoryHistoryManager();
        tasksSortedByTime = new TreeSet<>((t1, t2) -> {
            if (t1.getStartTime() != null || t2.getStartTime() != null) {
                if (t1.getId() == (t2.getId())) {
                    return 0;
                } else if (t1.getStartTime().isBefore(t2.getStartTime())) {
                    return 1;
                } else {
                    return -1;
                }
            }
            return -1;
        });
    }

    public InMemoryHistoryManager getHistoryManager() {
        return (InMemoryHistoryManager) history;
    }

    private boolean checkTimeIsFree(Task task) {
        if (task.getStartTime() == null) {
            return false;
        } else return tasksSortedByTime.stream().filter(task1 -> task1.getId() != task.getId())
                .anyMatch(t -> (
                        (t.getStartTime().isBefore(task.getEndTime()) & t.getEndTime().isAfter(task.getEndTime()))) ||
                        (t.getEndTime().isAfter(task.getStartTime()) & t.getStartTime().isBefore(task.getStartTime())) ||
                        (t.getStartTime().isBefore(task.getStartTime()) & t.getEndTime().isAfter(task.getEndTime())) ||
                        (t.getStartTime().isAfter(task.getStartTime()) & t.getEndTime().isBefore(task.getEndTime())) ||
                        (t.getStartTime().isEqual(task.getStartTime()) || (t.getEndTime().isEqual(task.getEndTime()))));
    }

    public void addToSortedTasks(Task task) {
        if (task.getStartTime() != null) {
            if (!checkTimeIsFree(task)) {
                if (!tasksSortedByTime.contains(task)) {
                    tasksSortedByTime.add(task);
                } else {
                    tasksSortedByTime.remove(task);
                    tasksSortedByTime.add(task);
                }
            } else {
                throw new ManagerSaveException("Временной интервал уже занят другой задачей");
            }
        }
    }

    public List<Task> getPrioritizedTasks() {
        return tasksSortedByTime.stream().toList();
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
        addToSortedTasks(task);
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
        int mainId = subTask.getEpicId();
        if (epicTasks.containsKey(mainId)) {
            subTask.setId(id);
            EpicTask epicTask = epicTasks.get(mainId);
            epicTask.addSubtask(subTask);
            subTasks.put(id, subTask);
            autoSetEpicStatus(mainId);
            addToSortedTasks(subTask);
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
            boolean allDone = false;
            EpicTask epicTask = epicTasks.get(id);
            ArrayList<SubTask> subTasks = epicTask.getSubTasks();
            if (!subTasks.isEmpty()) {
                allNew = subTasks.stream().allMatch(subTask -> subTask.getStatus() == Status.NEW);
                anyInProgress = subTasks.stream().anyMatch(subTask -> subTask.getStatus() != Status.NEW);
                allDone = subTasks.stream().allMatch(subTask -> subTask.getStatus() == Status.DONE);

            }
            if (allNew) {
                epicTask.setStatus(Status.NEW);
            } else if (allDone) {
                epicTask.setStatus(Status.DONE);
            } else if (anyInProgress) {
                epicTask.setStatus(Status.IN_PROGRESS);
            }
            epicTask.setEpicTime();
            epicTask.setDuration();
        }
    }

    @Override
    public void deleteTask(Task task) {
        int idDelete = task.getId();
        tasks.remove(idDelete);
        history.remove(idDelete);
        tasksSortedByTime.remove(task);
    }

    @Override
    public void deleteEpicTask(EpicTask epicTask) {
        int idDelete = epicTask.getId();
        if (epicTasks.containsKey(idDelete)) {
            EpicTask epicTaskToDelete = epicTasks.get(idDelete);
            ArrayList<SubTask> subTasksToDelete = epicTaskToDelete.getSubTasks();
            for (SubTask subTask : subTasksToDelete) {
                subTasks.remove(subTask.getId());
                tasksSortedByTime.remove(subTask);
                history.remove(subTask.getId());
            }
            history.remove(idDelete);
            epicTasks.remove(idDelete);
            tasksSortedByTime.remove(epicTask);
        }
    }

    @Override
    public void deleteSubTask(SubTask subTask) {
        int idDelete = subTask.getId();
        if (subTasks.containsKey(idDelete)) {
            EpicTask epicTask = epicTasks.get(subTask.getEpicId());
            epicTask.removeSubtask(subTask);
            subTasks.remove(idDelete);
            autoSetEpicStatus(epicTask.getId());
            tasksSortedByTime.remove(subTask);
            epicTask.setEpicTime();
            epicTask.setDuration();
            history.remove(idDelete);
        }
    }

    @Override
    public void deleteAllTasks() {
        for (Integer task : tasks.keySet()) {
            history.remove(task);
            tasksSortedByTime.remove(tasks.get(task));
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
            tasksSortedByTime.remove(subTasks.get(subTask));
        }
        subTasks.clear();
    }

    //Метод удаляет вообще все подзадачи по этому ничего на вход не принимает
    @Override
    public void deleteAllSubTasks() {
        for (EpicTask epicTask : epicTasks.values()) {
            epicTask.removeAllSubtasks();
            autoSetEpicStatus(epicTask.getId());
        }
        for (Integer subTask : subTasks.keySet()) {
            history.remove(subTask);
            tasksSortedByTime.remove(subTasks.get(subTask));
        }
        subTasks.clear();
    }

    @Override
    public void deleteAllSubTasksInEpic(int epicId) {
        if (epicTasks.containsKey(epicId)) {
            EpicTask epicTask = epicTasks.get(epicId);
            for (SubTask subTask : epicTask.getSubTasks()) {
                subTasks.remove(subTask.getId());
                history.remove(subTask.getId());
                tasksSortedByTime.remove(subTasks.get(subTask));
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
            tasksSortedByTime.remove(task);
            tasksSortedByTime.add(task);
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
            tasksSortedByTime.remove(epicTask);
            tasksSortedByTime.add(epicTask);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        int id = subTask.getId();
        int mainId = subTask.getEpicId();
        if ((subTasks.containsKey(id)) && (epicTasks.containsKey(mainId)) && (subTasks.get(id).getEpicId() == subTask.getEpicId())) {
            SubTask subTaskToUpdate = subTasks.get(id);
            tasksSortedByTime.remove(subTask);
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

    protected void setTask(Task task) {
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

    protected int getId() {
        return this.id;
    }

    protected void setId(int id) {
        this.id = id;
    }
}