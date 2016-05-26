package taskManager.postgreSql;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import taskManager.dao.DaoFactory;
import taskManager.dao.GenericDao;
import taskManager.dao.PersistException;
import taskManager.domain.Task;
import taskManager.domain.User;
import taskManager.utils.HibernateUtil;

import java.util.HashMap;
import java.util.Map;


@Repository
@Transactional //need to update\delete queries. Don't forget <tx:annotation-driven/>
public class PostgreSqlDaoFactory implements DaoFactory<Session> {

    private Map<Class, DaoCreator> creators;

    public Session getContext() throws PersistException {
        return HibernateUtil.getSessionFactory().openSession();
    }

    @Override
    public GenericDao getDao(Session session, Class dtoClass) throws PersistException {
        DaoCreator creator = creators.get(dtoClass);
        if (creator == null) {
            throw new PersistException("Dao object for " + dtoClass + " not found.");
        }
        return creator.create(session);
    }

    public PostgreSqlDaoFactory() {


        creators = new HashMap<Class, DaoCreator>();
        creators.put(User.class, new DaoCreator<Session>() {
            @Override
            public GenericDao create(Session session) {
                return new PostgreSqlUserDao(session);
            }
        });
        creators.put(Task.class, new DaoCreator<Session>() {
            @Override
            public GenericDao create(Session session) {
                return new PostgreSqlTaskDao(session);
            }
        });
    }
}
