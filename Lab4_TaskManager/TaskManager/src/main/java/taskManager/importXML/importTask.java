package taskManager.importXML;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import taskManager.domain.Task;
import taskManager.domain.User;
import taskManager.utils.Utils;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ImportTask {

    public static List<Task> parserToListObjects(String path){
        //FileReader fr = null;
        int size=0;
        List<Task> listUser = new ArrayList<>();

        try(FileReader fr = new FileReader(path)) {
            SAXBuilder parser = new SAXBuilder();
            //fr = new FileReader(path);
            Document doc = parser.build(fr);

            Element rootElement=doc.getRootElement();//users

            if(rootElement.getName().equals("tasks")){
                size = Integer.parseInt(rootElement.getAttributeValue("size"));
            }
            else return null;
            List<Element> listElement=rootElement.getChildren();

            for(Element element : listElement){
                listUser.add(parserElement(element));
            }

            return listUser;

        } catch (JDOMException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());

        }
        return null;

    }

    private static Task parserElement(Element element){
        if(element.getName().equals("task")){
            Task task = new Task();
            Class userClass = task.getClass();
            try {
                Field id = userClass.getDeclaredField("id");
                id.setAccessible(true);
                Element idElement = element.getChild("id", Namespace.getNamespace("taskManager"));
                Integer id_task = Integer.valueOf(idElement.getValue());
                id.set(task, id_task);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

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
            task.setParentId( (parentId != 0)? parentId : null );

            Element userElement = element.getChild("userId", Namespace.getNamespace("taskManager"));
            task.setUserId(Integer.valueOf(userElement.getValue()));

            Element dateElement = element.getChild("date", Namespace.getNamespace("taskManager"));
            task.setDate(Utils.strToCalendar(dateElement.getValue(), true).getTime() );

            return task;
        }
        return null;
    }

}
