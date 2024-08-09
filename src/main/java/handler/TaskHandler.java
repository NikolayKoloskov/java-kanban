package main.java.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import main.java.adapter.TaskAdapter;
import main.java.manager.HttpTaskManager;
import main.java.manager.ManagerSaveException;
import main.java.manager.ManagerSortedSaveException;
import main.java.tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends DefaultHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public TaskHandler(HttpTaskManager httpTaskManager) {
        super(httpTaskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String query = "";
        try {
            query = exchange.getRequestURI().getQuery();
        } catch (Exception e) {
            query = "";
        }
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .create();
        switch (method) {
            case "GET":

                //Подразумевает вызов по эндпоинту /tasks

                if (pathParts.length == 2 && query == null) {
                    handleGetAllTasks(exchange);
                }
                //Подразумевает вызов по эндпоинту /tasks?id= или /tasks/task?id=
                else if (pathParts.length >= 2 && query != null & query.split("=").length > 1) {
                    int id = Integer.parseInt(query.split("=")[1]);
                    Task task = httpTaskManager.getTask(id);
                    String jsonTask = gson.toJson(task);
                    writeResponse(exchange, jsonTask, 200);
                }
                break;
            case "POST":
                //endpoint /tasks/task?id= или /tasks/task (в json находится новая или изменяемая таска
                if (pathParts[1].equals("tasks")) {
                    if (pathParts.length == 3 && query != null & query.split("=").length > 1) {
                        Task task = getTaskFromRequestBody(exchange);
                        if (httpTaskManager.getTasks().containsKey(task.getId())) {
                            try {
                                httpTaskManager.updateTask(task);
                            } catch (ManagerSortedSaveException e) {
                                writeResponse(exchange, e.getMessage(), 406);
                                break;
                            }
                        } else {
                            try {
                                httpTaskManager.createTask(task);
                            } catch (ManagerSaveException e) {
                                writeResponse(exchange, e.getMessage(), 406);
                                break;
                            }
                        }
                        if (task.equals(httpTaskManager.getTask(task.getId()))) {
                            writeResponse(exchange, "Задача сохранена", 201);
                            break;
                        }
                        break;
                    } else if (pathParts.length == 2) {
                        if (query != null) {
                            Task task = getTaskFromRequestBody(exchange);
                            httpTaskManager.updateTask(task);
                            String jsonTask = gson.toJson(task);
                            writeResponse(exchange, jsonTask, 201);
                            break;
                        }
                    }
                } else {
                    writeResponse(exchange, "Bad request", 400);
                }
                break;
            case "DELETE":
                //endpoint /tasks или /tasks/task?id=
                if (pathParts.length == 2) {
                    httpTaskManager.deleteAllTasks();
                    writeResponse(exchange, "Удалены все задачи", 201);
                } else if (pathParts.length > 2 & query != null & query.split("=").length > 1) {
                    int id = Integer.parseInt(query.split("=")[1]);
                    if (httpTaskManager.getTasks().containsKey(id)) {
                        httpTaskManager.deleteTask(httpTaskManager.getTask(id));
                        if (httpTaskManager.getTasks().containsKey(id)) {
                            writeResponse(exchange, "Задача с id=" + id + " не удалена", 500);
                            break;
                        }
                        writeResponse(exchange, ("Удалена задача с id=" + id), 201);
                        break;
                    } else {
                        writeResponse(exchange, "Задача с id=" + id + " не найдена", 404);
                        break;
                    }
                } else {
                    writeResponse(exchange, "Bad request", 400);
                    break;
                }
                break;
            default:
                writeResponse(exchange, "Bad request", 400);
                break;
        }

    }

    private Task getTaskFromRequestBody(HttpExchange exchange) throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .create();
        Task task = gson.fromJson(new String(exchange.getRequestBody().readAllBytes(),
                DEFAULT_CHARSET), Task.class);
        return task;
    }

    private void handleGetAllTasks(HttpExchange exchange) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .create();
        List<Task> tasksList = httpTaskManager.getAllTasks();
        Type listType = new TypeToken<List<Task>>() {
        }.getType();
        String listTask = gson.toJson(tasksList, listType);
        try {
            writeResponse(exchange, listTask, 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
