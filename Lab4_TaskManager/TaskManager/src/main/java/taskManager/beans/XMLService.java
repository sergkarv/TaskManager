package taskManager.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import taskManager.dao.DaoFactory;
import taskManager.dao.PersistException;
import taskManager.domain.Task;
import taskManager.domain.User;
import taskManager.exportXML.ExportTask;
import taskManager.exportXML.ExportUser;
import taskManager.importXML.ImportTask;
import taskManager.importXML.ImportUser;
import taskManager.importXML.XmlValidator;
import taskManager.postgreSql.PostgreSqlTaskDao;
import taskManager.postgreSql.PostgreSqlUserDao;

import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.sql.Connection;
import java.util.*;

@Component
@Scope(value = "request")
public class XMLService {

    private static final String PATH_XSD_USER = "xsd/user.xsd";
    private static final String PATH_XSD_TASK = "xsd/task.xsd";
    @Autowired
    private XmlValidator fileValidator;

    @Autowired
    private DaoFactory<Connection> daoFactory;
    private Connection connection;

    @PostConstruct
    public void postXmlService() {
        try {
            connection = daoFactory.getContext();
        } catch (PersistException e) {
            e.printStackTrace();
        }
    }

    public boolean load(String path, Class c, BindingResult errors){

        File xsdFile = null;

        if(c.equals(User.class)){
            xsdFile = new File(PATH_XSD_USER);
        }
        else if(c.equals(Task.class)){
            xsdFile = new File(PATH_XSD_TASK);
        }

        if(!xsdFile.exists()){
            fileValidator.genereteError(errors,"XSD File not found");
            return false;
        }

        boolean validFile = validateXml(new File(path), xsdFile);

        if(!validFile){
            fileValidator.genereteError(errors,"XML File not valid! Please select other file");
            return false;
        }

        if(c.equals(User.class)){
            List<User> listUser = readXmlUser(path);
            if(listUser == null){
                fileValidator.genereteError(errors,"Error read XML File! Please select other file");
                return false;
            }

            try {
                PostgreSqlUserDao userDao = (PostgreSqlUserDao) daoFactory.getDao(connection, User.class);
                for(User user : listUser){
                    try{
                        userDao.getByPK(user.getId());
                        userDao.update(user);
                    }catch (PersistException e){
                        userDao.persist(user, false);//old id not used, create new id user
                    }
                }
                return true;
            } catch (PersistException e) {
                e.printStackTrace();
            }
        }
        else if(c.equals(Task.class)){//if id task is wrong, it is user error, not my folt
            List<Task> listTask = readXmlTask(path);
            if(listTask == null){
                fileValidator.genereteError(errors,"Error read XML File! Please select other file");
                return false;
            }

            try {
                PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) daoFactory.getDao(connection, Task.class);
                for(Task task : listTask){
                    try{
                        taskDao.getByPK(task.getId());
                        taskDao.update(task);
                    }catch (PersistException e){
                        taskDao.persist(task, true);
                    }
                }
                return true;
            } catch (PersistException e) {
                e.printStackTrace();
            }


        }
        fileValidator.genereteError(errors,"Something Error in XML File! Please select other file");
        return false;
    }

    public boolean save(String path, List list, Class c){
        List<Task> listTask = null;
        List<User> listUser = null;
        boolean flagCreate = false;

        if(c.equals(User.class)){
            listUser = new ArrayList<>();
            for(Object object : list){
                listUser.add((User) object);
            }
            flagCreate = ExportUser.createXMLUserDocument(path, listUser);
        }
        else if(c.equals(Task.class)){
            listTask = new ArrayList<>();
            for(Object object : list){
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

    public List<User> readXmlUser(String path){
        List<User> list = ImportUser.parserToListObjects(path);
        if(list == null) return null;
        Collections.sort(list);
        return list;
    }

    public List<Task> readXmlTask(String path){
        List<Task> list = ImportTask.parserToListObjects(path);
        if(list == null) return null;
        Collections.sort(list);
        return list;
    }

}
