/**
 * Интерфейс слушателя.
 */
package taskmanagerserver.core;

public interface Listener {
    /**
     * Получает массив задач 
     * @param tasks Массив задач
     */
    public void update(Object value);
    
}
