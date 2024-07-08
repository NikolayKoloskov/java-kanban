package main.java.tasks;

public class SubTask extends Task {
    private int epicId;

    public SubTask(int epicId, String name, String description, Status status) {
        super(name, description, status);
        this.epicId = epicId;
        this.taskType = TaskType.SUB_TASK;
    }


    @Override
    public String toString() {
        return "SubTask" + "\n" + "{" +
                "EpicId =" + epicId + ", " +
                "Tasks.Status='" + super.getStatus() + "', " +
                "Id='" + super.getId() + "', " +
                "Name='" + super.getName() + "', " +
                "Description='" + super.getDescription() + "', " +
                '}' + "\n";
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

}