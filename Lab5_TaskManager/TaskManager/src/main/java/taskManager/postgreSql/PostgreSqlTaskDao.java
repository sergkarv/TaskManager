package taskManager.postgreSql;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;
import taskManager.dao.AbstractJDBCDao;
import taskManager.dao.EmptyParamException;
import taskManager.dao.NullPointParameterException;
import taskManager.dao.PersistException;
import taskManager.domain.Task;

import javax.persistence.EntityManager;
import java.sql.*;
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

    public List<Task> getByParameters(Integer id, String name, String contacts,
                                      Integer parentId, Integer userId) throws PersistException {
        List<Task> list=null;
        String sql = getSelectQuery();
        StringBuffer s = new StringBuffer(sql);
        s.append(" WHERE id = ? and name = ? and contacts = ? and parentId = ? and userId = ?");
        LinkedList listParam = new LinkedList();
        if(id != null)  listParam.add(id);
        if(name != null)  listParam.add(name);
        if(contacts != null)  listParam.add(contacts);
        if(parentId != null)  listParam.add(parentId);
        if(userId != null)  listParam.add(userId);

        if(id == null){
            int index = s.lastIndexOf("id");
            s.delete(index, index+11);
        }
        if(name == null){
            int index = s.lastIndexOf("name");
            s.delete(index, index+13);
        }
        if(contacts == null){
            int index = s.lastIndexOf("contacts");
            s.delete(index, index+17);
        }
        if(parentId == null){
            int index = s.lastIndexOf("parentId");
            s.delete(index, index+17);
        }
        if(userId == null){
            int index = s.lastIndexOf("userId");
            s.delete(index-4, index+10);
        }

        sql = s.toString();
        Query query = session.createQuery(sql);

        for (int i = 0; i < listParam.size(); i++) {
            Object object = listParam.get(i);
            if (object instanceof Integer) {
                query.setParameter(i + 1, ((Integer) object).intValue());
            } else {
                query.setParameter(i + 1, (String) object);
            }
        }

        list = query.list();

        if (list == null || list.size() == 0) {
            throw new PersistException("Record with Parameters not found.");
        }

        return list;
    }
}
