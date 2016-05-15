package taskManager.postgreSql;

import taskManager.dao.AbstractJDBCDao;
import taskManager.dao.EmptyParamException;
import taskManager.dao.NullPointParameterException;
import taskManager.dao.PersistException;
import taskManager.domain.Task;
import taskManager.domain.User;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class PostgreSqlTaskDao extends AbstractJDBCDao<Task, Integer> {

    private class PersistTask extends Task {
        public void setId(int id) {
            super.setId(id);
        }
    }

    @Override
    public String getSelectQuery() {
        return "SELECT id, name, description, contacts, date, "
                + "highPriority, parentId, userId FROM tu.Task, " + last_insert_table();
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO tu.Task (id, name, description, contacts, date, "
                + "highPriority, parentId, userId) \n" +
                "VALUES (nextval('"+last_insert_table()+"'), ?, ?, ?, ?, ?, ?, ?)";
    }

    public String getCreateQueryForImport() {
        return "INSERT INTO tu.Task (id, name, description, contacts, date, "
                + "highPriority, parentId, userId) \n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE tu.Task \n" +
                "SET name = ?, description  = ?, contacts = ?, date = ?,"
                + " highPriority = ?, parentId = ?, userId = ? \n" +
                "WHERE id = ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM tu.Task WHERE id= ?;";
    }

    @Override
    public Task create(Task object) throws PersistException, NullPointParameterException, EmptyParamException {

        if(object == null){
            throw new NullPointParameterException("Object is null!");
        }
        if(object.getUserId().equals(null)){
            throw new EmptyParamException();
        }

        Task task = new Task();

        task.setName(object.getName());
        task.setDescription(object.getDescription());
        task.setContacts(object.getContacts());
        task.setHighPriority(object.isHighPriority());
        task.setDate(object.getDate());
        task.setParentId(object.getParentId());
        task.setUserId(object.getUserId());

        return persist(task, false);
    }

    public PostgreSqlTaskDao(Connection connection) {
        super(connection);
    }

    protected String last_insert_id(){
        return "tu.task_id_seq.last_value";
    }

    protected String last_insert_table(){
        return "tu.task_id_seq";
    }

    @Override
    protected List<Task> parseResultSet(ResultSet rs) throws PersistException {
        LinkedList<Task> result = new LinkedList<Task>();
        try {
            while (rs.next()) {
                PersistTask task = new PersistTask();
                task.setId(rs.getInt("id"));
                task.setName(rs.getString("name"));
                task.setDescription(rs.getString("description"));
                task.setContacts(rs.getString("contacts"));
                task.setDate(rs.getDate("date"));
                task.setHighPriority(rs.getBoolean("highPriority"));
                task.setParentId(rs.getInt("parentId"));
                if(rs.wasNull()){
                    task.setParentId(null);
                }
                task.setUserId(rs.getInt("userId"));
                if(rs.wasNull()){
                    task.setUserId(null);
                }
                result.add(task);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Task object) throws PersistException {
        try {
            Date sqlDate = convert(object.getDate());
            statement.setString(1, object.getName());
            statement.setString(2, object.getDescription());
            statement.setString(3, object.getContacts());
            statement.setDate(4, sqlDate);
            statement.setBoolean(5, object.isHighPriority());

            Integer parentId = (object.getParentId()== null) ? null : object.getParentId();

            if(parentId == null){
                statement.setNull(6, Types.NULL);
            }
            else{
                statement.setInt(6, parentId.intValue());
            }
            statement.setInt(7, object.getUserId().intValue());
            statement.setInt(8, object.getId().intValue());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Task object,
                                             boolean flagInsertWithId) throws PersistException {
        try {
            Date sqlDate = convert(object.getDate());
            Integer userId = (object.getUserId()== null) ? null : object.getUserId();
            
            Integer parentId = (object.getParentId()== null) ? null : object.getParentId();
            int index = 1;

            if(flagInsertWithId){
                statement.setInt(1, object.getId().intValue());
                index++;
            }
            
            statement.setString(index, object.getName());
            statement.setString(index+1, object.getDescription());
            statement.setString(index+2, object.getContacts());
            statement.setDate(index+3, sqlDate);
            statement.setBoolean(index+4, object.isHighPriority());
            if(parentId == null){
                statement.setObject(index+5, null);
            }
            else{
                statement.setInt(index+5, parentId.intValue());
            }
            statement.setInt(index+6, userId);
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    protected Date convert(java.util.Date date) {
        if (date == null) {
            return null;
        }
        return new Date(date.getTime());
    }

    public List<Task> getByParameters(Integer id, String name, String contacts,
                                      Integer parentId, Integer userId) throws PersistException {
        List<Task> list;
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

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for(int i = 0; i< listParam.size(); i++){
                Object object = listParam.get(i);
                if(object instanceof Integer){
                    statement.setInt(i+1, ((Integer) object).intValue());
                }else{
                    statement.setString(i+1, (String)object );
                }
            }

            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException(e);
        }
        if (list == null || list.size() == 0) {
            throw new PersistException("Record with Parameters not found.");
        }

        return list;
    }
}
