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

import taskManager.dao.EmptyParamException;
import taskManager.dao.NullPointParameterException;
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
    public void postUserController() {
        factory = new PostgreSqlDaoFactory();
        try {
            session = factory.getContext();
        } catch (PersistException e) {
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
            list = userDao.getAll();
            modelAndView.addObject("userListJSP", list);
        } catch (PersistException e) {
            e.printStackTrace();
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
        User user = null;
        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(session, User.class);
            user = Utils.userConvert(userweb);
            userDao.persist(user, false);
        } catch (PersistException e) {
            e.printStackTrace();
        } catch (NullPointParameterException e) {
            e.printStackTrace();
        } catch (EmptyParamException e) {
            e.printStackTrace();
        }

        model.addAttribute("success", "User " + user.getName() + " registered successfully");
        return "registrationsuccess";
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
        } catch (PersistException e) {
            e.printStackTrace();
        }

        modelAndView.addObject("userJSP", editUserWeb);
        modelAndView.addObject("edit", true);
        return modelAndView;
    }

    /**
     * This method will be called on form submission, handling POST request for
     * updating user in database.
     */
    @RequestMapping(value = { "/edit-user-{id}" }, method = RequestMethod.POST)
    public String updateUser(@ModelAttribute("userJSP") Userweb user, @PathVariable Integer id, ModelMap model) {

        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(session, User.class);
            User editUser = userDao.getByPK(id);

            editUser.setName(user.getName());
            editUser.setPassword(user.getPassword());

            userDao.update(editUser);
        } catch (PersistException e) {
            e.printStackTrace();
        }

        model.addAttribute("success", "User " + user.getName() + " updated successfully");
        return "registrationsuccess";
    }

    /**
     * This method will delete an user by it's id value.
     */
    @RequestMapping(value = { "/delete-user-{id}" }, method = RequestMethod.GET)
    public String deleteUser(@PathVariable Integer id) {

        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(session, User.class);
            userDao.delete(id);
        } catch (PersistException e) {
            e.printStackTrace();
        }

        return "redirect:/userslist";
    }
}
