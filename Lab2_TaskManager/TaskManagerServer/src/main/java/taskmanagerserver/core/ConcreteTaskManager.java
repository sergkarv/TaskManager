/**
 * Класс для работы с журналом задач.
 * Разрешает доступ к методам журнала задач.
 * Содержит ряд методов для системы оповещения и GUI.
 * Возможна ссылка лишь на один экземпляр данного класса.
 * @author Карасёв С.В.
 * @version 1.0
 */
package taskmanagerserver.core;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import task.Task;
import user.User;

public class ConcreteTaskManager implements TaskManager {
    
    /** Журнал задач */
    private HashMap<Long ,Task> tasks;
    private ArrayList<User> users;
    private final String pathTaskList = "data/tasklist.dat";
    
    /** Ссылка на объект данного класса ConcreteTaskManager */
    private static ConcreteTaskManager instance = new ConcreteTaskManager();

    /** При создании класса пробует загрузить журнал задач из файла */
    private ConcreteTaskManager() {
        tasks= new HashMap<>();
        users = new ArrayList<>();
        try {
            loadTasks();
            initUsers();
        } catch (IOException | ClassNotFoundException ex) {
        }
    }

    /** Возвращает ссылку на объект данного класса ConcreteTaskManager */
    public static TaskManager getInstance() {
        return instance;
    }
    
    private void initUsers() throws IOException, ClassNotFoundException {
        Collection<Task> list = tasks.values();
        for(Task task : list){
            if(task.getUser() != null && !users.contains(task.getUser())){
                users.add(task.getUser());
            }
        }
    }

    /**
     * Сохраняет журнал задач в файл
     * @throws IOException Ошибка при записи журнала в файл
     */
    @Override
    public void saveTasks() throws IOException {
        FileOutputStream fos = new FileOutputStream(pathTaskList);
        try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(tasks);
            oos.flush();
        }
    }

    /**
     * Загружает журнал задач из файла
     * @throws IOException  Ошибка при чтении журнала из файла
     * @throws ClassNotFoundException Ошибка при десериализации журнала
     */
    @Override
    public void loadTasks() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(pathTaskList);
        ObjectInputStream oin = new ObjectInputStream(fis);
        tasks = (HashMap<Long, Task>) oin.readObject();
    }

    
    @Override
    public void setTask(long id, String name, String description, String contacts,
            Calendar date, boolean finished, boolean highPriority, String soundPath,
            boolean workOnTask, User user) {
        tasks.get(id).setName(name);
        tasks.get(id).setDescription(description);
        tasks.get(id).setContacts(contacts);
        tasks.get(id).setDate(date);
        tasks.get(id).setFinished(finished);
        tasks.get(id).setHighPriority(highPriority);
        tasks.get(id).setSoundFileName(soundPath);
        tasks.get(id).setWorkOnTask(workOnTask);
        tasks.get(id).setUser(user);
    }

    /**
     * Добавляет задачу в журнал задач
     * @param task Новая задача
     */
    @Override
    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }
    
    @Override
    public void removeTask(long id) {
        tasks.remove(id);
    }

    @Override
    public Task getTask(long id) {
        return tasks.get(id);
    }
    
    @Override
    public Collection<Task> getCollectionTask(){
        return Collections.unmodifiableCollection(tasks.values());
    }
    
    @Override
    public List<Task> getWorkOnTasks(){
        ArrayList<Task> list = new ArrayList<>();
        for (Task task : this.getCollectionTask()) {
            if (task.getWorkOnTask()) {                
                list.add(task);
            }
        }
        if (list.size() == 0) {
            return null;
        }
        return Collections.unmodifiableList(list);
    }
    
    /**
     * Возвращает массив задач время которых подошло
     * помечает задачи удовлетворящие условию как активные
     * @return Массив выполненных задач
     */
    @Override
    public List<Task> getCompletedTasks()
    {
        ArrayList<Task> list = new ArrayList<>();
        for (Task task: this.getCollectionTask()) {
            if (task.isComplete() && !task.isFinished()
                    && !task.getWorkOnTask()) {
                task.setWorkOnTask(true);
                list.add(task);
            }
        }
        if (list.size() == 0) {
            return null;
        }
        return Collections.unmodifiableList(list);        
    }

    /**
     * Возвращает массив завершённых задач
     * @return Массив завершённых задач
     */
    @Override
    public List<Task> getFinishedTasks(){
        ArrayList<Task> list = new ArrayList<>();
        for (Task task: this.getCollectionTask()) {
            if (task.isFinished()) {
                list.add(task);
            }
        }
        
        if (list.size() == 0) {
            return null;
        }

        return Collections.unmodifiableList(list);
    }

    /**
     * Возвращает массив задач с высоким приоритетом
     * @return Массив задач с высоким приоритетом
     */
    @Override
    public List<Task> getHighPrioritedTasks() {
        ArrayList<Task> list = new ArrayList<>();
        for (Task task: this.getCollectionTask()) {
            if (task.isHighPriority()) {
                list.add(task);
            }
        }
        
        if (list.size() == 0) {
            return null;
        }
        
        return Collections.unmodifiableList(list);
    }

    /**
     * Возвращает массив задач с обычным приоритетом
     * @return Массив задач с обычным приоритетом
     */
    @Override
    public List<Task> getNormalPrioritedTasks() {
        ArrayList<Task> list = new ArrayList<>();
        for (Task task: this.getCollectionTask()) {
            if (!task.isHighPriority()) {
                list.add(task);
            }
        }
        
        if (list.size() == 0) {
            return null;
        }
        
        return Collections.unmodifiableList(list);
    }
    
    @Override
    public boolean containsUser(String name){
        if(name == null) return false;
        for(User user : users){
            if(user.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void addUser(User user) {
        if(!this.containsUser(user.getName())){
            users.add(user);
        }
    }

    @Override
    public User getUser(long id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User searchUser(String name) {
        if(name != null){
            for (User user : users) {
                if (user.getName().equals(name)) {
                    return user;
                }
            }
        }
        
        return null;
    }

    @Override
    public Collection<User> getCollectionUser() {
        return Collections.unmodifiableCollection(users);
    }

    @Override
    public Collection<Task> getCollectionTasksForUser(long idUser) {
        Collection<Task> collection = this.getCollectionTask();
        ArrayList<Task> list = new ArrayList<>();
        for(Task task : collection){
            if(task.getUser().getId() == idUser){
                list.add(task);
            }
        }
        return Collections.unmodifiableCollection(list);
    }

}
