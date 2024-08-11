package main.java.adapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import main.java.tasks.Status;
import main.java.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskAdapter extends TypeAdapter<Task> {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public void write(JsonWriter out, Task task) throws java.io.IOException {
        out.beginObject();
        out.name("Id").value(task.getId());
        out.name("Name").value(task.getName());
        out.name("Description").value(task.getDescription());
        out.name("Status").value(task.getStatus().toString());
        if (task.getStartTime() == null) {
            out.name("StartTime").nullValue();
        } else {
            out.name("StartTime").value(task.getStartTime().format(dateTimeFormatter));
        }
        if (task.getDuration() == null) {
            out.name("Duration").nullValue();
        } else {
            out.name("Duration").value(task.getDuration().toMinutes());
        }
        out.endObject();
    }

    @Override
    public Task read(JsonReader in) throws java.io.IOException {
        JsonElement jsonElement = JsonParser.parseReader(in);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Task task;
        if (jsonObject.get("StartTime").isJsonNull()) {
            task = new Task(
                    jsonObject.get("Name").getAsString(),
                    jsonObject.get("Description").getAsString(),
                    Status.valueOf(jsonObject.get("Status").getAsString())
            );
            if (jsonObject.get("Id").isJsonNull()) {
                task.setId(0);
            } else {
                task.setId(jsonObject.get("Id").getAsInt());
            }
        } else {
            task = new Task(
                    jsonObject.get("Name").getAsString(),
                    jsonObject.get("Description").getAsString(),
                    Status.valueOf(jsonObject.get("Status").getAsString()),
                    LocalDateTime.parse(jsonObject.get("StartTime").getAsString(), dateTimeFormatter),
                    Duration.ofMinutes(jsonObject.get("Duration").getAsInt())
            );
            task.setId(jsonObject.get("Id").getAsInt());
        }
        return task;
    }
}
