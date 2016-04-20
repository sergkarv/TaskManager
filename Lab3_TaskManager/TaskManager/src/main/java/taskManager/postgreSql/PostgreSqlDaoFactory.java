package taskManager.postgreSql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import taskManager.dao.DaoFactory;
import taskManager.dao.GenericDao;
import taskManager.dao.PersistException;
import taskManager.domain.Task;
import taskManager.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


@Component
public class PostgreSqlDaoFactory implements DaoFactory<Connection> {
    //jdbc:postgresql://localhost:5432/db
    private String user = "root";//Логин пользователя
    private String password = "root";//Пароль пользователя
    private String url = "jdbc:postgresql://localhost:5432/db";//URL адрес
    //private String driver = "org.apache.derby.jdbc.EmbeddedDriver";//имя драйвера
    private String driver = "org.postgresql.Driver";//имя драйвера
    //org.apache.derby.jdbc.ClientDriver
    private Map<Class, DaoCreator> creators;

    @Autowired
    private DataSource dataSource;

    public Connection getContext() throws PersistException {
        Connection connection = null;
        try {
            if(dataSource == null){
                try {
                    Class.forName("org.postgresql.Driver");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                connection = DriverManager.getConnection(url, user, password);
            }
            else{
                connection = dataSource.getConnection();
            }

        } catch (SQLException e) {
            throw new PersistException(e);
        }

        return  connection;
    }

    @Override
    public GenericDao getDao(Connection connection, Class dtoClass) throws PersistException {
        DaoCreator creator = creators.get(dtoClass);
        if (creator == null) {
            throw new PersistException("Dao object for " + dtoClass + " not found.");
        }
        return creator.create(connection);
    }

    public PostgreSqlDaoFactory() {
        //надеемся на то, что драйвер подхватится в getContext

        creators = new HashMap<Class, DaoCreator>();
        creators.put(User.class, new DaoCreator<Connection>() {
            @Override
            public GenericDao create(Connection connection) {
                return new PostgreSqlUserDao(connection);
            }
        });
        creators.put(Task.class, new DaoCreator<Connection>() {
            @Override
            public GenericDao create(Connection connection) {
                return new PostgreSqlTaskDao(connection);
            }
        });
    }
}
