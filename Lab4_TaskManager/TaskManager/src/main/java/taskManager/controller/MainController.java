package taskManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import taskManager.dao.*;
import taskManager.domain.User;

import javax.annotation.PostConstruct;
import java.sql.Connection;



@Controller
public class MainController {


   @Autowired
    private DaoFactory<Connection> daoFactory;

    private Connection connection;

    @PostConstruct
    public void postMainController() {
        //получаем соединение, которое будем использовать в др. методах для запросов к БД
        try {
            connection = daoFactory.getContext();
        } catch (PersistException e) {
            e.printStackTrace();
        }
    }

    /*Попадаем сюда на старте приложения  */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView main() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userJSP", new User());
        modelAndView.setViewName("index");
        return modelAndView;
    }

    /*как только на index.jsp подтвердится форма
    <spring:form method="post"  modelAttribute="userJSP" action="check-user">,
    то попадем вот сюда
     */
    @RequestMapping(value = "/check-user")
    public ModelAndView checkUser(@ModelAttribute("userJSP") User user) {
        ModelAndView modelAndView = new ModelAndView();

        //имя представления, куда нужно будет перейти
        modelAndView.setViewName("mainPage");

        //записываем в атрибут userJSP (используется на странице *.jsp объект user
        //modelAndView.addObject("userJSP", user);
        //modelAndView.addObject("taskJSP", new Task());
        return modelAndView;
    }

}
