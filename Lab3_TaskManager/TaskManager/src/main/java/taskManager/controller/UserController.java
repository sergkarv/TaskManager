package taskManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import taskManager.dao.DaoFactory;
import taskManager.dao.EmptyParamException;
import taskManager.dao.NullPointParameterException;
import taskManager.dao.PersistException;
import taskManager.domain.User;
import taskManager.postgreSql.PostgreSqlUserDao;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.util.List;


@Controller
public class UserController {

    @Autowired
    private DaoFactory<Connection> daoFactory;

    private Connection connection;

    @PostConstruct
    public void postUserController() {
        try {
            connection = daoFactory.getContext();
        } catch (PersistException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/userslist", method = RequestMethod.GET)
    public ModelAndView usersList() {
        ModelAndView modelAndView = new ModelAndView();

        //имя представления, куда нужно будет перейти
        modelAndView.setViewName("userslist");
        List<User> list = null;
        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) daoFactory.getDao(connection, User.class);
            list = userDao.getAll();
            modelAndView.addObject("userListJSP", list);
        } catch (PersistException e) {
            e.printStackTrace();
        }

        return modelAndView; //после уйдем на представление, указанное чуть выше, если оно будет найдено.
    }

    /**
     * This method will provide the medium to add a new user.
     */
    @RequestMapping(value = "/newuser" , method = RequestMethod.GET)
    public ModelAndView newUser() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration");
        User user = new User();
        modelAndView.addObject("userJSP", user);
        modelAndView.addObject("edit", false);
        return modelAndView;
    }

    @RequestMapping(value = "/newuser", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("userJSP") User user, ModelMap model) {


        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) daoFactory.getDao(connection, User.class);
            userDao.create(user);
        } catch (PersistException e) {
            e.printStackTrace();
        } catch (NullPointParameterException e) {
            e.printStackTrace();
        } catch (EmptyParamException e) {
            e.printStackTrace();
        }

        model.addAttribute("success", "User " + user.getName() + " registered successfully");
        //return "success";
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
        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) daoFactory.getDao(connection, User.class);
            editUser = userDao.getByPK(id);
        } catch (PersistException e) {
            e.printStackTrace();
        }

        modelAndView.addObject("userJSP", editUser);
        modelAndView.addObject("edit", true);
        return modelAndView;
    }

    /**
     * This method will be called on form submission, handling POST request for
     * updating user in database. It also validates the user input
     */
    @RequestMapping(value = { "/edit-user-{id}" }, method = RequestMethod.POST)
    public String updateUser(@ModelAttribute("userJSP") User user, @PathVariable Integer id, ModelMap model) {

        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) daoFactory.getDao(connection, User.class);
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
     * This method will delete an user by it's SSOID value.
     */
    @RequestMapping(value = { "/delete-user-{id}" }, method = RequestMethod.GET)
    public String deleteUser(@PathVariable Integer id) {

        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) daoFactory.getDao(connection, User.class);
            userDao.delete(userDao.getByPK(id));
        } catch (PersistException e) {
            e.printStackTrace();
        }

        return "redirect:/userslist";
    }
}
