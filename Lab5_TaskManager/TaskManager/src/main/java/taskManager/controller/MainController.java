package taskManager.controller;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;

import java.sql.Connection;
import java.util.List;

import taskManager.dao.*;
import taskManager.domain.Task;
import taskManager.domain.User;
import taskManager.postgreSql.PostgreSqlDaoFactory;
import taskManager.postgreSql.PostgreSqlUserDao;




@Controller
@Transactional
public class MainController {

    private PostgreSqlDaoFactory factory;
    private Session session;

    @PostConstruct
    public void postMainController() {
        factory = new PostgreSqlDaoFactory();
        try {
            session = factory.getContext();
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
        modelAndView.addObject("userJSP", user);
        modelAndView.addObject("taskJSP", new Task());
        return modelAndView;
    }

    //check CRUD
    @RequestMapping(value = "/test")
    public ModelAndView checkUserTest() {
        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao)factory.getDao(session, User.class);
            User user = new User();
            user.setName("ty nfjklsd 4");
            user.setPassword("45456");
            user.setId(1001);
            SQLQuery query = userDao.getSession().createSQLQuery("insert into tu.user values(?, ?, ?)");
            query.setInteger(1, user.getId());
            query.setParameter(2, user.getName());
            query.setParameter(3, user.getPassword());
            session.getTransaction().begin();
            int result = query.executeUpdate();
            session.getTransaction().commit();
            System.out.println("result="+result);
            User newUser = userDao.getByPK(user.getId());
            System.out.println(newUser);


        } catch (PersistException e) {
            e.printStackTrace();
        }

        return new ModelAndView("");
    }

}
