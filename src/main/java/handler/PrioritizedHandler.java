package main.java.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.java.manager.HttpTaskManager;
import main.java.manager.HttpTaskServer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PrioritizedHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final HttpTaskManager httpTaskManager;


    public PrioritizedHandler(HttpTaskManager httpTaskManager) {
        this.httpTaskManager = httpTaskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().toString();
        String[] pathSplit = path.split("/");
        if (method.equals("GET") && pathSplit[1].equals("prioritized")) {
            writeResponse(exchange, HttpTaskServer.getGson().toJson(httpTaskManager.getPrioritizedTasks()), 200);
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
}
