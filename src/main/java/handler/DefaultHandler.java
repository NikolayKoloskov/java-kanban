package main.java.handler;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.java.manager.HttpTaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class DefaultHandler implements HttpHandler {
    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected final HttpTaskManager httpTaskManager;

    protected DefaultHandler(HttpTaskManager httpTaskManager) {
        this.httpTaskManager = httpTaskManager;
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

    protected String getRequestBody(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
    }


}