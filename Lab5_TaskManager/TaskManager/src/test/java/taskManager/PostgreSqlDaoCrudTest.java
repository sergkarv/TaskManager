package taskManager;

import junit.framework.Assert;
import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import taskManager.dao.DaoFactory;
import taskManager.dao.EmptyParamException;
import taskManager.dao.NullPointParameterException;
import taskManager.dao.PersistException;
import taskManager.domain.Task;
import taskManager.domain.User;
import taskManager.postgreSql.PostgreSqlDaoFactory;
import taskManager.postgreSql.PostgreSqlTaskDao;
import taskManager.postgreSql.PostgreSqlUserDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Сергей on 24.03.16.
 */

public class PostgreSqlDaoCrudTest {

    private static Session session;
    private static DaoFactory<Session> factory = new PostgreSqlDaoFactory();

    @BeforeClass
    public static void setUpBeforeClass()throws PersistException, SQLException,
            NullPointParameterException, EmptyParamException {

        session = factory.getContext();

        PostgreSqlUserDao userDaoTest1 = (PostgreSqlUserDao)factory.getDao(session, User.class);

        User user = new User();
        user.setName("test_user_1");
        user.setPassword("Test@#");
        User userTest = userDaoTest1.persist(user, false);
        Assert.assertNotNull("userTest is null object", userTest);
        Assert.assertNotNull("userTest.id is null object", userTest.getId());
        Assert.assertEquals("userTest.name is not equals user.name", user.getName(), userTest.getName());

        user = new User();
        user.setName("test_user_2");
        user.setPassword("Test$%");
        userTest = userDaoTest1.persist(user, false);
        Assert.assertNotNull("userTest is null object", userTest);
        Assert.assertNotNull("userTest.id is null object", userTest.getId());
        Assert.assertEquals("userTest.name is not equals user.name", user.getName(), userTest.getName());

        PostgreSqlTaskDao taskDaoTest = (PostgreSqlTaskDao) factory.getDao(session, Task.class);

        Task taskList1 = new Task();
        taskList1.setName("list_1");
        taskList1.setUser(userTest);
        Task newTaskList1 = taskDaoTest.persist(taskList1, false);
        Assert.assertNotNull("newTaskList1 is null object", newTaskList1);
        Assert.assertNotNull("newTaskList1.id is null object", newTaskList1.getId());
        Assert.assertEquals("newTaskList1.name is not equals taskList1.name", taskList1.getName(), newTaskList1.getName());

        Task taskList2 = new Task();
        taskList2.setName("list_2");
        taskList2.setUser(userTest);
        Task newTaskList2 = taskDaoTest.persist(taskList2, false);
        Assert.assertNotNull("newTaskList2 is null object", newTaskList2);
        Assert.assertNotNull("newTaskList2.id is null object", newTaskList2.getId());
        Assert.assertEquals("newTaskList2.name is not equals taskList2.name", taskList2.getName(), newTaskList2.getName());

        Task taskList3 = new Task();
        taskList3.setName("list_3");
        taskList3.setUser(userTest);
        Task newTaskList3 = taskDaoTest.persist(taskList3, false);
        Assert.assertNotNull("newTaskList3 is null object", newTaskList3);
        Assert.assertNotNull("newTaskList3.id is null object", newTaskList3.getId());
        Assert.assertEquals("newTaskList3.name is not equals taskList3.name",
                taskList3.getName(), newTaskList3.getName());

        Task taskList1_1 = new Task();
        taskList1_1.setName("list_1_1");
        taskList1_1.setUser(userTest);
        taskList1_1.setParent(newTaskList1);
        Task newTaskList1_1 = taskDaoTest.persist(taskList1_1, false);
        Assert.assertNotNull("newTaskList1_1 is null object", newTaskList1_1);
        Assert.assertNotNull("newTaskList1_1.id is null object", newTaskList1_1.getId());
        Assert.assertEquals("newTaskList1_1.name is not equals taskList1_1.name",
                taskList1_1.getName(), newTaskList1_1.getName());
        Assert.assertEquals("Parent newTaskList1_1 is not newTaskList1",
                newTaskList1.getId(), newTaskList1_1.getParent().getId());

        Task taskList1_2 = new Task();
        taskList1_2.setName("list_1_2");
        taskList1_2.setUser(userTest);
        taskList1_2.setParent(newTaskList1);
        Task newTaskList1_2 = taskDaoTest.persist(taskList1_2, false);
        Assert.assertNotNull("newTaskList1_2 is null object", newTaskList1_2);
        Assert.assertNotNull("newTaskList1_2.id is null object", newTaskList1_2.getId());
        Assert.assertEquals("newTaskList1_2.name is not equals taskList1_2.name", taskList1_2.getName(), newTaskList1_2.getName());
        Assert.assertEquals("Parent newTaskList1_2 is not newTaskList1",
                newTaskList1.getId(),newTaskList1_2.getParent().getId());

        Task taskList2_1 = new Task();
        taskList2_1.setName("list_2_1");
        taskList2_1.setUser(userTest);
        taskList2_1.setParent(newTaskList2);
        Task newTaskList2_1 = taskDaoTest.persist(taskList2_1, false);
        Assert.assertNotNull("newTaskList2_1 is null object", newTaskList2_1);
        Assert.assertNotNull("newTaskList2_1.id is null object", newTaskList2_1.getId());
        Assert.assertEquals("newTaskList2_1.name is not equals taskList1.name", taskList2_1.getName(), newTaskList2_1.getName());
        Assert.assertEquals("Parent newTaskList2_1 is not newTaskList2",
                newTaskList2.getId(), newTaskList2_1.getParent().getId());

        Task taskList3_1 = new Task();
        taskList3_1.setName("list_3_1");
        taskList3_1.setUser(userTest);
        taskList3_1.setParent(newTaskList3);
        Task newTaskList3_1 = taskDaoTest.persist(taskList3_1, false);
        Assert.assertNotNull("newTaskList3_1 is null object", newTaskList3_1);
        Assert.assertNotNull("newTaskList3_1.id is null object", newTaskList3_1.getId());
        Assert.assertEquals("newTaskList3_1.name is not equals taskList3_1.name", taskList3_1.getName(), newTaskList3_1.getName());
        Assert.assertEquals("Parent newTaskList3_1 is not newTaskList3",
                newTaskList3.getId(), newTaskList3_1.getParent().getId());

        Task taskList3_2 = new Task();
        taskList3_2.setName("list_3_2");
        taskList3_2.setUser(userTest);
        taskList3_2.setParent(newTaskList3);
        Task newtaskList3_2 = taskDaoTest.persist(taskList3_2, false);
        Assert.assertNotNull("newtaskList3_2 is null object", newtaskList3_2);
        Assert.assertNotNull("newtaskList3_2.id is null object", newtaskList3_2.getId());
        Assert.assertEquals("newtaskList3_2.name is not equals taskList3_2.name", taskList3_2.getName(), newtaskList3_2.getName());
        Assert.assertEquals("Parent newtaskList3_2 is not newtaskList3",
                newTaskList3.getId(), newtaskList3_2.getParent().getId());

        Task taskList3_2_1 = new Task();
        taskList3_2_1.setName("list_3_2_1");
        taskList3_2_1.setUser(userTest);
        taskList3_2_1.setParent(newtaskList3_2);
        Task newtaskList3_2_1 = taskDaoTest.persist(taskList3_2_1, false);
        Assert.assertNotNull("newtaskList3_2_1 is null object", newtaskList3_2_1);
        Assert.assertNotNull("newtaskList3_2_1.id is null object", newtaskList3_2_1.getId());
        Assert.assertEquals("newtaskList3_2_1.name is not equals taskList3_2_1.name", taskList3_2_1.getName(), newtaskList3_2_1.getName());
        Assert.assertEquals("Parent newtaskList3_2_1 is not newTaskList3_2",
                newtaskList3_2.getId(), newtaskList3_2_1.getParent().getId());
    }


    @AfterClass
    public static void tearDownAfterClass() throws PersistException, SQLException{
        deleteCascade();
        session.close();
    }

    @Test
    public void selectAllTask() throws PersistException {
        PostgreSqlTaskDao taskDaoTest = (PostgreSqlTaskDao) factory.getDao(session, Task.class);
        List<Task> list = taskDaoTest.getAll();
        Assert.assertNotNull("List<Task> is null object", list);
        System.out.println("id | name | description | contacts | date | highPriority | parentId | userId");
        for(Task task : list){
            StringBuffer s = new StringBuffer();
            s.append(task.getId()).append(" | ");
            s.append(task.getName()).append(" | ");
            s.append(task.getDescription()).append(" | ");
            s.append(task.getContacts()).append(" | ");
            s.append(task.getDate()).append(" | ");
            s.append(task.isHighPriority()).append(" | ");
            s.append(task.getParent()!= null?task.getParent().getId(): "null").append(" | ");
            s.append(task.getUser().getId()).append(" | ");
            System.out.println(s.toString());
        }
        System.out.println();
    }

    @Test
    public void selectAllUser() throws PersistException{
        PostgreSqlUserDao taskDaoUser = (PostgreSqlUserDao) factory.getDao(session, User.class);
        List<User> list = taskDaoUser.getAll();
        Assert.assertNotNull("List<User> is null object", list);
        System.out.println("id | name | password");
        for(User user : list){
            StringBuffer s = new StringBuffer();
            s.append(user.getId()).append(" | ");
            s.append(user.getName()).append(" | ");
            s.append(user.getPassword()).append(" | ");
            System.out.println(s.toString());
        }
        System.out.println();
    }

    public static void selectAllTaskAndUser() throws PersistException{
        PostgreSqlUserDao taskDaoUser = (PostgreSqlUserDao) factory.getDao(session, User.class);
        List<User> listUser = taskDaoUser.getAll();
        PostgreSqlTaskDao taskDaoTest = (PostgreSqlTaskDao) factory.getDao(session, Task.class);
        List<Task> listTask = taskDaoTest.getAll();
        Assert.assertNotNull("List<Task> is null object", listTask);
        Assert.assertNotNull("List<User> is null object", listUser);
        System.out.println("id | name | description | contacts | date | highPriority | parent | user | password");
        boolean flag = false;
        for(User user : listUser){
            for(Task task : user.getTasks()){
                flag = false;
                for(Task parent : listTask){
                    if(flag) break;

                    if( (task.getUser().getId() == user.getId()) &&
                            ( ((task.getParent() == null) )|| (task.getParent().getId() == parent.getId()) )
                            ){
                        StringBuffer s = new StringBuffer();
                        s.append(task.getId()).append(" | ");
                        s.append(task.getName()).append(" | ");
                        s.append(task.getDescription()).append(" | ");
                        s.append(task.getContacts()).append(" | ");
                        s.append(task.getDate()).append(" | ");
                        s.append(task.isHighPriority()).append(" | ");
                        s = (task.getParent() != null) ? s.append(parent.getName()).append(" | ") :
                                s.append("NULL").append(" | ");
                        s.append(user.getName()).append(" | ");
                        s.append(user.getPassword()).append(" | ");
                        System.out.println(s.toString());
                        if(task.getParent() == null){
                            flag = true;
                            break;
                        }
                    }

                }

            }
        }

        System.out.println();
    }

    @Test
    public void selectTask()throws PersistException{
        PostgreSqlTaskDao taskDaoTest = (PostgreSqlTaskDao) factory.getDao(session, Task.class);
        List<Task> listTask = taskDaoTest.getAll();
        int id = listTask.get(0).getId();
        Task task = taskDaoTest.getByPK(id);
        Assert.assertNotNull("Task is null object", task);
    }

    @Test
    public void selectUser()throws PersistException{
        PostgreSqlUserDao taskDaoUser = (PostgreSqlUserDao) factory.getDao(session, User.class);
        List<User> listUser = taskDaoUser.getAll();
        int id = listUser.get(0).getId();
        User user = taskDaoUser.getByPK(id);
        Assert.assertNotNull("Task is null object", user);
    }

    @Test
    public void updateUser() throws PersistException{
        //change password of testUser_2
        PostgreSqlUserDao taskDaoUser = (PostgreSqlUserDao) factory.getDao(session, User.class);
        List<User> listUser = taskDaoUser.getAll();
        User currentUser = null;
        for(User user : listUser ){
            if(user.getName().equals("test_user_2")){
                currentUser = user;
                break;
            }

        }
        currentUser.setPassword("test_test_test_update");
        taskDaoUser.update(currentUser);
        System.out.println("test_user_2 is changed");
        System.out.println();
        selectAllUser();
    }

    @Test
    public void updateTask() throws PersistException{
        PostgreSqlTaskDao taskDaoTest = (PostgreSqlTaskDao) factory.getDao(session, Task.class);
        List<Task> listTask = taskDaoTest.getAll();
        Task task321 = null;
        Task list3 = null;
        for(Task task : listTask){
            if(task.getName().equals("list_3_2_1")){
                task.setName("list_3_2_1_update");
                task.setDescription("test_update_description");
                task.setContacts("test_update_contacts");
                task321 = task;
                break;
            }
        }
        for(Task task : listTask){
            if(task.getName().equals("list_3")){
                list3 = task;
                break;
            }
        }

        task321.setParent(list3);
        System.out.println("task_3_2_1 is changed");
        System.out.println();
        taskDaoTest.update(task321);
        selectAllTask();

    }

    @Test
    public void updateTasksConditions() throws PersistException{
        PostgreSqlTaskDao taskDaoTest = (PostgreSqlTaskDao) factory.getDao(session, Task.class);
        List<Task> listTask = taskDaoTest.getAll();
        List<Task> updateTask = new ArrayList<>();
        for(Task task : listTask){
            if(task.getParent() != null){
                task.setDescription("Change Task where parent is not null!");
                taskDaoTest.update(task);
                updateTask.add(task);
            }
        }
        for(Task task : updateTask){
            taskDaoTest.update(task);
        }
        System.out.println("6 Tasks is changed");
        System.out.println();
        selectAllTask();
    }

    @Test
    public void deleteUser()throws PersistException{
        PostgreSqlUserDao taskDaoUser = (PostgreSqlUserDao) factory.getDao(session, User.class);
        PostgreSqlTaskDao taskDao  = (PostgreSqlTaskDao) factory.getDao(session, Task.class);
        List<User> listUser = taskDaoUser.getAll();
        for(User user : listUser ){
            if(user.getName().equals("test_user_2")){
//                for(Task task : user.getTasks()){
//                    taskDao.delete(task.getId());
//                }
                taskDaoUser.delete(user.getId());
                break;
            }
        }
        System.out.println("test_user_2 is deleted");
        System.out.println();
        selectAllUser();
    }

    @Test
    public void deleteTask() throws PersistException{
        PostgreSqlTaskDao taskDaoTest = (PostgreSqlTaskDao) factory.getDao(session, Task.class);
        List<Task> listTask = taskDaoTest.getAll();
        //Task list2 = null;
        for(Task task : listTask){
            if(task.getName().equals("list_2")){
                taskDaoTest.delete(task.getId());
                break;
            }
        }
        System.out.println("list_2 is deleted");
        System.out.println();
        selectAllTask();
    }

    public static void deleteCascade( ) throws PersistException{
        PostgreSqlUserDao taskDaoUser = (PostgreSqlUserDao) factory.getDao(session, User.class);
        List<User> listUser = taskDaoUser.getAll();
        for(User user : listUser ){
            if(user.getName().equals("test_user_1")){
                taskDaoUser.delete(user.getId());
                break;
            }
        }
        System.out.println("test_user_1 is deleted");
        System.out.println();
        selectAllTaskAndUser();
    }


}
