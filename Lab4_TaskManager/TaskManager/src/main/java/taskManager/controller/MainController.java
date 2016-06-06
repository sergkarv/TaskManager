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
        try {
            connection = daoFactory.getContext();
        } catch (PersistException e) {
            e.printStackTrace();
        }
    }

    /*Get here at application start  */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView main() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userJSP", new User());
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping(value = "/check-user")
    public ModelAndView checkUser(@ModelAttribute("userJSP") User user) {
        ModelAndView modelAndView = new ModelAndView();

        //name of the view where you want to go
        modelAndView.setViewName("mainPage");

        //modelAndView.addObject("userJSP", user);
        //modelAndView.addObject("taskJSP", new Task());
        return modelAndView;
    }

}
