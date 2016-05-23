package taskManager.importXML;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class XmlValidator implements Validator {
    @Override
    public void validate(Object uploadedFile, Errors errors) {
        ImportXml file = (ImportXml) uploadedFile;
        if (file.getFile().getSize() == 0) {
            errors.rejectValue("file", "uploadForm.selectFile", "Please select a file!");
        }
    }

    public void genereteError(Errors errors, String message){
        errors.rejectValue("file", "uploadForm.selectFile", message);
    }

    @Override
    public boolean supports(Class<?> clazz) {
// TODO Auto-generated method stub
        return false;
    }
}
