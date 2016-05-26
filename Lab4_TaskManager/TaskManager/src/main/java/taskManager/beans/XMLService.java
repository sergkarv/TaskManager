package taskManager.beans;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import taskManager.dao.DaoFactory;
import taskManager.dao.Identified;
import taskManager.dao.PersistException;
import taskManager.domain.Task;
import taskManager.domain.User;
import taskManager.exportXML.exportTask;
import taskManager.exportXML.exportUser;
import taskManager.importXML.ImportXml;
import taskManager.importXML.XmlValidator;
import taskManager.importXML.importTask;
import taskManager.importXML.importUser;
import taskManager.postgreSql.PostgreSqlTaskDao;
import taskManager.postgreSql.PostgreSqlUserDao;

import javax.annotation.PostConstruct;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

@Component
@Scope(value = "request")
public class XMLService {

    private final String pathXsdUser = "xsd/user.xsd";
    private final String pathXsdTask = "xsd/task.xsd";
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
            xsdFile = new File(pathXsdUser);
        }
        else if(c.equals(Task.class)){
            xsdFile = new File(pathXsdTask);
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
            ArrayList<User> listUser = readXmlUser(path);
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
            ArrayList<Task> listTask = readXmlTask(path);
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
            flagCreate = exportUser.createXMLUserDocument(path, listUser);
        }
        else if(c.equals(Task.class)){
            listTask = new ArrayList<>();
            for(Object object : list){
                listTask.add((Task) object);
            }
            flagCreate = exportTask.createXMLTaskDocument(path, listTask);
        }

        return flagCreate;

    }

    public boolean validateXml(File xml, File xsd) {
        try {
//            SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
//                    .newSchema(new StreamSource(xsd))
//                    .newValidator()
//                    .validate(new StreamSource(xml));
            Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                    .newSchema(new StreamSource(xsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xml));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public ArrayList<User> readXmlUser(String path){
        ArrayList<User> list = importUser.parserToListObjects(path);
        if(list == null) return null;
        Collections.sort(list);
        return list;
    }

    public ArrayList<Task> readXmlTask(String path){
        ArrayList<Task> list = importTask.parserToListObjects(path);
        if(list == null) return null;
        Collections.sort(list);
        return list;
    }

}