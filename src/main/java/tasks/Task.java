package main.java.tasks;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private int id;
    private String name;
    private String description;
    private Status status;
    TaskType taskType;
    private Duration duration;
    private LocalDateTime startTime;
    public final LocalDateTime DEFAULT_TIME = LocalDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);
    public final Duration DEFAULT_DURATION = Duration.ZERO;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.taskType = TaskType.TASK;
        this.startTime = DEFAULT_TIME;
        this.duration = DEFAULT_DURATION;

    }

    public Task(String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.taskType = TaskType.TASK;
        this.startTime = startTime;
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        return LocalDateTime.from(startTime.plus(duration));
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

    public void update(String name, String description, Status status) {
        this.status = status;
        this.description = description;
        this.name = name;
        this.duration = getDuration();
        this.startTime = getStartTime();
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

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    @Override
    public String toString() {
        return "Task" + "\n" + "{" +
                "id='" + id + "', " +
                "name='" + name + "', " +
                "description='" + description + "', " +
                "status='" + status + "', " +
                "StartTime='" + startTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) + "', " +
                "Duration='" + duration.toMinutes() + "', " +

                "}" + "\n";
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && status == task.status && taskType == task.taskType
                && Objects.equals(startTime, task.startTime) && Objects.equals(duration, task.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getDEFAULT_TIME() {
        return DEFAULT_TIME;
    }

    public Duration getDEFAULT_DURATION() {
        return DEFAULT_DURATION;
    }
}