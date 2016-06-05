package taskManager.importXML;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import taskManager.domain.User;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class importUser {

    public static ArrayList<User> parserToListObjects(String path){
        int size=0;
        ArrayList<User> listUser = new ArrayList<>();

        try(FileReader fr = new FileReader(path)) {
            SAXBuilder parser = new SAXBuilder();
            Document doc = parser.build(fr);

            Element rootElement=doc.getRootElement();//users

            if(rootElement.getName().equals("users")){
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

    private static User parserElement(Element element){
        if(element.getName().equals("user")){
            User user = new User();
            Element idElement = element.getChild("id", Namespace.getNamespace("taskManager"));
            Integer id_user = Integer.valueOf( idElement.getValue() );
            user.setId(id_user);
            Element nameElement = element.getChild("name", Namespace.getNamespace("taskManager"));
            user.setName( nameElement.getValue() );
            Element passElement = element.getChild("password", Namespace.getNamespace("taskManager"));
            user.setPassword(passElement.getValue() );

            return user;
        }
        return null;
    }

}
