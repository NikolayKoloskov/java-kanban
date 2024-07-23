package main.java.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubTask extends Task {
    private int epicId;

    public SubTask(int epicId, String name, String desc, Status status) {
        super(name, desc, status);
        this.epicId = epicId;
        this.setTaskType(TaskType.SUB_TASK);
    }

    public SubTask(int epicId, String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
        this.setTaskType(TaskType.SUB_TASK);
    }


    @Override
    public String toString() {
        return "SubTask" + "\n" + "{" +
                "EpicId =" + epicId + ", " +
                "Tasks.Status='" + super.getStatus() + "', " +
                "Id='" + super.getId() + "', " +
                "Name='" + super.getName() + "', " +
                "Description='" + super.getDescription() + "', " +
                "StartTime='" + super.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) + "', " +
                "Duration='" + super.getDuration().toMinutes() + "', " +
                '}' + "\n";
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

}