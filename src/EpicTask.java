import java.util.ArrayList;


public class EpicTask extends Task {
    private ArrayList<SubTask> subTasks;
    private Status epicStatus;

    public EpicTask(int id, String name, String description) {
        super(id, name, description);
        this.subTasks = new ArrayList<>();
        this.epicStatus = Status.NEW;
    }

    public void addSubtask(SubTask task) {
        if (subTasks == null) {
            subTasks.add(task);
        }
        if (!subTasks.contains(task)) {
            subTasks.add(task);
        }
        else {
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



    @Override
    public void setStatus(Status epicStatus) {
        this.epicStatus = epicStatus;
    }

    @Override
    public Status getStatus() {
        return epicStatus;
    }

    @Override
    public String toString() {
        return "EpicTask{" + "\n" +
                "subtasks=" + subTasks +"\n" +
                "status=" + getStatus() +
                "name='" + getName() +
                "description='" + getDescription() +
                '}';
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }
    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }


}