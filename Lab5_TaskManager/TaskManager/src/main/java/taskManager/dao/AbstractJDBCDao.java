package taskManager.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import java.util.List;

/**
 * Абстрактный класс предоставляющий базовую реализацию CRUD операций с использованием JDBC.
 *
 * @param <T>  тип объекта персистенции
 * @param <PK> тип первичного ключа
 */
public abstract class AbstractJDBCDao<T , PK extends Integer> implements GenericDao<T, PK> {

    protected Session session;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public AbstractJDBCDao(Session session) {
        this.session = session;
    }

    public abstract String getSelectQuery();

    @Override
    public T persist(T object, boolean useSelfId) throws PersistException, NullPointParameterException, EmptyParamException {
        session.flush();
        session.getTransaction().begin();
        session.save(object);
        session.getTransaction().commit();
        return object;
    }

    @Override
    public List<T> getAll() throws PersistException {
        session.flush();
        List<T> list;
        String sql = getSelectQuery();
        Query query = session.createQuery(sql);
        list = query.list();
        if (list == null || list.size() == 0) {
            throw new PersistException("Table is empty or no access to the table ");
        }
        return list;
    }

}
