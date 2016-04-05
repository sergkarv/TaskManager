package taskManager.postgreSql;

import taskManager.dao.DaoFactory;
import taskManager.dao.GenericDao;
import taskManager.dao.PersistException;
import taskManager.domain.Task;
import taskManager.domain.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PostgreSqlDaoFactory implements DaoFactory<Connection> {
    //jdbc:postgresql://localhost:5432/db
    private String user = "root";//Логин пользователя
    private String password = "root";//Пароль пользователя
    private String url = "jdbc:postgresql://localhost:5432/db";//URL адрес
    //private String driver = "org.apache.derby.jdbc.EmbeddedDriver";//имя драйвера
    private String driver = "org.postgresql.Driver";//имя драйвера
    //org.apache.derby.jdbc.ClientDriver
    private Map<Class, DaoCreator> creators;

    public Connection getContext() throws PersistException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            throw new PersistException(e);
        }
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/db",
                            "root", "root");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
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
        try {
            Class.forName(driver);//Регистрируем драйвер
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

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
