package tasks;

import java.util.ArrayList;


public class EpicTask extends Task {
    private ArrayList<SubTask> subTasks;

    public EpicTask(String name, String description) {
        super(name, description, Status.NEW);
        this.subTasks = new ArrayList<>();
    }

    public void addSubtask(SubTask task) {
        if (!subTasks.contains(task)) {
            subTasks.add(task);
        } else {
            System.out.println("Задача уже существует");
        }
    }

    public void removeSubtask(SubTask task) {
        if (subTasks == null) {
            System.out.println("Список задач пуст");
        } else {
            if (subTasks.contains(task)) {
                subTasks.remove(task);
            } else {
                System.out.println("Задача не найдена");
            }
        }
    }

    public void removeAllSubtasks() {
        if (subTasks == null) {
            System.out.println("Список задач пуст");
        } else {
            subTasks.clear();
        }
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public SubTask getSubTask(int id) {
        SubTask subTask = null;
        for (int i = 0; i < subTasks.size(); i++) {
            if (subTasks.get(i).getId() == id) {
                subTask = subTasks.get(i);
                break;
            }
        }
        return subTask;
    }

    @Override
    public String toString() {
        return "EpicTask" + "\n" + "{" +
                "id='" + getId() + "', " +
                "status='" + getStatus() + "', " +
                "name='" + getName() + "', " +
                "description='" + getDescription() + "', " +
                "subtasks'=" + subTasks + "', " +
                '}' + "\n";
    }
}