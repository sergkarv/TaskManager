package taskmanagerclient.core;


import task.Task;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import protocol.Command;
import protocol.Command;
import protocol.DataPackage;
import taskmanagerclient.GUI;

public class ConnectionClass {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    //private Thread sendThread;
    private Thread receiveThread;    
    private Socket socket;
    private GUI gui;
    private final String STANDART_SIGNAL = "signal/bell.wav";
    private String nameUser;
    //private List alertTasks = new ArrayList();
    private Collection<Task> allUserTasks;
    private List<Long> alertTasks;

    public ConnectionClass(String address, int port, String nameUser, final GUI gui)
            throws UnknownHostException, IOException {
        socket = new Socket(address, port);
        this.nameUser = nameUser;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        this.gui = gui;
        
        receiveThread = new Thread() {
            @Override
            public void run() {
                DataPackage receivedCommand = null;
                while (true) {
                    try {
                        receivedCommand = (DataPackage) in.readObject();
                        switch (receivedCommand.getName()) {
                            //никак не используется; оставлено для возможного изменения кода
                            case FAIL : {
                                JOptionPane.showMessageDialog(ConnectionClass.this.gui,
                                        "Операция была завершена некорректно.",
                                        "Ошибка", JOptionPane.ERROR_MESSAGE);
                                gui.setEnabled(true);
                                break;
                            }

                            case ALL_TASKS_AND_ALERT : {
                                //получаем все данные при первом обращении
                                Collection<Task> tasks =
                                        (Collection<Task>) receivedCommand.getValue();
                                List<Long> alertList = (List<Long>) receivedCommand
                                        .getAlert();
                                gui.setEnabled(true);
                                ConnectionClass.this.allUserTasks = tasks;
                                ConnectionClass.this.alertTasks  = alertList;
                                gui.fillTable(tasks);
                                gui.alert(alertList, true, true);
                                break;
                            }
                            case ALL_TASKS_AND_ALERT_SILENT : {
                                Collection<Task> tasks = (Collection<Task>)
                                        receivedCommand.getValue();
                                List<Long> alertList = (List<Long>) receivedCommand
                                        .getAlert();
                                gui.setEnabled(true);
                                ConnectionClass.this.allUserTasks = tasks;
                                ConnectionClass.this.alertTasks = alertList;
                                gui.fillTable(tasks);
                                gui.alert(alertList, false, false);
                                break;
                            }
//                            //                            
                        }
                    } catch (SocketException e) {
                        JOptionPane.showMessageDialog(ConnectionClass.this.gui,
                                "Соединение с сервером потеряно",
                                "Ошибка", JOptionPane.ERROR_MESSAGE);
                        //ConnectionClass.this.disconnect(gui);
                        try {
                            //sendThread.interrupt();
                            Thread.currentThread().interrupt();
                            in.close();
                            out.close();
                            gui.disconnect();
                        } catch (IOException ex) {
                            System.out.println("Fail");
                            System.out.println(ex.getMessage());
                        }
                        break;
                    } catch (IOException | ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(gui,
                                "Ошибка при получении команды от сервера",
                                "Ошибка", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                }
            }
        };
        receiveThread.start();
    }

    public List<Long> getAlertTasks() {
        return Collections.unmodifiableList(alertTasks);
    }

    public Collection<Task> getAllUserTasks() {
        return Collections.unmodifiableCollection(allUserTasks);
    }
    
    public Task getTask(long id){
        for(Task task : allUserTasks){
            if(task.getId() == id){
                return task;
            }
        }
        return null;
    }
    
    //отправляем пользователю нужные задания упакованные в объект класс DataPackage
    public void sendData(DataPackage data) {
        if (data != null) {
            try {

                out.flush();
                out.writeObject(data);
                out.flush();
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(gui,
                        "Ошибка при передаче команды клиенту",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void addTask(String name, String description, String contacts,
            Calendar date, boolean isFinished, boolean isHighPriority) {
        Task task = new Task(name, description, contacts, date, isFinished, 
                isHighPriority, STANDART_SIGNAL, null);
        DataPackage data = new DataPackage(Command.ADD, task, null);
        this.sendData(data);
    }

    public void setTask(Task task) {
        DataPackage data = new DataPackage(Command.EDIT, task, null);
        this.sendData(data);
    }

    public void removeTask(long id) {
        DataPackage data = new DataPackage(Command.REMOVE, id, null);
        this.sendData(data);
    }

    public void getAllTasks(String nameUser) {
        DataPackage data = new DataPackage(Command.NAME_USER, nameUser, null);
        this.sendData(data);
    }

}
