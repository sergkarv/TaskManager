package taskManager.postgreSql;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;
import taskManager.dao.AbstractJDBCDao;
import taskManager.dao.PersistException;
import taskManager.domain.User;
import java.util.*;

@Transactional
public class PostgreSqlUserDao extends AbstractJDBCDao<User, Integer> {

    @Override
    public String getSelectQuery() {
        return "FROM User";
    }

    @Override
    public User persist(User object, boolean useSelfId) throws PersistException{
        session.flush();
        if(object == null){
            throw new NullPointerException("Object is null!");
        }
        if(object.getName().equals(null)||object.getName().equals("")){
            throw new IllegalArgumentException("Object task has null name!");
        }

        User user = null;
        if(useSelfId){
            SQLQuery query = session.createSQLQuery("insert into tu.user values(:id, :name, :pass)");
            query.setParameter("id", object.getId());
            query.setParameter("name",object.getName());
            query.setParameter("pass", object.getPassword());
            session.getTransaction().begin();
            int result = query.executeUpdate();
            session.getTransaction().commit();
            if(result == 0) throw new PersistException("0 rows inserted");
            user = (User) session.get(User.class, object.getId());
        }
        else{
            user = super.persist(object, useSelfId);
        }

        return user;
    }

    public PostgreSqlUserDao(Session session) {
        super(session);
    }

    @Override
    public User getByPK(Integer key) throws PersistException {
        session.flush();
        User user = (User)session.get(User.class, key);
        if(user == null) throw new PersistException("User with ID does not exist!");
        return user;
    }

    @Override
    public void update(User object) throws PersistException {
        session.flush();
        User user = (User)session.get(User.class, object.getId());
        if(user == null) throw new PersistException("User is not exist!");
        user.setName(object.getName());
        user.setPassword(object.getPassword());
        session.getTransaction().begin();
        session.save(user);
        session.getTransaction().commit();
    }

    @Override
    public void delete(Integer key) throws PersistException {
        session.flush();
        User user = (User)session.get(User.class, key);
        if(user == null) throw new PersistException("User is not exist!");
        session.getTransaction().begin();
        session.delete(user);
        session.getTransaction().commit();
    }

    public List<User> getByParameters(Integer id, String name, String pass) throws PersistException {
        session.flush();
        List<User> list=null;
        String sql = getSelectQuery();
        StringBuffer s = new StringBuffer(sql);
        if(id!= null || name!=null||pass!=null) s.append(" WHERE");
        //s.append(" WHERE id = :idUser and name = :nameUser and password = :passUser");
        HashMap<String, Object> mapParam = new HashMap<>();
        if(id != null)  {
            mapParam.put("idUser", id);
            s.append(" id = :idUser");
        }
        if(name != null){
            mapParam.put("nameUser", name);
            s.append((id != null)? " and name = :nameUser": " name = :nameUser");
        }
        if(pass != null)  {
            mapParam.put("passUser", pass);
            s.append((name != null || id != null)? " and password = :passUser": " password = :passUser");
        }



        sql = s.toString();
        Query query  = session.createQuery(sql);

        for(String key : mapParam.keySet()){
            query.setParameter(key, mapParam.get(key));
        }

        list = query.list();

        if (list == null || list.size() == 0) {
            throw new PersistException("Record with Parameters not found.");
        }

        return list;
    }
}
