package taskManager;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import task.Task;
import user.User;

/**
 * Интерфейс класса для работы с журналом задач
 * @author Карасев С.В.
 * @version 1.0
 */
public interface TaskManager {
    
    public void saveTasks() throws IOException;
    public void loadTasks() throws IOException, ClassNotFoundException;
    public void setTask(long id, String name, String description, String contacts,
            Calendar date, boolean finished, boolean highPriority,
            boolean workOnTask, User user);
    
    public void addTask(Task task);
    public void removeTask(long id);
    public Task getTask(long id);
    public List<Task> getWorkOnTasks();
    public List<Task> getCompletedTasks();
    public List<Task> getFinishedTasks();
    public List<Task> getHighPrioritedTasks();
    public List<Task> getNormalPrioritedTasks();
    public Collection<Task> getCollectionTask();
    public void addUser(User user);
    public User getUser(long id);
    public boolean containsUser(String name);
    public User searchUser(String name);
    public Collection<User> getCollectionUser();
    public Collection<Task> getCollectionTasksForUser(long idUser);
}
