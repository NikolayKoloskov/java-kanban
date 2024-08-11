package main.java.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.java.adapter.DurationAdapter;
import main.java.adapter.LocalDateTimeTypeAdapter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HttpTaskManager extends FileBackedTaskManager {
    private final Gson gson;

    public HttpTaskManager() throws IOException, InterruptedException {
        super(Paths.get(""), Path.of(""));
        DurationAdapter durationAdapter = new DurationAdapter();
        LocalDateTimeTypeAdapter timeTypeAdapter = new LocalDateTimeTypeAdapter();
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(DurationAdapter.class, durationAdapter)
                .registerTypeAdapter(LocalDateTimeTypeAdapter.class, timeTypeAdapter)
                .create();
    }

    public Gson getGson() {
        return gson;
    }
}
