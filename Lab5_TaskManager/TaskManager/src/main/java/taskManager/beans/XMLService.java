package taskManager.beans;

import org.hibernate.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import taskManager.dao.PersistException;
import taskManager.domain.Task;
import taskManager.domain.Taskweb;
import taskManager.domain.User;
import taskManager.exportXML.ExportTask;
import taskManager.exportXML.ExportUser;
import taskManager.importXML.ImportTask;
import taskManager.importXML.ImportUser;
import taskManager.importXML.XmlValidator;
import taskManager.postgreSql.PostgreSqlDaoFactory;
import taskManager.postgreSql.PostgreSqlTaskDao;
import taskManager.postgreSql.PostgreSqlUserDao;
import taskManager.utils.Utils;

import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;

import java.text.ParseException;
import java.util.*;

@Component
@Scope(value = "request")
public class XMLService {

    private static final String PATH_XSD_USER = "xsd/user.xsd";
    private static final String PATH_XSD_TASK = "xsd/task.xsd";
    @Autowired
    private XmlValidator fileValidator;

    private PostgreSqlDaoFactory factory;
    private Session session;

    @PostConstruct
    public void postXmlService() throws PersistException {
        factory = new PostgreSqlDaoFactory();
        session = factory.getContext();
    }

    public boolean load(String path, Class c, BindingResult errors) throws PersistException, ParseException {
        File xsdFile = null;

        if (c.equals(User.class)) {
            xsdFile = new File(PATH_XSD_USER);
        } else if (c.equals(Task.class)) {
            xsdFile = new File(PATH_XSD_TASK);
        }

        if (!xsdFile.exists()) {
            fileValidator.genereteError(errors, "XSD File not found");
            return false;
        }

        boolean validFile = validateXml(new File(path), xsdFile);

        if (!validFile) {
            fileValidator.genereteError(errors, "XML File not valid! Please select other file");
            return false;
        }

        if (c.equals(User.class)) {
            List<User> listUser = readXmlUser(path);
            if (listUser == null) {
                fileValidator.genereteError(errors, "Error read XML File! Please select other file");
                return false;
            }


            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(session, User.class);
            for (User user : listUser) {
                try {
                    userDao.update(user);
                } catch (PersistException e) {
                    //use self Id; if user.tasks reference to task.user
                    userDao.persist(user, true);
                    //if userDao.persist(user, false) create new id to user
                }
            }
            return true;

        } else if (c.equals(Task.class)) {//if id task is wrong, it is user error, not my folt
            List<Taskweb> listTaskWeb = readXmlTask(path);
            if (listTaskWeb == null) {
                fileValidator.genereteError(errors, "Error read XML File! Please select other file");
                return false;
            }

            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) factory.getDao(session, Task.class);
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(session, User.class);
            Task task = null;
            for (Taskweb taskweb : listTaskWeb) {
                try {
                    task = Utils.taskConvert(taskweb, taskDao, userDao);
                    taskDao.update(task);
                } catch (PersistException e) {
                    //use self Id; if task.parent reference to task.id
                    taskDao.persist(task, true);
                    //if taskDao.persist(task, false) create new id to task

                }
            }
            return true;

        }
        fileValidator.genereteError(errors, "Something Error in XML File! Please select other file");
        return false;
    }

    public boolean save(String path, List list, Class c) {
        List<Task> listTask = null;
        List<User> listUser = null;
        boolean flagCreate = false;

        if (c.equals(User.class)) {
            listUser = new ArrayList<>();
            for (Object object : list) {
                listUser.add((User) object);
            }
            flagCreate = ExportUser.createXMLUserDocument(path, listUser);
        } else if (c.equals(Task.class)) {
            listTask = new ArrayList<>();
            for (Object object : list) {
                listTask.add((Task) object);
            }
            flagCreate = ExportTask.createXMLTaskDocument(path, listTask);
        }

        return flagCreate;

    }

    public boolean validateXml(File xml, File xsd) {
        try {
            Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                    .newSchema(new StreamSource(xsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xml));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public List<User> readXmlUser(String path) {
        List<User> list = ImportUser.parserToListObjects(path);
        if (list == null) return null;
        Collections.sort(list);
        return list;
    }

    public List<Taskweb> readXmlTask(String path) throws ParseException{
        List<Taskweb> list = ImportTask.parserToListObjects(path);
        if (list == null) return null;
        Collections.sort(list);
        return list;
    }

}
