package taskManager.postgreSql;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;
import taskManager.dao.*;
import taskManager.domain.Task;
import taskManager.domain.User;

import javax.persistence.EntityManager;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Transactional
public class PostgreSqlTaskDao extends AbstractJDBCDao<Task, Integer> {

    @Override
    public String getSelectQuery() {
        return " FROM Task";
    }

    public PostgreSqlTaskDao(Session session) {
        super(session);
    }

    @Override
    public Task getByPK(Integer key) throws PersistException {
        Task task = (Task)session.get(Task.class, key);
        if(task == null) throw new PersistException("Task with ID does not exist!");
        return task;
    }

    @Override
    public void update(Task object) throws PersistException {
        Task task = (Task)session.get(Task.class, object.getId());
        if(task == null) throw new PersistException("Task does not exist!");
        task.setName(object.getName());
        task.setDescription(object.getDescription());
        task.setContacts(object.getContacts());
        task.setDate(object.getDate());
        task.setHighPriority(object.isHighPriority());
        task.setParent(object.getParent());
        task.setUser(object.getUser());
        session.getTransaction().begin();
        session.save(task);
        session.getTransaction().commit();
    }

    @Override
    public void delete(Integer key) throws PersistException {
        Task task = (Task)session.get(Task.class, key);
        if(task == null) throw new PersistException("Task with ID does not exist!");
        session.getTransaction().begin();
        session.delete(task);
        session.getTransaction().commit();
    }

    protected Date convert(java.util.Date date) {
        if (date == null) {
            return null;
        }
        return new Date(date.getTime());
    }

    @Override
    public Task persist(Task object, boolean useSelfId) throws PersistException, EmptyParamException, NullPointParameterException {
        if(object == null){
            throw new NullPointParameterException("Object is null!");
        }
        if(object.getName().equals(null)){
            throw new EmptyParamException();
        }

        Task task = null;
        if(useSelfId){
            SQLQuery query = session.createSQLQuery("insert into tu.task values(:id, :name, :desc, " +
                    ":contacts, :date, :high, :parent, :user)");
            query.setParameter("id", object.getId());
            query.setParameter("name",object.getName());
            query.setParameter("desc", object.getDescription());
            query.setParameter("contacts", object.getContacts());
            query.setParameter("date", object.getDate());
            query.setParameter("high", object.isHighPriority());
            query.setParameter("parent", (object.getParent()!=null)? object.getParent().getId(): null);
            query.setParameter("user", object.getUser());
            session.getTransaction().begin();
            int result = query.executeUpdate();
            session.getTransaction().commit();
            if(result == 0) throw new PersistException("0 rows inserted");
            task = (Task) session.get(Task.class, object.getId());
        }
        else{
            task = super.persist(object, useSelfId);
        }

        return task;
    }

    public List<Task> getByParameters( Integer id, String name, String contacts,
                                      Task parent, User user) throws PersistException {
        List<Task> list=null;
        //String sql = "select id, name, description, contacts, date, highpriority, parentId, userId from tu.task";
        String sql = getSelectQuery();
        StringBuffer s = new StringBuffer(sql);
        if(id!= null || name!=null||contacts!=null||parent!=null||user!=null) s.append(" WHERE");
        //s.append(" WHERE id = :idTask and name = :nameTask and contacts = :contactsTask
        // and parentId = :parentTask and userId = :userTask");
        HashMap<String, Object> mapParam = new HashMap<>();
        if(id != null)  {
            mapParam.put("idTask", id);
            s.append(" id = :idTask");
        }
        if(name != null)  {
            mapParam.put("nameTask", name);
            s.append((id != null)? " and name = :nameTask": " name = :nameTask");
        }
        if(contacts != null)  {
            mapParam.put("contactsTask", contacts);
            s.append((id != null || name!=null)? " and contacts = :contactsTask": " contacts = :contactsTask");
        }
        if(parent != null)  {
            mapParam.put("parentTask", parent);
            s.append((id != null || name!=null || contacts!=null)? " and parent = :parentTask": " parent = :parentTask");
        }
        if(user != null)  {
            mapParam.put("userTask", user);
            s.append((id != null || name!=null || contacts!=null || parent!=null)? " and user = :userTask":
                    " user = :userTask");
        }

        sql = s.toString();
        Query query = session.createQuery(sql);

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