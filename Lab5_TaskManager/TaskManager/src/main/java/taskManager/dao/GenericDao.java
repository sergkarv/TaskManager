package taskManager.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Unified interface to manage persistent state of objects
 * @param <T> type of object persistence
 * @param <PK> type of primary key
 */
public interface GenericDao<T , PK extends Serializable> {

    /** Creates a new record appropriate to the item object */
    public T persist(T object, boolean useSelfId)  throws PersistException, NullPointParameterException, EmptyParamException;
    /** Returns an object corresponding to a record with primary key "key" or null */
    public T getByPK(PK key) throws PersistException;

    /** Saves the state of the object in the database */
    public void update(T object) throws PersistException;

    /** Deletes the record object from the database */
    public void delete(PK key) throws PersistException;

    /** Returns a list of objects appropriate all records in the database */
    public List<T> getAll() throws PersistException;
}
