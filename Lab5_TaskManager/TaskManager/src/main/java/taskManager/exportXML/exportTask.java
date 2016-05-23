package taskManager.exportXML;

import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import taskManager.domain.Task;
import taskManager.domain.User;
import taskManager.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Сергей on 04.05.16.
 */
public class exportTask {

    public static boolean createXMLTaskDocument(String path, List<Task> list){
        boolean flag = false;
        // Создаем документ
        Document xmlDoc = new Document();
        //factory.setNamespaceAware(true);
        // Создаем корневой элемент
        Element root = new Element("tasks");
        root.setNamespace(Namespace.getNamespace("taskManager"));
        root.setAttribute("size", list.size()+"");
        // Добавляем корневой элемент в документ
        xmlDoc.setRootElement(root);

        for(Task task : list){
            Element taskElement = new Element("task");
            taskElement.setNamespace(Namespace.getNamespace("taskManager"));

            Element idElement = new Element("id");
            idElement.setNamespace(Namespace.getNamespace("taskManager"));
            idElement.addContent(task.getId().toString());
            taskElement.addContent(idElement);

            Element nameElement = new Element("name");
            nameElement.setNamespace(Namespace.getNamespace("taskManager"));
            nameElement.addContent(task.getName());
            taskElement.addContent(nameElement);

            Element descriptionElement = new Element("description");
            descriptionElement.setNamespace(Namespace.getNamespace("taskManager"));
            descriptionElement.addContent(task.getDescription());
            taskElement.addContent(descriptionElement);

            Element contactsElement = new Element("contacts");
            contactsElement.setNamespace(Namespace.getNamespace("taskManager"));
            contactsElement.addContent(task.getContacts());
            taskElement.addContent(contactsElement);

            Element dateElement = new Element("date");
            dateElement.setNamespace(Namespace.getNamespace("taskManager"));
            Calendar calendar =  Calendar.getInstance();
            calendar.setTime(task.getDate());
            dateElement.addContent(Utils.calendarToStr(calendar, true) );
            taskElement.addContent(dateElement);

            Element priorityElement = new Element("highPriority");
            priorityElement.setNamespace(Namespace.getNamespace("taskManager"));
            priorityElement.addContent(task.isHighPriority()+"");
            taskElement.addContent(priorityElement);

            Element parentElement = new Element("parentId");
            parentElement.setNamespace(Namespace.getNamespace("taskManager"));
            parentElement.addContent((task.getParent() != null)? task.getParent().getId().toString() : "0");
            taskElement.addContent(parentElement);

            Element userElement = new Element("userId");
            userElement.setNamespace(Namespace.getNamespace("taskManager"));
            userElement.addContent(task.getUser().getId().toString());
            taskElement.addContent(userElement);

            root.addContent(taskElement);

        }
        try {
            // Получаем "красивый" формат для вывода XML
            // с переводами на новую строку и отступами
            Format fmt = Format.getPrettyFormat();
            // Выводим созданный XML в файл, используя подготовленный формат
            XMLOutputter serializer = new XMLOutputter(fmt);
            serializer.output(xmlDoc, new FileOutputStream(new File(path)));
            flag = true;
        }
        catch (IOException e) {
            System.err.println(e);
            flag = false;
        }

        return flag;

    }

}
