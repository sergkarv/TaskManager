/**
 * Интерфейс слушателя.
 */
package connection;

public interface Listener {
    /**
     * Получает массив задач 
     * @param tasks Массив задач
     */
    public void update(Object value);
    
}
