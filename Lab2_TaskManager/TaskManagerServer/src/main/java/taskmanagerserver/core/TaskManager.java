package taskmanagerserver.core;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import task.Task;
import user.User;

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
            boolean workOnTask, User user);
    
    public void addTask(Task task);
    public void removeTask(long id);
    public Task getTask(long id);
    public Task[] getWorkOnTasks();
    public Task[] getCompletedTasks();
    public Task[] getFinishedTasks();
    public Task[] getHighPrioritedTasks();
    public Task[] getNormalPrioritedTasks();
    public Collection<Task> getCollectionTask();
    public void addUser(User user);
    public User getUser(long id);
    public boolean containsUser(String name);
    public User searchUser(String name);
    public Collection<User> getCollectionUser();
    public Collection<Task> getCollectionTasksForUser(long idUser);
}
