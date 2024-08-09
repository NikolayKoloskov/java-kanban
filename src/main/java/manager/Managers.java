package main.java.manager;

import java.io.IOException;

public class Managers {

    public static HttpTaskManager getDefault() throws IOException, InterruptedException {
        return new HttpTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
