/**
 * Класс для работы с журналом задач.
 * Разрешает доступ к методам журнала задач.
 * Содержит ряд методов для системы оповещения и GUI.
 * Возможна ссылка лишь на один экземпляр данного класса.
 * @author Карасёв С.В.
 * @version 1.0
 */
package taskmanager.core;

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

public class ConcreteTaskManager implements TaskManager {
    
    /** Журнал задач */
    private HashMap<Long ,Task> tasks;
    private final String pathTaskList = "data/tasklist.dat";
    
    /** Ссылка на объект данного класса ConcreteTaskManager */
    private static ConcreteTaskManager instance = new ConcreteTaskManager();

    /** При создании класса пробует загрузить журнал задач из файла */
    private ConcreteTaskManager() {
        tasks= new HashMap<>();
        try {
            loadTasks();
        } catch (IOException | ClassNotFoundException ex) {
        }
    }

    /** Возвращает ссылку на объект данного класса ConcreteTaskManager */
    public static TaskManager getInstance() {
        return instance;
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
            boolean workOnTask) {
        tasks.get(id).setName(name);
        tasks.get(id).setDescription(description);
        tasks.get(id).setContacts(contacts);
        tasks.get(id).setDate(date);
        tasks.get(id).setFinished(finished);
        tasks.get(id).setHighPriority(highPriority);
        tasks.get(id).setSoundFileName(soundPath);
        tasks.get(id).setWorkOnTask(workOnTask);
        
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
    public Collection<Task> getCollection(){
        return Collections.unmodifiableCollection(tasks.values());
    }
    
    @Override
    public Task[] getWorkOnTasks(){
        ArrayList<Task> list = new ArrayList<>();
        for (Task task : this.getCollection()) {
            if (task.getWorkOnTask()) {                
                list.add(task);
            }
        }
        if (list.toArray().length == 0) {
            return null;
        }
        return this.toArray(list);
    }
    
    /**
     * Возвращает массив выполненных задач
     * @return Массив выполненных задач
     */
    @Override
    public Task[] getCompletedTasks()
    {
        ArrayList<Task> list = new ArrayList<>();
        for (Task task: this.getCollection()) {
            if (task.isComplete() && !task.isFinished()
                    && !task.getWorkOnTask()) {
                task.setWorkOnTask(true);
                list.add(task);
            }
        }
        if (list.toArray().length == 0) {
            return null;
        }
        return this.toArray(list);        
    }

    /**
     * Превращает List<Task> в массив задач Task[]
     * @param list List<Task>, который требуется превратить в массив
     * @return Массив задач
     */
    private Task[] toArray(List<Task> list) {
        Task[] array = new Task[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    /**
     * Возвращает массив завершённых задач
     * @return Массив завершённых задач
     */
    @Override
    public Task[] getFinishedTasks(){
        ArrayList<Task> list = new ArrayList<>();
        for (Task task: this.getCollection()) {
            if (task.isFinished()) {
                list.add(task);
            }
        }
        
        if (list.toArray().length == 0) {
            return null;
        }

        return this.toArray(list);
    }

    /**
     * Возвращает массив задач с высоким приоритетом
     * @return Массив задач с высоким приоритетом
     */
    @Override
    public Task[] getHighPrioritedTasks() {
        ArrayList<Task> list = new ArrayList<>();
        for (Task task: this.getCollection()) {
            if (task.isHighPriority()) {
                list.add(task);
            }
        }
        
        if (list.toArray().length == 0) {
            return null;
        }
        
        return this.toArray(list);
    }

    /**
     * Возвращает массив задач с обычным приоритетом
     * @return Массив задач с обычным приоритетом
     */
    @Override
    public Task[] getNormalPrioritedTasks() {
        ArrayList<Task> list = new ArrayList<>();
        for (Task task: this.getCollection()) {
            if (!task.isHighPriority()) {
                list.add(task);
            }
        }
        
        if (list.toArray().length == 0) {
            return null;
        }
        
        return this.toArray(list);
    }
}
