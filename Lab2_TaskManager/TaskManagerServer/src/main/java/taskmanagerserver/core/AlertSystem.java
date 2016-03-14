
package taskmanagerserver.core;

import java.util.List;
import task.Task;

/**
 * Интерфейс системы оповещения о выполненных заданиях. 
 * @author Головин, Карасев
 * @version 1.0
 */
public interface AlertSystem 
{
    /** Подписывает слушателя на данный экземпляр класса.
     * @param listener Слушатель
     */
    public void addListener(Listener listener);
     /** Отписывает слушателя от данного экземпляра класса
     * @param listener Слушатель
     */
    public void removeListener(Listener listener);
    /** Оповещает слушателя об выполненных заданиях
     * @param tasks Массив выполненных заданий
     */
    public void notifyListener(List<Task> tasks);
    /**
     * Проверка списка задач на наличие выполненных.
     * В случае, если такие задачи обнаружены, оповещаются слушатели, подписанные на систему оповещения.
     */
    public void check();
}
