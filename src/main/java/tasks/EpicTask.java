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
    LocalDateTime epicEndTime;

    public EpicTask(String name, String description) {
        super(name, description, Status.NEW);
        this.subTasks = new ArrayList<>();
        this.taskType = TaskType.EPIC_TASK;
        this.epicTime = null;
        this.epicDuration = null;
    }

    public void setEpicTime() {
        if (subTasks.isEmpty()) {
            this.epicTime = null;
            super.setStartTime(null);
        } else {
            this.epicTime = getStartTime();
            super.setStartTime(epicTime);
        }
    }

    public void setDuration() {
        if (subTasks.isEmpty()) {
            this.epicDuration = null;
            super.setDuration(null);
        } else {
            this.epicDuration = getDuration();
            super.setDuration(epicDuration);
        }
    }

    @Override
    public Duration getDuration() {
        Duration generalDuration = Duration.ofMinutes(0);
        for (SubTask sub : subTasks) {
            if (sub.getStartTime() == null) {
                generalDuration = generalDuration.plus(Duration.ZERO);
            } else generalDuration = generalDuration.plus(sub.getDuration());
        }
        return generalDuration;
    }

    @Override
    public LocalDateTime getStartTime() {
        if (subTasks.isEmpty()) {
            return null;
        } else if (subTasks.stream().allMatch(task -> task.getStartTime() == null)) {
            return null;
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
                .map(Task::getStartTime)
                .filter(startTime -> startTime != null)
                .findFirst();
        return firstNonDefaultTime.get();
    }

    @Override
    public LocalDateTime getEndTime() {
        if (subTasks.isEmpty()) {
            return null;
        } else if (subTasks.stream().allMatch(task -> task.getEndTime() == null)) {
            return null;
        }
        subTasks.sort((task1, task2) -> {
            if (task1.getEndTime().isAfter(task2.getEndTime())) {
                return 1;
            }
            if (task1.getEndTime().isBefore(task2.getEndTime())) {
                return -1;
            } else {
                return 0;
            }

        });
        Optional<LocalDateTime> lastTime = subTasks.stream()
                .map(task -> task.getStartTime().plus(task.getDuration()))
                .max(LocalDateTime::compareTo);
        this.epicEndTime = lastTime.get();
        return lastTime.get();
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
                    getEndTime();
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
            getEndTime();

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
        String time;
        String duration;
        if (epicTime == null) {
            time = "null";
        } else time = epicTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        if (epicDuration == null) {
            duration = "null";
        } else duration = String.valueOf(epicDuration.toMinutes());
        return "EpicTask" + "\n" + "{" +
                "id='" + getId() + "', " +
                "status='" + getStatus() + "', " +
                "name='" + getName() + "', " +
                "description='" + getDescription() + "', " +
                "StartTime='" + time + "', " +
                "Duration='" + duration + "', " +
                "subtasks'=" + subTasks + "', " +
                "endTime'=" + getEndTime().toString() +
                '}' + "\n";
    }
}