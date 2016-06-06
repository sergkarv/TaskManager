package taskManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import taskManager.beans.XMLService;
import taskManager.domain.Task;
import taskManager.domain.User;
import taskManager.importXML.ImportXml;
import taskManager.importXML.XmlValidator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Controller
@Scope(value = "request")
public class ImportController {

    @Autowired
    private XmlValidator fileValidator; //check size == 0

    @Autowired
    private XMLService service;

    @RequestMapping(value = "/import_xml", method = RequestMethod.GET)
     public ModelAndView importXmlGet() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("import");
        return modelAndView;
    }

    @RequestMapping(value = "/import_xml", method = RequestMethod.POST)
    public ModelAndView importXmlPost( @ModelAttribute("XMLFile") ImportXml uploadedFile, ModelMap model,
                                       HttpServletRequest request, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();

        String value = request.getParameter("type_import");
        String fileName = null;

        MultipartFile file = uploadedFile.getFile();
        fileValidator.validate(uploadedFile, result);
        if (result.hasErrors()) {
            modelAndView.setViewName("import");
            return modelAndView;
        }

        try {
            byte[] bytes = file.getBytes();
            fileName = file.getOriginalFilename();
            System.out.println(fileName);
            String rootPath = "C:\\importXML\\";
            File dir = new File(rootPath + File.separator + "loadFiles");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            //save file in other path( on the server)
            String path = dir.getAbsolutePath() + File.separator + fileName;
            File loadFile = new File(dir.getAbsolutePath() + File.separator + fileName);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(loadFile));
            stream.write(bytes);
            stream.flush();
            stream.close();
            boolean flagSuccess = false;

            if(value.equals("user")){
                flagSuccess = service.load(path, User.class, result);
            }
            else{
                flagSuccess = service.load(path, Task.class, result);
            }

            if(!flagSuccess){
                modelAndView.setViewName("import");//and transmit error messege
            }
            else{
                modelAndView.setViewName("importSuccess");
                model.addAttribute("success", "Import from XML File successfully");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return modelAndView;
    }
}
