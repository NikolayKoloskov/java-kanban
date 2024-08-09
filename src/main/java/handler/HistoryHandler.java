package main.java.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.java.adapter.TaskAdapter;
import main.java.manager.HttpTaskManager;
import main.java.tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HistoryHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final HttpTaskManager httpTaskManager;


    public HistoryHandler(HttpTaskManager httpTaskManager) {
        this.httpTaskManager = httpTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().toString();
        String[] pathSplit = path.split("/");
        if (method.equals("GET") && pathSplit[1].equals("history")) {
            handleHistory(exchange);
        } else {
            writeResponse(exchange, "Метод не допустимый: " + method, 405);
        }
    }

    protected void writeResponse(HttpExchange exchange,
                                 String responseString,
                                 int responseCode) throws IOException {

        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(responseCode, responseString.isEmpty() ? -1 : responseString.getBytes(DEFAULT_CHARSET).length);
        try (OutputStream os = exchange.getResponseBody()) {
            if (!responseString.isEmpty()) {
                os.write(responseString.getBytes(DEFAULT_CHARSET));
            }
        }
    }

    private void handleHistory(HttpExchange exchange) throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .create();
        List<Task> history = httpTaskManager.getHistory();
        Type listType = new TypeToken<List<Task>>() {
        }.getType();
        String listHistory = gson.toJson(history, listType);
        try {
            writeResponse(exchange, listHistory, 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
