package taskManager.importXML;


import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import taskManager.domain.Taskweb;
import taskManager.postgreSql.PostgreSqlDaoFactory;
import taskManager.utils.Utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class importTask {

    //private static PostgreSqlDaoFactory factory = new PostgreSqlDaoFactory();

    public static ArrayList<Taskweb> parserToListObjects(String path){
        int size=0;
        ArrayList<Taskweb> listTask = new ArrayList<>();

        try(FileReader fr = new FileReader(path)) {
            SAXBuilder parser = new SAXBuilder();
            Document doc = parser.build(fr);

            Element rootElement=doc.getRootElement();//tasks

            if(rootElement.getName().equals("tasks")){
                size = Integer.parseInt(rootElement.getAttributeValue("size"));
            }
            else return null;
            List<Element> listElement=rootElement.getChildren();

            for(Element element : listElement){
                listTask.add(parserElement(element));
            }

            return listTask;

        } catch (JDOMException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());

        }
        return null;

    }

    private static Taskweb parserElement(Element element){
        if(element.getName().equals("task")){
            Taskweb task = new Taskweb();

            Element idElement = element.getChild("id", Namespace.getNamespace("taskManager"));
            Integer id_task = Integer.valueOf(idElement.getValue());
            task.setId(id_task);

            Element nameElement = element.getChild("name", Namespace.getNamespace("taskManager"));
            task.setName( nameElement.getValue() );

            Element contactsElement = element.getChild("contacts", Namespace.getNamespace("taskManager"));
            task.setContacts(contactsElement.getValue());

            Element descriptionElement = element.getChild("description", Namespace.getNamespace("taskManager"));
            task.setDescription(descriptionElement.getValue());

            Element highElement = element.getChild("highPriority", Namespace.getNamespace("taskManager"));
            task.setHighPriority( Boolean.valueOf(highElement.getValue()) );

            Element parentElement = element.getChild("parentId", Namespace.getNamespace("taskManager"));
            int parentId = Integer.valueOf(parentElement.getValue());
            task.setParentId(parentId);
//            try {
//                GenericDao<Task, Integer> dao = factory.getDao(factory.getContext(), Task.class);
//                Task parentTask = dao.getByPK(parentId);
//                task.setParentId(parentTask);
//            } catch (PersistException e) {
//                e.printStackTrace();
//            }

            Element userElement = element.getChild("userId", Namespace.getNamespace("taskManager"));
            int userId = Integer.valueOf(userElement.getValue());
//            try {
//                GenericDao<User, Integer> dao = factory.getDao(factory.getContext(), User.class);
//                User user = dao.getByPK(userId);
//                task.setUser(user);
//            } catch (PersistException e) {
//                e.printStackTrace();
//            }
            task.setUserId(userId);

            Element dateElement = element.getChild("date", Namespace.getNamespace("taskManager"));
            task.setDate(Utils.strToCalendar(dateElement.getValue(), true).getTime() );

            return task;
        }
        return null;
    }

}
