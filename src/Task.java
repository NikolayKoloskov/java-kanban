public class Task {
    protected final int id;
    private String name;
    private String description;
    private Status status;

    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status epicStatus) {
        this.status = epicStatus;
    }


    @Override
    public String toString() {
        return "Task{" + "\n" +
                "id=" + id + "," +
                "name='" + name + "," +
                "description='" + description + "," +
                "status=" + status + "," +
                '}';
    }
}