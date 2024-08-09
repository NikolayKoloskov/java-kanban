package main.java.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import main.java.adapter.DurationAdapter;
import main.java.adapter.LocalDateTimeTypeAdapter;
import main.java.handler.*;
import main.java.tasks.EpicTask;
import main.java.tasks.Status;
import main.java.tasks.SubTask;
import main.java.tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    protected HttpTaskManager fileBacked;
    private final HttpServer httpServer;

    public HttpTaskServer(HttpTaskManager httpTaskManager) throws IOException {
        fileBacked = httpTaskManager;
        httpServer = HttpServer.create(new InetSocketAddress(62300), 0);
        createContext();
    }

    private void createContext() {
        httpServer.createContext("/tasks", new TaskHandler(fileBacked));
        httpServer.createContext("/subtasks", new SubTaskHandler(fileBacked));
        httpServer.createContext("/epics", new EpicHandler(fileBacked));
        httpServer.createContext("/history", new HistoryHandler(fileBacked));
        httpServer.createContext("/prioritized", new PrioritizedHandler(fileBacked));
    }

    public void start() {
        httpServer.start();
        System.out.println("Сервер запущен на порту: " + +httpServer.getAddress().getPort());

    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Сервер остановлен.");
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
    }

    public static void main(String[] args) throws IOException, ManagerSaveException, InterruptedException {
        HttpTaskServer server = new HttpTaskServer(Managers.getDefault());
        server.start();
        server.fileBacked.createTask(new Task("Name1", "Description1", Status.NEW));
        server.fileBacked.createEpicTask(new EpicTask("Name2", "Description2"));
        server.fileBacked.createSubTask(new SubTask(2, "name3", "description3", Status.NEW));
        server.fileBacked.createTask(new Task("Name4", "Description4", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(60)));
        server.fileBacked.getTask(1);
    }
}
