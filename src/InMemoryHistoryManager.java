import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{
    private ArrayList<Task> history;

    public InMemoryHistoryManager(){
        history = new ArrayList<>();
    }
    @Override
    public void add(Task task) {
        if (history.size() > 9){
            history.removeFirst();
            history.add(task);
        }
        else{
            history.add(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory()  {
        return history;
    }
}
