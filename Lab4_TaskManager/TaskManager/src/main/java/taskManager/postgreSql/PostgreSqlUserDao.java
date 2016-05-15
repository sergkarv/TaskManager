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
        return "INSERT INTO tu.User (id, name, password) \n" +
                "VALUES (nextval('"+last_insert_table()+"'), ?, ?);";
    }

    @Override
    public String getCreateQueryForImport() {
        return "INSERT INTO tu.User (id, name, password) \n" +
                "VALUES ( ?, ?, ?);";
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
        return persist(g, false);
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
    protected void prepareStatementForInsert(PreparedStatement statement, User object,
                                             boolean flagInsertWithId) throws PersistException {
        try {

            int index = 1;
            if(flagInsertWithId){
                statement.setInt(1, object.getId().intValue());
                index++;
            }
            statement.setString(index, object.getName());
            statement.setString(index+1, object.getPassword());
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

    public List<User> getByParameters(Integer id, String name, String pass) throws PersistException {
        List<User> list;
        String sql = getSelectQuery();
        StringBuffer s = new StringBuffer(sql);
        s.append(" WHERE id = ? and name = ? and password = ?");
        LinkedList listParam = new LinkedList();
        if(id != null)  listParam.add(id);
        if(name != null)  listParam.add(name);
        if(pass != null)  listParam.add(pass);

        if(id == null){
            int index = s.lastIndexOf("id");
            s.delete(index, index+11);
        }
        if(name == null){
            int index = s.lastIndexOf("name");
            s.delete(index, index+13);
        }
        if(pass == null){
            int index = s.lastIndexOf("password");
            s.delete(index-4, index+12);
        }

        sql = s.toString();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for(int i = 0; i< listParam.size(); i++){
                Object object = listParam.get(i);
                if(object instanceof Integer){
                    statement.setInt(1, id);
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
