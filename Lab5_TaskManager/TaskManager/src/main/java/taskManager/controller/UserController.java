package taskManager.controller;

import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.util.List;

import taskManager.dao.PersistException;
import taskManager.domain.User;
import taskManager.domain.Userweb;
import taskManager.postgreSql.PostgreSqlDaoFactory;
import taskManager.postgreSql.PostgreSqlUserDao;
import taskManager.utils.Utils;


@Controller
public class UserController {

    private PostgreSqlDaoFactory factory;
    private Session session;

    @PostConstruct
    public void postUserController(){
        factory = new PostgreSqlDaoFactory();
        try {
            session = factory.getContext();
        } catch (PersistException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/userslist", method = RequestMethod.GET)
    public ModelAndView usersList() {
        ModelAndView modelAndView = new ModelAndView();

        //name of the view where you want to go
        modelAndView.setViewName("userslist");
        List<User> list = null;
        try {

            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(session, User.class);
            session = factory.getContext();
            list = userDao.getAll();
            modelAndView.addObject("userListJSP", list);
        } catch (PersistException e) {
            modelAndView.setViewName("errorPage");
            modelAndView.addObject("error", e.getMessage());
            modelAndView.addObject("URLPage","/check-user");
            modelAndView.addObject("namePage","Main Page");
        }

        return modelAndView;
    }

    /**
     * This method will provide the medium to add a new user.
     */
    @RequestMapping(value = {"/newuser"} , method = RequestMethod.GET)
    public ModelAndView newUser() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration");
        Userweb user = new Userweb();
        modelAndView.addObject("userJSP", user);
        modelAndView.addObject("edit", false);
        return modelAndView;
    }

    @RequestMapping(value = {"/newuser"}, method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("userJSP") Userweb userweb, ModelMap model) {
        String resultPage = null;
        User user = null;
        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(session, User.class);
            user = Utils.userConvert(userweb);
            userDao.persist(user, false);
            resultPage = "registrationsuccess";
            model.addAttribute("success", "User " + user.getName() + " registered successfully");
        } catch (PersistException e) {
            resultPage = "errorPage";
            model.addAttribute("error", e.getMessage());
            model.addAttribute("URLPage", "/check-user");
            model.addAttribute("namePage", "Main Page");
        }catch (RuntimeException e){
            System.err.println(e);
            e.printStackTrace();
            resultPage = "errorPage";
            model.addAttribute("error", "System Error! A data-entry error!");
            model.addAttribute("URLPage","/check-user");
            model.addAttribute("namePage","Main Page");
        }

        return resultPage;
    }

    /**
     * This method will provide the medium to update an existing user.
     */
    @RequestMapping(value = { "/edit-user-{id}" }, method = RequestMethod.GET)
    public ModelAndView editUser(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration");
        User editUser = null;
        Userweb editUserWeb = null;
        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(session, User.class);
            editUser = userDao.getByPK(id);
            editUserWeb = Utils.userConvert(editUser);
            modelAndView.addObject("userJSP", editUserWeb);
            modelAndView.addObject("edit", true);
        } catch (PersistException e) {
            modelAndView.setViewName("errorPage");
            modelAndView.addObject("error", e.getMessage());
            modelAndView.addObject("URLPage","/check-user");
            modelAndView.addObject("namePage","Main Page");
        }

        return modelAndView;
    }

    /**
     * This method will be called on form submission, handling POST request for
     * updating user in database.
     */
    @RequestMapping(value = { "/edit-user-{id}" }, method = RequestMethod.POST)
    public String updateUser(@ModelAttribute("userJSP") Userweb user, @PathVariable Integer id, ModelMap model) {
        String resultPage = null;
        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(session, User.class);
            User editUser = userDao.getByPK(id);

            editUser.setName(user.getName());
            editUser.setPassword(user.getPassword());

            userDao.update(editUser);
            model.addAttribute("success", "User " + user.getName() + " updated successfully");
            resultPage = "registrationsuccess";
        } catch (PersistException e) {
            resultPage = "errorPage";
            model.addAttribute("error", e.getMessage());
            model.addAttribute("URLPage","/check-user");
            model.addAttribute("namePage","Main Page");
        }

        return resultPage;
    }

    /**
     * This method will delete an user by it's id value.
     */
    @RequestMapping(value = { "/delete-user-{id}" }, method = RequestMethod.GET)
    public Object deleteUser(@PathVariable Integer id) {
        Object resultPage = null;
        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(session, User.class);
            userDao.delete(id);
            resultPage = "redirect:/userslist";
        } catch (PersistException e) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("errorPage");
            modelAndView.addObject("error", e.getMessage());
            modelAndView.addObject("URLPage","/check-user");
            modelAndView.addObject("namePage","Main Page");
            resultPage = modelAndView;
        }

        return resultPage;
    }
}
