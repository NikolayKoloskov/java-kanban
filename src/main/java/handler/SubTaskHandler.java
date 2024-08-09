package main.java.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import main.java.adapter.SubTaskAdapter;
import main.java.manager.HttpTaskManager;
import main.java.manager.ManagerSaveException;
import main.java.manager.ManagerSortedSaveException;
import main.java.tasks.SubTask;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubTaskHandler extends DefaultHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;


    public SubTaskHandler(HttpTaskManager httpTaskManager) {
        super(httpTaskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().toString();
        String[] pathPart = path.split("/");
        SubTask subTask;
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
                .create();
        String query = "";
        try {
            query = exchange.getRequestURI().getQuery();
        } catch (Exception e) {
            query = "";
        }
        switch (method) {
            case "GET":
                if (pathPart[1].equals("subtasks") && pathPart.length == 2) {
                    if (httpTaskManager.getSubTasks().isEmpty()) {
                        writeResponse(exchange, "Спикок подзадач пуст", 200);
                        break;
                    }
                    handleAllSubTasks(exchange);
                    break;
                } else if (pathPart[1].equals("subtasks")
                        & pathPart[2].contains("subtask") & query != null & query.split("=").length > 1) {
                    int id = Integer.parseInt(query.split("=")[1]);
                    if (httpTaskManager.getSubTasks().isEmpty()) {
                        writeResponse(exchange, "Список подзадач пуст", 200);
                        break;
                    }
                    writeResponse(exchange, gson.toJson(httpTaskManager.getSubTask(id)), 200);
                    break;
                } else if (pathPart[2].contains("id=")) {
                    int id = Integer.parseInt(path.split("=")[1]);
                    if (!httpTaskManager.getSubTasks().containsKey(id)) {
                        writeResponse(exchange, "Подзадачи с id " + id + " нет.", 404);
                        break;
                    }
                    try {
                        writeResponse(exchange, gson.toJson(httpTaskManager.getSubTask(id)), 200);
                    } catch (ManagerSaveException e) {
                        writeResponse(exchange, "Ошибка при получении подзадачи с id " + id, 500);
                        e.printStackTrace();
                        break;
                    }
                    break;
                } else {
                    writeResponse(exchange, "Bad request", 400);
                    break;
                }
            case "POST":
                subTask = getSubTaskFromRequestBody(exchange);
                if (pathPart[1].equals("subtasks")) {
                    if (pathPart.length == 3 && query != null & query.split("=").length > 1) {
                        if (httpTaskManager.getSubTasks().containsKey(subTask.getId())) {
                            try {
                                httpTaskManager.updateSubTask(subTask);
                                writeResponse(exchange, "Задача сохранена", 201);
                                break;
                            } catch (ManagerSortedSaveException e) {
                                writeResponse(exchange, e.getMessage(), 406);
                                break;
                            }
                        } else {
                            try {
                                httpTaskManager.createSubTask(subTask);
                            } catch (ManagerSaveException e) {
                                writeResponse(exchange, e.getMessage(), 406);
                                break;
                            }
                        }
                        if (subTask.equals(httpTaskManager.getTask(subTask.getId()))) {
                            writeResponse(exchange, "Задача сохранена", 201);
                            break;
                        }
                        break;
                    } else if (pathPart.length == 2) {
                        if (query != null) {
                            subTask = getSubTaskFromRequestBody(exchange);
                            httpTaskManager.updateSubTask(subTask);
                            String jsonTask = gson.toJson(subTask);
                            writeResponse(exchange, jsonTask, 201);
                            break;
                        }
                    }
                } else {
                    writeResponse(exchange, "Bad request", 400);
                    break;
                }
                break;
            case "DELETE":
                subTask = getSubTaskFromRequestBody(exchange);
                if (pathPart.length == 2) {
                    httpTaskManager.deleteAllSubTasks();
                    writeResponse(exchange, "Удалены все задачи", 201);
                } else if (pathPart.length > 2 & query != null & query.split("=").length > 1) {
                    int id = Integer.parseInt(query.split("=")[1]);
                    if (httpTaskManager.getSubTasks().containsKey(id)) {
                        httpTaskManager.deleteSubTask(httpTaskManager.getSubTask(id));
                        if (httpTaskManager.getSubTasks().containsKey(id)) {
                            writeResponse(exchange, "Задача с id=" + id + " не удалена", 500);
                            break;
                        }
                        writeResponse(exchange, ("Удалена задача с id= " + id), 201);
                        break;
                    } else {
                        writeResponse(exchange, "Задача с id= " + id + " не найдена", 404);
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


    private void handleAllSubTasks(HttpExchange exchange) throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
                .create();
        List<SubTask> tasksList = httpTaskManager.getAllSubTasks();
        Type listType = new TypeToken<List<SubTask>>() {
        }.getType();
        String listTask = gson.toJson(tasksList, listType);
        try {
            writeResponse(exchange, listTask, 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SubTask getSubTaskFromRequestBody(HttpExchange exchange) throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
                .create();
        SubTask task = gson.fromJson(new String(exchange.getRequestBody().readAllBytes(),
                DEFAULT_CHARSET), SubTask.class);
        return task;
    }

}

