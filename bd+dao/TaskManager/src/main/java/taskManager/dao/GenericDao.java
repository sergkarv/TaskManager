package taskManager.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Унифицированный интерфейс управления персистентным состоянием объектов
 * @param <T> тип объекта персистенции
 * @param <PK> тип первичного ключа
 */
public interface GenericDao<T extends Identified<PK>, PK extends Serializable> {

    /** Создает новую запись; требуются входные данные для корректной инициализации*/
    public T create(T object) throws PersistException, NullPointParameterException, EmptyParamException;

    /** Создает новую запись, соответствующую объекту object */
    public T persist(T object)  throws PersistException;

    /** Возвращает объект соответствующий записи с первичным ключом key или null */
    public T getByPK(PK key) throws PersistException;

    /** Сохраняет состояние объекта group в базе данных */
    public void update(T object) throws PersistException;

    /** Удаляет запись об объекте из базы данных */
    public void delete(T object) throws PersistException;

    /** Возвращает список объектов соответствующих всем записям в базе данных */
    public List<T> getAll() throws PersistException;
}
