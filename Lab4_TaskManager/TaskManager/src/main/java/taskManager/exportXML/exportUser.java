package taskManager.exportXML;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import taskManager.domain.Task;
import taskManager.domain.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class ExportUser {

    public static boolean createXMLUserDocument(String path, List<User> list){
        boolean flag = false;
        // Создаем документ
        Document xmlDoc = new Document();
        // Создаем корневой элемент
        Element root = new Element("users");
        root.setNamespace(Namespace.getNamespace("taskManager"));
        root.setAttribute("size", list.size()+"");
        // Добавляем корневой элемент в документ
        xmlDoc.setRootElement(root);

        for(User user : list){
            Element taskElement = new Element("user");
            taskElement.setNamespace(Namespace.getNamespace("taskManager"));

            Element idElement = new Element("id");
            idElement.setNamespace(Namespace.getNamespace("taskManager"));
            idElement.addContent(user.getId().toString());
            taskElement.addContent(idElement);

            Element nameElement = new Element("name");
            nameElement.setNamespace(Namespace.getNamespace("taskManager"));
            nameElement.addContent(user.getName());
            taskElement.addContent(nameElement);

            Element passElement = new Element("password");
            passElement.setNamespace(Namespace.getNamespace("taskManager"));
            passElement.addContent(user.getPassword());
            taskElement.addContent(passElement);

            root.addContent(taskElement);

        }
        try {
            // Получаем "красивый" формат для вывода XML
            // с переводами на новую строку и отступами
            Format fmt = Format.getPrettyFormat();
            // Выводим созданный XML в файл, используя подготовленный формат
            XMLOutputter serializer = new XMLOutputter(fmt);
            //serializer.output(xmlDoc, System.out);
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
