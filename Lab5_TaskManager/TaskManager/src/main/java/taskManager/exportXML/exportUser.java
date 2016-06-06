package taskManager.exportXML;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import taskManager.domain.User;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExportUser {

    public static boolean createXMLUserDocument(String path, List<User> list){
        boolean flag = false;
        // Create document
        Document xmlDoc = new Document();
        // Create root element
        Element root = new Element("users");
        root.setNamespace(Namespace.getNamespace("taskManager"));
        root.setAttribute("size", list.size()+"");
        // Add root element to document
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
            Format fmt = Format.getPrettyFormat();
            // Output generated XML to file, using a prepared format
            XMLOutputter serializer = new XMLOutputter(fmt);
            //serializer.output(xmlDoc, System.out);
            serializer.output(xmlDoc, new FileOutputStream(new File(path)));
            flag = true;
        }
        catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
            flag = false;
        }

        return flag;

    }

}
