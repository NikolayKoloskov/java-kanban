package main.java.manager;

import main.java.tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File fileOfTasks;
    private String path = "./src/main/java/manager/";


    public FileBackedTaskManager() throws ManagerSaveException {
        Path paths = Paths.get(path);
        Path file = Paths.get(path, "StorageOfTasks.cvs");
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

    public FileBackedTaskManager(File file) throws IOException, ManagerSaveException {
        this.fileOfTasks = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException, ManagerSaveException {
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

    private static Task taskFromString(String string) throws ManagerSaveException, NumberFormatException {
        String[] task = string.split(",");
        Task taskToReturn = null;
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
            switch (task[1]) {
                case "TASK":
                    if (task[3].equals("DONE")) {
                        status = Status.DONE;
                    } else if (task[3].equals("NEW")) {
                        status = Status.NEW;
                    } else if (task[3].equals("IN_PROGRESS")) {
                        status = Status.IN_PROGRESS;
                    } else {
                        throw new ManagerSaveException("Статус задачи не корректный в файле");
                    }
                    taskToReturn = new Task(task[2], task[4], status);
                    taskToReturn.setId(Integer.parseInt(task[0]));
                    return taskToReturn;
                case "EPIC_TASK":
                    if (task[3].equals("DONE")) {
                        status = Status.DONE;
                    } else if (task[3].equals("NEW")) {
                        status = Status.NEW;
                    } else if (task[3].equals("IN_PROGRESS")) {
                        status = Status.IN_PROGRESS;
                    } else {
                        throw new ManagerSaveException("Статус задачи не корректный в файле");
                    }
                    taskToReturn = new EpicTask(task[2], task[4]);
                    taskToReturn.setId(Integer.parseInt(task[0]));
                    taskToReturn.setStatus(status);
                    return taskToReturn;
                case "SUB_TASK":
                    if (task[3].equals("DONE")) {
                        status = Status.DONE;
                    } else if (task[3].equals("NEW")) {
                        status = Status.NEW;
                    } else if (task[3].equals("IN_PROGRESS")) {
                        status = Status.IN_PROGRESS;
                    } else {
                        throw new ManagerSaveException("Статус задачи не корректный в файле");
                    }
                    taskToReturn = new SubTask(Integer.parseInt(task[5]), task[2], task[4], status);
                    taskToReturn.setId(Integer.parseInt(task[0]));
                    return taskToReturn;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new NumberFormatException(e.getMessage());
        }
        return taskToReturn;
    }

    public static void main(String[] args) throws ManagerSaveException, IOException {
        File file = new File("./src/main/java/manager/StorageOfTasks.cvs");

        FileBackedTaskManager manager = new FileBackedTaskManager();
        manager.createTask(new Task("taskname1", "taskDesc1", Status.NEW));
        manager.createEpicTask(new EpicTask("epicname1", "epicDesc1"));
        manager.createSubTask(new SubTask(2, "subtaskName1", "subtaskDesc1", Status.NEW));
        manager.createSubTask(new SubTask(2, "subtaskName2", "subtaskDesc2", Status.NEW));

        FileBackedTaskManager manager2 = FileBackedTaskManager.loadFromFile(file);
        System.out.println(manager2.getTasks());
        System.out.println(manager2.getEpicTasks());
        System.out.println(manager2.getTasks());
    }

    private String taskToString(Task task) throws IllegalArgumentException {
        TaskTypes type;
        try {
            if (getTask(task.getId()) != null) {
                type = TaskTypes.TASK;
                return String.format("%d,%S,%s,%S,%s", task.getId(), type, task.getName(),
                        task.getStatus(), task.getDescription());
            } else if (getEpicTask(task.getId()) != null) {
                type = TaskTypes.EPIC_TASK;
                EpicTask epicTask = (EpicTask) task;
                return String.format("%d,%S,%s,%S,%s", epicTask.getId(), type, epicTask.getName(),
                        epicTask.getStatus(), epicTask.getDescription());
            } else if (getSubTask(task.getId()) != null) {
                type = TaskTypes.SUB_TASK;
                SubTask subTask = (SubTask) task;
                return String.format("%d,%S,%s,%S,%s,%s", subTask.getId(), type, subTask.getName(),
                        subTask.getStatus(), subTask.getDescription(), subTask.getEpicId());
            }
            return null;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
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

    @Override
    public void createTask(Task task) throws ManagerSaveException {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpicTask(EpicTask epicTask) throws ManagerSaveException {
        super.createEpicTask(epicTask);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) throws ManagerSaveException {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void deleteTask(Task task) throws ManagerSaveException {
        super.deleteTask(task);
        save();
    }

    @Override
    public void deleteEpicTask(EpicTask epicTask) throws ManagerSaveException {
        super.deleteEpicTask(epicTask);
        save();
    }

    @Override
    public void deleteSubTask(SubTask subTask) throws ManagerSaveException {
        super.deleteSubTask(subTask);
        save();
    }

    @Override
    public void deleteAllTasks() throws ManagerSaveException {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpicTasks() throws ManagerSaveException {
        super.deleteAllEpicTasks();
        save();
    }

    @Override
    public void deleteAllSubTasks() throws ManagerSaveException {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteAllSubTasksInEpic(int epicId) throws ManagerSaveException {
        super.deleteAllSubTasksInEpic(epicId);
        save();
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) throws ManagerSaveException {
        super.updateEpicTask(epicTask);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) throws ManagerSaveException {
        super.updateSubTask(subTask);
        save();
    }
}