package main.java.manager;

public class ManagerSaveException extends Exception {
    public ManagerSaveException(String message) {
        super(message);
        System.out.println(message);
    }
}
