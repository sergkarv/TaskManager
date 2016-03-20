/**
 * Задача. Хранит в себе информацию о задаче, её параметры.
 *
 * @author Карасев С.В.
 * @version 1.0
 */
package task;

import java.io.Serializable;
import java.util.Calendar;
import user.User;

public class Task implements Serializable {
    
    private long id;

    /**
     * Название задачи
     */
    private String name;
    /**
     * Описание задачи
     */
    private String description;
    /**
     * Контактные данные
     */
    private String contacts;
    /**
     * Дата/время оповещения о задаче
     */
    private Calendar date;
    /**
     * Флаг завершённости задачи
     */
    private boolean finished;
    /**
     * Флаг высокого приоритета задачи
     */
    private boolean highPriority;
    /**
     * Дата создании задачи 
     */
    private Calendar dateCreatedTask;
    /**
     * Флаг того что над задачей работают и она в окне Просмотра активных задач
     */
    private boolean workOnTask;
    
    private User user;

    /**Создание задачи с незаполненными данными */
    public Task() {
        name = null;
        description = null;
        contacts = null;
        date = null;
        finished = false;
        highPriority = false;
        dateCreatedTask = Calendar.getInstance();
        workOnTask = false;
        id = dateCreatedTask.getTimeInMillis();
        user = null;
    }

    /** Инициализация данных задачи 
     * @param name Название задачи
     * @param description  Описание задачи
     * @param contacts  Контактные данные
     * @param date Дата/время оповещения
     * @param finished Завершённость задачи
     * @param highPriority Важность(повышенный приоритет) задачи
     * @param sound Путь к звуковому файлу для оповещения
     * @param user Пользователь данной задачи
     */
    public Task(String name, String description, String contacts,
            Calendar date, boolean finished, boolean highPriority, String sound,
            User user) {
        this.name = name;
        this.description = description;
        this.contacts = contacts;
        this.date = date;
        this.finished = finished;
        this.highPriority = highPriority;
        dateCreatedTask = Calendar.getInstance();
        workOnTask = false;
        id = dateCreatedTask.getTimeInMillis();
        this.user = user;
    }

     /** Установка флага того что задача в окне Просмотра активных задач и
      * её не надо добавлять снова в это окно
      * @param flag задачу обрабатывают
      */ 
    public void setWorkOnTask(boolean flag) {
        workOnTask = flag;
    }

    /**Возврат флага обработки задачи
     * @return  флаг
     */
    public boolean getWorkOnTask() {
        return workOnTask;
    }

    /**Проверяет подошло ли время для выполнения данной задачи
     * и проверяет просроченность данной задачи
     * @return состояние активности задачи
     */
    public boolean isComplete() {
        return date.after(dateCreatedTask) && date.before(Calendar.getInstance());
    }

    /** Задать название задачи
     * @param name Название задачи
     */
    public void setName(String name) {
        this.name = name;
    }

    /** Получить название задачи
     * @return Название задачи
     */
    public String getName() {
        return name;
    }

    /** Получить строковое представление задачи
     * @return Строковое представление задачи
     */
    @Override
    public String toString() {
        return getName();
    }

    /** Задать описание задачи
     * @param value Описание задачи
     */
    public void setDescription(String value) {
        description = value;
    }

    /** Получить описание задачи
     * @return Описание задачи
     */
    public String getDescription() {
        return description;
    }

    /** Задать дату/время выполнения задачи
     * @param date Дата/время выполнения
     */
    public void setDate(Calendar date) {
        this.date = date;
    }

    /** Получить дату/время выполнения задачи
     * @return Дата/время выполнения
     */
    public Calendar getDate() {
        return date;
    }

    /** Задать контактную информацию
     * @param s Контактная информация
     */
    public void setContacts(String s) {
        contacts = s;
    }

    /** Получить контактную информацию
     * @return Контактная информация
     */
    public String getContacts() {
        return contacts;
    }

    /** Получить завершённость задачи
     * @return Завершённость задачи
     */
    public boolean isFinished() {
        return finished;
    }

    /** Задать флаг завершённости задачи
     * @param value Завершённость задачи
     */
    public void setFinished(boolean value) {
        finished = value;
    }

    /** Задать флаг высокого приоритета
     * @param value Высокоприоритетность задачи
     */
    public void setHighPriority(boolean value) {
        highPriority = value;
    }

    /** Получить высокоприоритетность задачи
     * @return Высокоприоритетность
     */
    public boolean isHighPriority() {
        return highPriority;
    }

    @Override
    public int hashCode() {
        int t = (int)id ^ (int)(id >> 32); 
        return t;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Task other = (Task) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public long getId() {
        return id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
    
}
