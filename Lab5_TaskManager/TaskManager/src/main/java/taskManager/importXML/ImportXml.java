package taskManager.importXML;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Сергей on 28.04.16.
 */
public class ImportXml {
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }
    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
