package main.java.adapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import main.java.tasks.EpicTask;
import main.java.tasks.Status;
import main.java.tasks.SubTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EpicTaskAdapter extends TypeAdapter<EpicTask> {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public void write(JsonWriter out, EpicTask task) throws java.io.IOException {
        out.beginObject();
        out.name("Id").value(task.getId());
        out.name("Status").value(task.getStatus().toString());
        out.name("Name").value(task.getName());
        out.name("Description").value(task.getDescription());
        if (task.getStartTime() == null) {
            out.name("StartTime").nullValue();
            out.name("Duration").nullValue();
        } else {
            out.name("StartTime").value(task.getStartTime().format(dateTimeFormatter));
            out.name("Duration").value(task.getDuration().toMinutes());
        }
        for (SubTask subTask : task.getSubTasks()) {
            out.name("SubTasks").beginArray();
            out.beginObject();
            out.name("Id").value(subTask.getId());
            out.name("Name").value(subTask.getName());
            out.name("Description").value(subTask.getDescription());
            out.name("Status").value(subTask.getStatus().toString());
            if (subTask.getStartTime() != null) {
                out.name("StartTime").value(subTask.getStartTime().format(dateTimeFormatter));
                out.name("Duration").value(subTask.getDuration().toMinutes());

            } else {
                out.name("StartTime").nullValue();
                out.name("Duration").nullValue();
            }
            out.endObject();
            out.endArray();
        }
        out.endObject();
    }

    @Override
    public EpicTask read(JsonReader in) throws java.io.IOException {
        JsonElement jsonElement = JsonParser.parseReader(in);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        EpicTask task;
        int epicId;
        if (jsonObject.get("StartTime").isJsonNull()) {
            task = new EpicTask(
                    jsonObject.get("Name").getAsString(),
                    jsonObject.get("Description").getAsString());
            if (jsonObject.get("Id").isJsonNull()) {
                task.setId(0);
                epicId = 0;
            } else {
                epicId = jsonObject.get("Id").getAsInt();
                task.setId(jsonObject.get("Id").getAsInt());
            }
            if (jsonObject.get("SubTasks").isJsonArray()) {
                for (JsonElement subTaskElement : jsonObject.get("SubTasks").getAsJsonArray()) {
                    JsonObject subTaskObject = subTaskElement.getAsJsonObject();
                    SubTask subTask = new SubTask(
                            epicId,
                            subTaskObject.get("Name").getAsString(),
                            subTaskObject.get("Description").getAsString(),
                            Status.valueOf(subTaskObject.get("Status").getAsString()));
                    subTask.setId(subTaskObject.get("Id").getAsInt());
                    task.addSubtask(subTask);
                }
            }
        } else {
            task = new EpicTask(
                    jsonObject.get("Name").getAsString(),
                    jsonObject.get("Description").getAsString());
            task.setId(jsonObject.get("Id").getAsInt());
            if (jsonObject.get("Id").isJsonNull()) {
                task.setId(0);
            } else {
                task.setId(jsonObject.get("Id").getAsInt());
            }
            if (jsonObject.get("SubTasks").isJsonArray()) {
                for (JsonElement subTaskElement : jsonObject.get("SubTasks").getAsJsonArray()) {
                    JsonObject subTaskObject = subTaskElement.getAsJsonObject();
                    LocalDateTime time;
                    Duration duration;
                    if (subTaskObject.get("StartTime").isJsonNull()) {
                        time = null;
                    } else {
                        time = LocalDateTime.parse(subTaskObject.get("StartTime").getAsString(), dateTimeFormatter);
                    }
                    if (subTaskObject.get("Duration").isJsonNull()) {
                        duration = null;
                    } else {
                        duration = Duration.ofMinutes(subTaskObject.get("Duration").getAsInt());
                    }
                    task.addSubtask(new SubTask(
                            jsonObject.get("Id").getAsInt(),
                            subTaskObject.get("Name").getAsString(),
                            subTaskObject.get("Description").getAsString(),
                            Status.valueOf(subTaskObject.get("Status").getAsString()),
                            time,
                            duration));
                }
            }
        }
        return task;
    }
}
