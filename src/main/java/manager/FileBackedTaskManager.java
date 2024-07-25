package main.java.manager;

import main.java.tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Long.parseLong;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File fileOfTasks;
    private String path;


    public FileBackedTaskManager(Path paths, Path file) throws ManagerSaveException {

        if ((!paths.toString().isBlank()) || !paths.toString().isEmpty()) {
            path = String.valueOf(paths);
        } else {
            path = "./src/main/java/manager/";
        }
        if (file.toString().isBlank()) {
            file = Paths.get(path, "StorageOfTasks.cvs");
        } else {
            file = Path.of(file.toString());
        }

        if (!Files.isDirectory(paths)) {
            try {
                Files.createDirectory(paths);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ManagerSaveException("Произошла ошибка при создании директории.");
            }
        }
        if (!Files.exists(file)) {
            try {
                Files.createFile(file);
                this.fileOfTasks = new File(String.valueOf(file));
            } catch (IOException e) {
                e.printStackTrace();
                throw new ManagerSaveException("Произошла ошибка при создании директории.");
            }
        } else {
            this.fileOfTasks = new File(String.valueOf(file));
        }
    }

    public FileBackedTaskManager(File file) throws ManagerSaveException {
        this.fileOfTasks = file;
    }


//    public static void main(String[] args) throws ManagerSaveException, IOException {
//        File file = new File("./src/main/java/manager/StorageOfTasks.cvs");
//        Task task = new Task("taskname1", "taskDesc1", Status.NEW);
//        FileBackedTaskManager manager = new FileBackedTaskManager(Paths.get(""), file.toPath());
//        manager.createTask(task);
//        manager.createTask(new Task("taskname2", "taskDesc2", Status.DONE));
//        manager.createEpicTask(new EpicTask("epicname1", "epicDesc1"));
//        manager.createSubTask(new SubTask(3, "subtaskName1", "subtaskDesc1", Status.NEW));
//        manager.createSubTask(new SubTask(3, "subtaskName2", "subtaskDesc2", Status.NEW));
//        manager.getTask(1);
//        manager.getEpicTask(3);
//        FileBackedTaskManager manager2 = new FileBackedTaskManager(Paths.get(""), file.toPath());
//        manager2 = manager2.loadFromFile(file);
//        manager2.createTask(new Task("taskname2", "taskDesc2", Status.NEW));
//        System.out.println(manager2.getTasks());
//        System.out.println("\n ___________________________ \n");
//        manager2.deleteTask(manager2.getTask(1));
//
//        System.out.println(manager2.getTasks());
//        System.out.println("\n ___________________________ \n");
//        FileBackedTaskManager manager3 = new FileBackedTaskManager(Paths.get(""), file.toPath());
//        manager3 = manager3.loadFromFile(file);
//        manager3.createTask(task);
//
//        System.out.println(manager3.getTasks());
//    }

    public FileBackedTaskManager loadFromFile(File file) throws IOException, ManagerSaveException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        FileReader reader = new FileReader(file, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(reader);
        List<String> list = new ArrayList<>();
        while (bufferedReader.ready()) {
            String line = bufferedReader.readLine();
            list.add(line);
        }
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).isBlank()) {
                break;
            }
            Task taskFromFile = taskFromString(list.get(i));
            fileBackedTaskManager.setTask(taskFromFile);
            fileBackedTaskManager.setIdInManager(taskFromFile);
            fileBackedTaskManager.addToSortedTasks(taskFromFile);
        }
        List<Integer> historyId = new ArrayList<>();
        String[] history = list.get(list.size() - 1).split(",");
        for (int j = 0; j < history.length; j++) {
            if (!history[j].isBlank()) {
                historyId.add(Integer.parseInt(history[j]));
            }
        }
        for (Integer id : historyId) {
            if (fileBackedTaskManager.getTasks().containsKey(id)) {
                fileBackedTaskManager.getTask(id);
            } else if (fileBackedTaskManager.getSubTasks().containsKey(id)) {
                fileBackedTaskManager.getSubTask(id);
            } else if (fileBackedTaskManager.getEpicTasks().containsKey(id)) {
                fileBackedTaskManager.getEpicTask(id);
            }
        }
        bufferedReader.close();
        return fileBackedTaskManager;
    }

    private Task taskFromString(String string) throws ManagerSaveException, NumberFormatException {
        String[] task = string.split(",");
        Task taskToReturn = null;
        int id = 1;
        Status status;
        try {
            Integer.parseInt(task[0]);
        } catch (NumberFormatException e) {
            throw new ManagerSaveException("Некорректный id задачи в файле");
        }
        if (string.isBlank() && string.isEmpty()) {
            throw new ManagerSaveException("Строка пустая, задача не перенесена");
        }
        try {
            switch (TaskType.valueOf(task[1])) {
                case TASK:
                    status = Status.valueOf(task[3]);
                    taskToReturn = new Task(task[2], task[4], status);
                    taskToReturn.setId(Integer.parseInt(task[0]));
                    taskToReturn.setTaskType(TaskType.valueOf(task[1]));
                    setIdInManager(taskToReturn);
                    if (task[5].equals("null")) {
                        taskToReturn.setStartTime(null);
                    } else {
                        taskToReturn.setStartTime(LocalDateTime.parse(task[5], DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
                    }
                    if (task[6].equals("null")) {
                        taskToReturn.setDuration(null);
                    } else {
                        taskToReturn.setDuration(Duration.ofMinutes(parseLong(task[6])));
                    }
                    return taskToReturn;
                case EPIC_TASK:
                    status = Status.valueOf(task[3]);
                    taskToReturn = new EpicTask(task[2], task[4]);
                    taskToReturn.setId(Integer.parseInt(task[0]));
                    taskToReturn.setStatus(status);
                    taskToReturn.setTaskType(TaskType.valueOf(task[1]));
                    ((EpicTask) taskToReturn).setEpicTime();
                    ((EpicTask) taskToReturn).setDuration();
                    setIdInManager(taskToReturn);
                    return taskToReturn;
                case SUB_TASK:
                    status = Status.valueOf(task[3]);
                    taskToReturn = new SubTask(Integer.parseInt(task[5]), task[2], task[4], status);
                    taskToReturn.setId(Integer.parseInt(task[0]));
                    taskToReturn.setTaskType(TaskType.valueOf(task[1]));
                    if (task[6].equals("null")) {
                        taskToReturn.setStartTime(null);
                    } else {
                        taskToReturn.setStartTime(LocalDateTime.parse(task[6], DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
                    }
                    if (task[7].equals("null")) {
                        taskToReturn.setDuration(null);
                    } else {
                        taskToReturn.setDuration(Duration.ofMinutes(parseLong(task[7])));
                    }
                    setIdInManager(taskToReturn);
                    return taskToReturn;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new NumberFormatException(e.getMessage());
        }
        return taskToReturn;
    }

    private void setIdInManager(Task task) {
        if (task.getId() >= getId()) {
            setId(task.getId() + 1);
        }
    }

    public void save() throws ManagerSaveException {
        if (fileOfTasks != null) {
            try (FileWriter writer = new FileWriter(fileOfTasks, StandardCharsets.UTF_8)) {
                writer.append("id,type,name,status,description,epic" + "\n");
                for (Task task : getTasks().values()) {
                    writer.write(taskToString(task) + "\n");
                }
                for (Task task : getEpicTasks().values()) {
                    writer.write(taskToString(task) + "\n");
                }
                for (Task task : getSubTasks().values()) {
                    writer.write(taskToString(task) + "\n");
                }
                writer.append(" " + "\n");
                for (Task history : getHistory()) {
                    Integer id1 = history.getId();
                    writer.append(String.valueOf(id1)).append(",");
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new ManagerSaveException("Не удалось сохранить задачи в файл");
            }
        }
    }

    private String taskToString(Task task) throws IllegalArgumentException, ManagerSaveException {
        try {
            if (task.getTaskType() != null) {
                String time;
                String duration;
                switch (task.getTaskType()) {
                    case TaskType.TASK:
                        if (task.getStartTime() != null) {
                            time = task.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                        } else time = "null";
                        if (task.getDuration() != null) {
                            duration = String.valueOf(task.getDuration().toMinutes());
                        } else duration = "null";
                        return String.format("%d,%S,%s,%S,%s,%s,%s", task.getId(), task.getTaskType(), task.getName(), task.getStatus(), task.getDescription(), time, duration);
                    case TaskType.EPIC_TASK:
                        EpicTask epicTask = (EpicTask) task;
                        if (epicTask.getStartTime() != null) {
                            time = epicTask.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                        } else time = "null";
                        if (epicTask.getDuration() != null) {
                            duration = String.valueOf(epicTask.getDuration().toMinutes());
                        } else duration = "null";
                        return String.format("%d,%S,%s,%S,%s,%s,%s", epicTask.getId(), task.getTaskType(), epicTask.getName(), epicTask.getStatus(), epicTask.getDescription(), time, duration);
                    case TaskType.SUB_TASK:
                        SubTask subTask = (SubTask) task;
                        if (subTask.getStartTime() != null) {
                            time = subTask.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                        } else time = "null";
                        if (subTask.getDuration() != null) {
                            duration = String.valueOf(subTask.getDuration().toMinutes());
                        } else duration = "null";
                        return String.format("%d,%S,%s,%S,%s,%s,%s,%s", subTask.getId(), subTask.getTaskType(), subTask.getName(), subTask.getStatus(), subTask.getDescription(), subTask.getEpicId(), time, duration);
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
        return null;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpicTask(EpicTask epicTask) {
        super.createEpicTask(epicTask);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void deleteTask(Task task) {
        super.deleteTask(task);
        save();
    }

    @Override
    public void deleteEpicTask(EpicTask epicTask) {
        super.deleteEpicTask(epicTask);
        save();
    }

    @Override
    public void deleteSubTask(SubTask subTask) {
        super.deleteSubTask(subTask);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpicTasks() {
        super.deleteAllEpicTasks();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteAllSubTasksInEpic(int epicId) {
        super.deleteAllSubTasksInEpic(epicId);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        super.updateEpicTask(epicTask);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;

    }

    @Override
    public EpicTask getEpicTask(int id) {
        EpicTask task = super.getEpicTask(id);
        save();
        return task;

    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask task = super.getSubTask(id);
        save();
        return task;

    }

    @Override
    public List<SubTask> getAllSubTasksByEpicId(int epicId) {
        List<SubTask> tasks = super.getAllSubTasksByEpicId(epicId);
        save();
        return tasks;
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = super.getAllTasks();
        save();
        return tasks;
    }

    @Override
    public List<EpicTask> getAllEpicTasks() {
        List<EpicTask> tasks = super.getAllEpicTasks();
        save();
        return tasks;
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        List<SubTask> tasks = super.getAllSubTasks();
        save();
        return tasks;
    }
}