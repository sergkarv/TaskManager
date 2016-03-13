/**
 * Система оповещения о выполненных заданиях.
 * В отдельном потоке регулярно проверяет список задач.
 * В случае нахождения выполненных задач оповещает подписанного слушателя.
 * Возможна ссылка лишь на один экземпляр данного класса.
 * @author - Карасев
 * @version 1.0
 */

package taskmanagerserver.core;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import task.Task;

public class ConcreteAlertSystem implements AlertSystem, Runnable {

    /** Слушатели */
    ArrayList<Listener> arrayListeners;
    /** Объект для работы с журналом задач */
    TaskManager manager;
    /** Ссылка на экземпляр класса ConcreteAlertSystem */
    private static ConcreteAlertSystem instance = new ConcreteAlertSystem();

    /** Получает ссылку на объект для работы с журналом задач */
    private ConcreteAlertSystem() {
        manager = ConcreteTaskManager.getInstance();
        arrayListeners= new ArrayList<>();
    }
    
    /** Возвращает ссылку на объект системы оповещения
     * @return Ссылка на экземпляр данного класса
     */
    public static ConcreteAlertSystem getInstance(){
        return instance;
    }

    /** Подписывает слушателя на данный экземпляр класса.
     * @param listener Слушатель
     */
    @Override
    public void addListener(Listener listener) {
        arrayListeners.add(listener);
    }

    /** Отписывает слушателя от данного экземпляра класса
     * @param listener Слушатель
     */
    @Override
    public void removeListener(Listener listener) {
        arrayListeners.remove(listener);
    }

    /** Оповещает слушателя об выполненных заданиях
     * @param tasks Массив выполненных заданий
     */
    @Override
    public void notifyListener(Task[] tasks) {
        
        //ConnectionClass.addIndexTasks(tasks);
        for(Listener listener: arrayListeners){
            listener.update(tasks);
        }        
    }

    /** Проверяет список заданий, оповещает слушателя при необходимости */
    @Override
    public void check() {
        Task[] tasks =  manager.getCompletedTasks();
        if (tasks != null) {
            notifyListener(tasks);
        }
    }

    /**
     * Вызывает проверку списка не чаще 2 раз в секунду.
     * @exception ex Поток прерван другим потоком в момент ожидания
     */
    @Override
    public void run() {
        try {
            while(true){
                Thread.currentThread().sleep(500);
                check();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ConcreteAlertSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
