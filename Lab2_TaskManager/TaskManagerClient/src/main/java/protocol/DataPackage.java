package protocol;

import java.io.Serializable;

public class DataPackage implements Serializable {

    //Наименование команды
    private Command name;
    private Object value;
    private Object alert;
    
    public DataPackage(Command name) {
        this(name, null, null);
    }

    public DataPackage(Command name, Object params, Object alert) {
        this.name = name;
        this.value = params;
        this.alert = alert;
    }

    public Command getName() {
        return this.name;
    }

    public Object getValue() {
        return this.value;
    }
    
    public Object getAlert(){
        return alert;
    }
}
