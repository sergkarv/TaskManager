package taskManager.controller;

import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import taskManager.beans.XMLService;
import taskManager.dao.PersistException;
import taskManager.domain.Task;
import taskManager.domain.User;
import taskManager.postgreSql.PostgreSqlDaoFactory;
import taskManager.postgreSql.PostgreSqlTaskDao;
import taskManager.postgreSql.PostgreSqlUserDao;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;


@Controller
@Scope(value = "request")
public class ExportController {
    @Autowired
    private XMLService service;

    private PostgreSqlDaoFactory factory;
    private Session session;

    private static String pathDirXML = null;

    @PostConstruct
    public void postExportController() {
        factory = new PostgreSqlDaoFactory();
        try {
            session = factory.getContext();
        } catch (PersistException e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public ModelAndView exportXmlGet() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("export");
        return modelAndView;
    }

    @RequestMapping(value = "/export/export_task", method = RequestMethod.GET)
    public ModelAndView exportTaskMenuGet() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("exportTaskMenu");
        return modelAndView;
    }

    @RequestMapping(value = "/export/export_user", method = RequestMethod.GET)
    public ModelAndView exportUserMenuGet() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("exportUserMenu");
        return modelAndView;
    }

    @RequestMapping(value = "/export/export_user/equals_query", method = RequestMethod.GET)
    public ModelAndView exportUserEqualsGet() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("exportEqualsUser");
        return modelAndView;
    }

    @RequestMapping(value = "/export/export_task/equals_query", method = RequestMethod.GET)
    public ModelAndView exportTaskEqualsGet() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("exportEqualsTask");

        List<User> listUser = null;
        List<Task> listTask = null;
        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(session, User.class);
            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) factory.getDao(session, Task.class);
            listUser = userDao.getAll();
            listTask = taskDao.getAll();
            modelAndView.addObject("userListJSP", listUser);
            modelAndView.addObject("taskListJSP", listTask);
        } catch (PersistException e) {
            modelAndView.setViewName("errorPage");
            modelAndView.addObject("error", e.getMessage());
            modelAndView.addObject("URLPage","/export");
            modelAndView.addObject("namePage","Export Page");

        }

        return modelAndView;
    }

    @RequestMapping(value = "/export/export_task/equals_query", method = RequestMethod.POST)
    public String exportTaskEqualsPost(HttpServletRequest request, ModelMap model) {
        String resultPage = null;
        String value;
        Integer id;
        String name;
        String contacts;
        Integer idParent;
        Integer idUser;
        String rootPath = "C:\\exportXML\\";
        Random random = new Random();
        String fileName = "exportQueryTasks" + random.nextInt()+".xml";

        id = (Integer) getParam("Id", request);

        name = (String) getParam("Name", request);

        contacts = (String) getParam("Contacts", request);

        idParent = (Integer) getParam("parent", request);

        idUser = (Integer) getParam("user", request);

        List<Task> listTask = null;
        boolean flag = false;
        String valueParent = request.getParameter("parent");
        if(valueParent!= null){
            if(valueParent.equals("null")) flag = true;
        }
        try {
            if(id == null && name == null && contacts == null && idParent == null && idUser == null && !flag)
                throw new PersistException("Empty fields! Don't enter all empty fields. ");

            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) factory.getDao(session, Task.class);
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(session, User.class);
            Task parent = null;
            User user = null;
            if(idParent != null){
                parent = taskDao.getByPK(idParent);
            }
            if(idUser != null){
                user = userDao.getByPK(idUser);
            }

            listTask = taskDao.getByParameters(id, name, contacts, parent, user, flag);
            Collections.sort(listTask);

            File dir = new File(rootPath + File.separator + "saveFiles");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            ExportController.pathDirXML = dir.getAbsolutePath();
            //save file in other path
            String path = dir.getAbsolutePath() + File.separator + fileName;

            boolean flagCreate = service.save(path, listTask, Task.class);

            if(flagCreate){
                model.addAttribute("nameXML", fileName);
                model.addAttribute("downloadInfo",fileName + " is ready for downloading");
                resultPage = "download";
            }
            else{
                model.addAttribute("error", "Error! Error creating User XML file!");
                resultPage = "errorPage";
                model.addAttribute("URLPage","/export");
                model.addAttribute("namePage","Export Page");
            }

        } catch (PersistException e) {
            resultPage = "errorPage";
            model.addAttribute("error", e.getMessage());
            model.addAttribute("URLPage", "/export");
            model.addAttribute("namePage", "Export Page");
        }

        return resultPage;
    }

    @RequestMapping(value = "/export/export_user/equals_query", method = RequestMethod.POST)
    public String exportUserEqualsPost(HttpServletRequest request, ModelMap model) {
        String resultPage = null;
        String value;
        Integer id;
        String name;
        String pass;
        String rootPath = "C:\\exportXML\\";
        Random random = new Random();
        String fileName = "exportQueryUsers" + random.nextInt()+".xml";

        id = (Integer) getParam("Id", request);

        name = (String) getParam("Name", request);

        pass = (String) getParam("Password", request);

        List<User> listUser = null;
        try {
            PostgreSqlUserDao userDao = (PostgreSqlUserDao) factory.getDao(session, User.class);

            if(id == null && name == null && pass == null)
                throw new PersistException("Empty fields! Don't enter all empty fields. ");

            listUser = userDao.getByParameters(id, name, pass);
            Collections.sort(listUser);

            File dir = new File(rootPath + File.separator + "saveFiles");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            ExportController.pathDirXML = dir.getAbsolutePath();
            //save file in other path
            String path = dir.getAbsolutePath() + File.separator + fileName;

            boolean flagCreate = service.save(path, listUser, User.class);

            if(flagCreate){
                model.addAttribute("nameXML", fileName);
                model.addAttribute("downloadInfo",fileName + " is ready for downloading");
                resultPage = "download";
            }
            else{
                model.addAttribute("error", "Error! Error creating User XML file!");
                resultPage = "errorPage";
                model.addAttribute("URLPage","/export");
                model.addAttribute("namePage","Export Page");
            }

        } catch (PersistException e) {
            resultPage = "errorPage";
            model.addAttribute("error", e.getMessage());
            model.addAttribute("URLPage","/export");
            model.addAttribute("namePage","Export Page");
        }

        return resultPage;
    }

    @RequestMapping(value = "/export/export_user/get_all_users", method = RequestMethod.GET)
    public ModelAndView getAllUserGet(HttpServletRequest request,
                                      HttpServletResponse response, ModelMap model)  {
        ModelAndView modelAndView = new ModelAndView();

        String rootPath = "C:\\exportXML\\";
        Random random = new Random();
        String fileName = "exportAllUsers" + random.nextInt()+".xml";

        List<User> list = null;

        try {
            PostgreSqlUserDao taskDao = (PostgreSqlUserDao) factory.getDao(session, User.class);
            list = taskDao.getAll();
            Collections.sort(list);
        } catch (PersistException e) {
            modelAndView.setViewName("errorPage");
            modelAndView.addObject("error", e.getMessage());
            modelAndView.addObject("URLPage","/export");
            modelAndView.addObject("namePage","Export Page");
        }

        File dir = new File(rootPath + File.separator + "saveFiles");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        ExportController.pathDirXML = dir.getAbsolutePath();
        //save file in other path
        String path = dir.getAbsolutePath() + File.separator + fileName;

        boolean flagCreate = service.save(path, list, User.class);

        if(flagCreate){
            modelAndView.addObject("nameXML", fileName);
            modelAndView.addObject("downloadInfo",fileName + " is ready for downloading");
            modelAndView.setViewName("download");
        }
        else{
            modelAndView.addObject("error", "Error! Error creating User XML file!");
            modelAndView.setViewName("errorPage");
            modelAndView.addObject("URLPage","/export");
            modelAndView.addObject("namePage","Export Page");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/export/export_task/get_all_tasks", method = RequestMethod.GET)
    public ModelAndView getAllTaskGet(HttpServletRequest request,
                              HttpServletResponse response, ModelMap model)  {
        ModelAndView modelAndView = new ModelAndView();

        String rootPath = "C:\\exportXML\\";
        Random random = new Random();
        String fileName = "exportAllTasks" + random.nextInt()+".xml";

        List<Task> list = null;

        try {
            PostgreSqlTaskDao taskDao = (PostgreSqlTaskDao) factory.getDao(session, Task.class);
            list = taskDao.getAll();
            Collections.sort(list);
        } catch (PersistException e) {
            modelAndView.addObject("error", "Error! Error creating User XML file!");
            modelAndView.setViewName("errorPage");
            modelAndView.addObject("URLPage","/export");
            modelAndView.addObject("namePage", "Export Page");
            return modelAndView;
        }

        File dir = new File(rootPath + File.separator + "saveFiles");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        ExportController.pathDirXML = dir.getAbsolutePath();
        //save file in other path
        String path = dir.getAbsolutePath() + File.separator + fileName;

        boolean flagCreate = service.save(path, list, Task.class);

        if(flagCreate){
            modelAndView.addObject("nameXML", fileName);
            modelAndView.addObject("downloadInfo",fileName + " is ready for downloading");
            modelAndView.setViewName("download");
        }
        else{
            modelAndView.addObject("error", "Error! Error creating User XML file!");
            modelAndView.setViewName("errorPage");
            modelAndView.addObject("URLPage","/export");
            modelAndView.addObject("namePage", "Export Page");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/export/download_xml-{nameXMLFile}", method = RequestMethod.GET)
    public @ResponseBody Object downloadXmlUserGet(
                     @PathVariable String nameXMLFile,
                                   HttpServletRequest request,
                                   HttpServletResponse response
                                   )  {
        try {
            downloadOrViewXMLFile(nameXMLFile, request, response, true);
        } catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView();
            System.err.println(e);
            e.printStackTrace();
            modelAndView.addObject("error", "System Error!");
            modelAndView.setViewName("errorPage");
            modelAndView.addObject("URLPage","/export");
            modelAndView.addObject("namePage", "Export Page");
            return modelAndView;
        }
        return null;
    }

    @RequestMapping(value = "/export/view_xml-{nameXMLFile}", method = RequestMethod.GET)
    public @ResponseBody Object viewXmlUserGet(
            @PathVariable String nameXMLFile,
            HttpServletRequest request,
            HttpServletResponse response
    )  {
        try {
            downloadOrViewXMLFile(nameXMLFile, request, response, false);
        } catch (Exception e) {
            ModelAndView modelAndView = new ModelAndView();
            System.err.println(e);
            e.printStackTrace();
            modelAndView.addObject("error", "System Error!");
            modelAndView.setViewName("errorPage");
            modelAndView.addObject("URLPage","/export");
            modelAndView.addObject("namePage", "Export Page");
            return modelAndView;
        }
        return null;
    }

    private void downloadOrViewXMLFile(String nameXMLFile,
                                       HttpServletRequest request,
                                       HttpServletResponse response,
                                       boolean flagMode)throws Exception{

        ServletContext context = request.getServletContext();
        String path = ExportController.pathDirXML + File.separator + nameXMLFile+".xml";
        File saveFile = new File(path);
        //FileInputStream inputStream = null;
        OutputStream outStream = null;
        try(FileInputStream inputStream = new FileInputStream(saveFile)) {
            // get MIME type of the file
            String mimeType = null;
            if(flagMode){
                mimeType = "application/octet‐stream";
            }
            else{
                mimeType = context.getMimeType(path);
            }
            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "text/xml";
            }

            // set content attributes for the response
            response.setContentType(mimeType);
            response.setContentLength((int) saveFile.length());

            // set headers for the response
            String headerKey = "Content‐Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"",
                    saveFile.getName());
            response.setHeader(headerKey, headerValue);

            // get output stream of the response
            outStream = response.getOutputStream();
            IOUtils.copy(inputStream, outStream);

        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }

    }

    private Object getParam(String param, HttpServletRequest request){
        Object object = null;
        String value = value = request.getParameter(param);

        switch(param){
            case "Id":
                if(value != null){
                    if(!value.equals("")){
                        object = Integer.valueOf(value);
                    }
                }
                break;
            case "Name":
            case "Password":
            case "Contacts":
                if(value != null){
                    if(!value.equals("")){
                        object = value;
                    }
                }
                break;
            case "user":
                String idUserString = null;
                if(value != null){
                    if(!value.equals("")){
                        idUserString = value.substring( value.indexOf('(')+1, value.length()-1 );
                        object = Integer.valueOf(idUserString);
                    }
                }
                break;
            case "parent":
                String idParentString = null;
                if(value != null){
                    if(!value.equals("null")&&!value.equals("")){
                        idParentString = value.substring( value.indexOf('(')+1, value.length()-1 );
                        object = Integer.valueOf(idParentString);
                    }
                }
                break;
        }

        return  object;
    }

}
