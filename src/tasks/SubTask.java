package tasks;

public class SubTask extends Task {
    private int epicId;

    public SubTask(int epicId, String name, String description, Status status) {
        super(name, description, status);
        this.epicId = epicId;
    }


    @Override
    public String toString() {
        return "Tasks.SubTask" + "\n" + "{" +
                "EpicId =" + epicId + ", " +
                "Tasks.Status='" + super.getStatus() + "', " +
                "Id='" + super.getId() + "', " +
                "Name='" + super.getName() + "', " +
                "Description='" + super.getDescription() + "', " +
                '}' + "\n";
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getMainId() {
        return epicId;
    }

}