package taskmanagerclient.core;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author admin
 */
public class DefaultTableModelNotEdit extends DefaultTableModel{
    public DefaultTableModelNotEdit(){
        super();
    }
    
    public DefaultTableModelNotEdit(int rowCount, int columnCount){
        super(rowCount, columnCount);
    }
    
    public DefaultTableModelNotEdit(Vector columnNames, int rowCount){
        super(columnNames, rowCount);
    }
    
    public DefaultTableModelNotEdit(Vector dataVector, Vector columnNames){
        super(dataVector, columnNames);
    }
    
    public DefaultTableModelNotEdit(Object[] columnNames, int rowCount){
        super(columnNames, rowCount);
    }
    
    public DefaultTableModelNotEdit(Object[][] data, Object[] columnNames){
        super(data, columnNames);
    }
    
    boolean[] canEdit = new boolean [] {
        false, false, false
    };
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
    }
}
