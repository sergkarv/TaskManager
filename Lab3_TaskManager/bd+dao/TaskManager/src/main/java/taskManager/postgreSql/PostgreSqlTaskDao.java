package taskManager.postgreSql;

import taskManager.dao.AbstractJDBCDao;
import taskManager.dao.EmptyParamException;
import taskManager.dao.NullPointParameterException;
import taskManager.dao.PersistException;
import taskManager.domain.Task;
import taskManager.domain.User;

import java.sql.*;
import java.util.HashMap;
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
        return "INSERT INTO tu.Task (name, description, contacts, date, "
                + "highPriority, parentId, userId) \n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?);";
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

        return persist(task);
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
    protected void prepareStatementForInsert(PreparedStatement statement, Task object) throws PersistException {
        try {
            Date sqlDate = convert(object.getDate());
            Integer userId = (object.getUserId()== null) ? null : object.getUserId();
            
            Integer parentId = (object.getParentId()== null) ? null : object.getParentId();
            
            statement.setString(1, object.getName());
            statement.setString(2, object.getDescription());
            statement.setString(3, object.getContacts());
            statement.setDate(4, sqlDate);
            statement.setBoolean(5, object.isHighPriority());
            if(parentId == null){
                statement.setObject(6, null);
            }
            else{
                statement.setInt(6, parentId.intValue());
            }
            statement.setInt(7, userId);
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
}
