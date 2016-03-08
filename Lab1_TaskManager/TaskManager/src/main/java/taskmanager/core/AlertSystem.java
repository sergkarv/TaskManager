
package taskmanager.core;

/**
 * Интерфейс системы оповещения о выполненных заданиях. 
 * @author Карасев С.В.
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
    public void notifyListener(Task[] tasks);
    /**
     * Проверка списка задач на наличие выполненных.
     * В случае, если такие задачи обнаружены, оповещаются слушатели, подписанные на систему оповещения.
     */
    public void check();
}
