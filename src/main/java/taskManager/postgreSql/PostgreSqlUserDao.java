package taskManager.postgreSql;

import taskManager.dao.AbstractJDBCDao;
import taskManager.dao.EmptyParamException;
import taskManager.dao.NullPointParameterException;
import taskManager.dao.PersistException;
import taskManager.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.*;

public class PostgreSqlUserDao extends AbstractJDBCDao<User, Integer> {

    private class PersistUser extends User {
        public void setId(int id) {
            super.setId(id);
        }
    }

    @Override
    public String getSelectQuery() {
        return "SELECT id, name, password FROM tu.User, " + last_insert_table();
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO tu.User (name, password) \n" +
                "VALUES (?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE tu.User SET name = ?, password = ? WHERE id= ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM tu.User WHERE id= ?;";
    }

    @Override
    public User create(User object) throws PersistException, EmptyParamException, NullPointParameterException {
        if(object == null){
            throw new NullPointParameterException("Object is null!");
        }
        if(object.getName().equals(null)){
            throw new EmptyParamException();
        }
        User g = new User();
        g.setName(object.getName());
        g.setPassword(object.getPassword());
        return persist(g);
    }

    public PostgreSqlUserDao(Connection connection) {
        super(connection);
    }

    protected String last_insert_id(){
        return "tu.user_id_seq.last_value";
    }

    protected String last_insert_table(){
        return "tu.user_id_seq";
    }

    @Override
    protected List<User> parseResultSet(ResultSet rs) throws PersistException {
        LinkedList<User> result = new LinkedList<User>();
        try {
            while (rs.next()) {
                PersistUser user = new PersistUser();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                result.add(user);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, User object) throws PersistException {
        try {
            statement.setString(1, object.getName());
            statement.setString(2, object.getPassword());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, User object) throws PersistException {
        try {
            statement.setString(1, object.getName());
            if(object.getPassword() == null){
                statement.setNull(2, Types.NULL);
            }
            else{
                statement.setString(2, object.getPassword());
            }

            statement.setInt(3, object.getId().intValue());//where id =
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }
}
