package main.java.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;


public class EpicTask extends Task {
    private ArrayList<SubTask> subTasks;
    LocalDateTime epicTime;
    Duration epicDuration;

    public EpicTask(String name, String description) {
        super(name, description, Status.NEW);
        this.subTasks = new ArrayList<>();
        this.taskType = TaskType.EPIC_TASK;
        this.epicTime = DEFAULT_TIME;
        this.epicDuration = DEFAULT_DURATION;
    }

    public void setEpicTime() {
        if (subTasks.isEmpty()) {
            this.epicTime = DEFAULT_TIME;
            super.setStartTime(DEFAULT_TIME);
        } else {
            this.epicTime = getStartTime();
            super.setStartTime(epicTime);
        }
    }

    public void setDuration() {
        if (subTasks.isEmpty()) {
            this.epicDuration = DEFAULT_DURATION;
            super.setDuration(DEFAULT_DURATION);
        } else {
            this.epicDuration = getDuration();
            super.setDuration(epicDuration);
        }
    }

    @Override
    public Duration getDuration() {
        Duration generalDuration = Duration.ofMinutes(0);
        for (SubTask sub : subTasks) {
            generalDuration = generalDuration.plus(sub.getDuration());
        }
        return generalDuration;
    }

    @Override
    public LocalDateTime getStartTime() {
        if (subTasks.isEmpty()) {
            return DEFAULT_TIME;
        }
        subTasks.sort((task1, task2) -> {
                    if (task1.getStartTime().isAfter(task2.getStartTime())) {
                        return 1;
                    }
                    if (task1.getStartTime().isBefore(task2.getStartTime())) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
        );
        Optional<LocalDateTime> firstNonDefaultTime = subTasks.stream()
                .map(Task::getStartTime).filter(startTime -> startTime != DEFAULT_TIME)
                .findFirst();
        return firstNonDefaultTime.orElse(DEFAULT_TIME);
    }

    public void addSubtask(SubTask task) {
        if (!subTasks.contains(task)) {
            subTasks.add(task);
            setEpicTime();
            setDuration();
        } else {
            System.out.println("Задача уже существует");
        }
    }

    public void removeSubtask(SubTask task) {
        if (task != null) {
            if (subTasks == null) {
                System.out.println("Список задач пуст");
            } else {
                if (subTasks.contains(task)) {
                    subTasks.remove(task);
                    setEpicTime();
                    setDuration();
                } else {
                    System.out.println("Задача не найдена");
                }
            }
        }
    }

    public void removeAllSubtasks() {
        if (subTasks == null) {
            System.out.println("Список задач пуст");
        } else {
            subTasks.clear();
            setEpicTime();
            setDuration();
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
                "StartTime='" + epicTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) + "', " +
                "Duration='" + epicDuration.toMinutes() + "', " +
                "subtasks'=" + subTasks + "', " +
                '}' + "\n";
    }
}