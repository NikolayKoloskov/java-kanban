package main.java.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import main.java.adapter.EpicTaskAdapter;
import main.java.adapter.SubTaskAdapter;
import main.java.manager.HttpTaskManager;
import main.java.manager.ManagerSaveException;
import main.java.manager.ManagerSortedSaveException;
import main.java.tasks.EpicTask;
import main.java.tasks.SubTask;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends DefaultHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;


    public EpicHandler(HttpTaskManager httpTaskManager) {
        super(httpTaskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().toString();
        String[] pathPart = path.split("/");
        EpicTask epicTask;
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(EpicTask.class, new EpicTaskAdapter())
                .create();
        String query = "";
        try {
            query = exchange.getRequestURI().getQuery();
        } catch (Exception e) {
            query = "";
        }
        switch (method) {
            case "GET":
                if (pathPart[1].equals("epics") && pathPart.length == 2) {
                    if (httpTaskManager.getEpicTasks().isEmpty()) {
                        writeResponse(exchange, "Спикок эпиков пуст", 200);
                        break;
                    }
                    handleAllEpicTasks(exchange);
                    break;
                } else if (pathPart[1].equals("epics")
                        & pathPart[2].contains("epic") & query != null & query.split("=").length > 1) {
                    int id = Integer.parseInt(query.split("=")[1]);
                    if (httpTaskManager.getEpicTasks().isEmpty()) {
                        writeResponse(exchange, "Список эпиков пуст", 200);
                        break;
                    }
                    if (!httpTaskManager.getEpicTasks().containsKey(id)) {
                        writeResponse(exchange, "Эпика с id " + id + " нет.", 404);
                        break;
                    }
                    try {
                        writeResponse(exchange, gson.toJson(httpTaskManager.getEpicTask(id)), 200);
                        break;
                    } catch (Exception e) {
                        writeResponse(exchange, "Ошибка при получении эпика с id " + id, 500);
                        e.printStackTrace();
                        break;
                    }
                } else if (pathPart[2].contains("id=")) {
                    int id = Integer.parseInt(path.split("=")[1]);
                    if (!httpTaskManager.getEpicTasks().containsKey(id)) {
                        writeResponse(exchange, "Эпика с id " + id + " нет.", 404);
                        break;
                    }
                    try {
                        writeResponse(exchange, gson.toJson(httpTaskManager.getEpicTask(id)), 200);
                    } catch (ManagerSaveException e) {
                        writeResponse(exchange, "Ошибка при получении эпика с id " + id, 500);
                        e.printStackTrace();
                        break;
                    }
                    break;
                } else {
                    writeResponse(exchange, "Bad request", 400);
                    break;
                }
            case "POST":
                epicTask = getEpicTaskFromRequestBody(exchange);
                if (pathPart[1].equals("epics")) {
                    if (pathPart.length == 3 && query != null & query.split("=").length > 1) {
                        if (httpTaskManager.getEpicTasks().containsKey(epicTask.getId())) {
                            try {
                                httpTaskManager.updateEpicTask(epicTask);
                                writeResponse(exchange, "Эпик сохранен", 201);
                                break;
                            } catch (ManagerSortedSaveException e) {
                                writeResponse(exchange, e.getMessage(), 406);
                                break;
                            }
                        } else {
                            try {
                                httpTaskManager.createEpicTask(epicTask);
                            } catch (ManagerSaveException e) {
                                writeResponse(exchange, e.getMessage(), 406);
                                break;
                            }
                        }
                        if (epicTask.equals(httpTaskManager.getEpicTask(epicTask.getId()))) {
                            writeResponse(exchange, "Эпик сохранен", 201);
                            break;
                        }
                        break;
                    } else if (pathPart.length == 2) {
                        if (query != null) {
                            epicTask = getEpicTaskFromRequestBody(exchange);
                            httpTaskManager.updateEpicTask(epicTask);
                            String jsonTask = gson.toJson(epicTask);
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
                epicTask = getEpicTaskFromRequestBody(exchange);
                if (pathPart.length == 2) {
                    httpTaskManager.deleteAllSubTasks();
                    writeResponse(exchange, "Удалены все эпики и их подзадачи", 201);
                } else if (pathPart.length > 2 & query != null & query.split("=").length > 1) {
                    int id = Integer.parseInt(query.split("=")[1]);
                    if (httpTaskManager.getSubTasks().containsKey(id)) {
                        httpTaskManager.deleteSubTask(httpTaskManager.getSubTask(id));
                        if (httpTaskManager.getSubTasks().containsKey(id)) {
                            writeResponse(exchange, "Эпик с id=" + id + " не удалена", 500);
                            break;
                        }
                        writeResponse(exchange, ("Удален эпик с id= " + id), 201);
                        break;
                    } else {
                        writeResponse(exchange, "Эпик с id= " + id + " не найден", 404);
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

    private void handleAllEpicTasks(HttpExchange exchange) throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(EpicTask.class, new EpicTaskAdapter())
                .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
                .create();
        List<EpicTask> tasksList = httpTaskManager.getAllEpicTasks();
        Type listType = new TypeToken<List<EpicTask>>() {
        }.getType();
        String listTask = gson.toJson(tasksList, listType);
        try {
            writeResponse(exchange, listTask, 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private EpicTask getEpicTaskFromRequestBody(HttpExchange exchange) throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(EpicTask.class, new EpicTaskAdapter())
                .create();
        EpicTask task = gson.fromJson(new String(exchange.getRequestBody().readAllBytes(),
                DEFAULT_CHARSET), EpicTask.class);
        return task;
    }
}
