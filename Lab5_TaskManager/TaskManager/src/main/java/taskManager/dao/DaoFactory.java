package taskManager.dao;


/** Factory object for working with database */
public interface DaoFactory<Context> {

    public interface DaoCreator<Context> {
        public GenericDao create(Context context);
    }

    /** Returns a connection to the database */
    public Context getContext() throws PersistException;

    /** Returns an object for managing the persistent state of the object */
    public GenericDao getDao(Context manager, Class dtoClass) throws PersistException;
}
