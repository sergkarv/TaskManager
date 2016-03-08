/**
 * Интерфейс слушателя.
 */
package taskmanager.core;

public interface Listener {
    /**
     * Получает массив задач 
     * @param tasks Массив задач
     */
    public void update(Task[] tasks);
    
}
