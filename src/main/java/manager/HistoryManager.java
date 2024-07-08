package main.java.manager;

import main.java.tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    List<Task> getHistory();
}
