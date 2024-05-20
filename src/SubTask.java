public class SubTask extends Task {
    private Status subTaskStatus;
    int epicId;

    public SubTask (int epicId, int id, String name, String description){
        super(id, name, description);
        this.epicId = epicId;
        subTaskStatus = Status.NEW;
    }

    @Override
    public String toString() {
        return "SubTask{" +"\n" +
                "EpicId =" + epicId + ","+
                "Status=" + subTaskStatus + "," +
                "Id=" + id + "," +
                "Name=" + super.getName() + "," +
                "Description=" + super.getDescription() + "," +
                '}'+ "\n";
    }

    public Status getSubTaskStatus() {
        return subTaskStatus;
    }

    public void setSubTaskStatus(Status subTaskStatus) {
        this.subTaskStatus = subTaskStatus;
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
