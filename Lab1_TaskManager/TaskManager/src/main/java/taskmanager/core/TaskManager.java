package taskmanager.core;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;

/**
 * Интерфейс класса для работы с журналом задач
 * @author Карасев
 * @version 1.0
 */
public interface TaskManager {
    
    public void saveTasks() throws IOException;
    public void loadTasks() throws IOException, ClassNotFoundException;
    public void setTask(long id, String name, String description, String contacts,
            Calendar date, boolean finished, boolean highPriority, String soundPath,
            boolean workOnTask);
    
    public void addTask(Task task);
    public void removeTask(long id);
    public Task getTask(long id);
    public Task[] getWorkOnTasks();
    public Task[] getCompletedTasks();
    public Task[] getFinishedTasks();
    public Task[] getHighPrioritedTasks();
    public Task[] getNormalPrioritedTasks();
    public Collection<Task> getCollection();
}
