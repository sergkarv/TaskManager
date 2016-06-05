package taskManager.importXML;

import org.springframework.web.multipart.MultipartFile;

public class ImportXml {
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }
    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
