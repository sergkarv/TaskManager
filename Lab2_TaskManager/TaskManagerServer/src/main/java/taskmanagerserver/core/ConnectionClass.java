package taskmanagerserver.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import javax.swing.JOptionPane;
import protocol.Command;
import protocol.DataPackage;
import task.Task;
import taskmanagerserver.GUI;
import user.User;

public class ConnectionClass implements Listener {
    //update
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Thread receiveThread;
    private GUI gui;
    private static List<Long> alertTasks = new ArrayList<>();
    private User connectUser;
    private TaskManager manager;
    
    public ConnectionClass(Socket s, final GUI gui, final TaskManager manager) throws IOException {
        this.gui = gui;
        in = new ObjectInputStream(s.getInputStream());
        out = new ObjectOutputStream(s.getOutputStream());
        this.manager = manager;

        receiveThread = new Thread() {
            @Override
            public void run() {
                DataPackage receivedCommand = null;
                while (true) {
                    try {
                        receivedCommand = (DataPackage) in.readObject();
                        //Command com = receivedCommand.getName();

                        switch (receivedCommand.getName()) {

                            case NAME_USER: {
                                synchronized (ConnectionClass.this.manager) {
                                    String userName = (String) receivedCommand.getValue();
                                    boolean flag
                                            = manager.containsUser(userName);
                                    if (flag) {
                                        User user = manager.searchUser(userName);
                                        connectUser = user;
                                    } else {
                                        User user = new User(userName);
                                        manager.addUser(user);
                                        connectUser = user;
                                        gui.addUserToComboBox(user);
                                    }
                                }

                                ConnectionClass.this.gui.update();
                                //sendTaskAndAlertTask();
                                break;
                            }
                            case ADD: {
                                synchronized (ConnectionClass.this.manager) {
                                    Task task = (Task) receivedCommand.getValue();
                                    task.setUser(connectUser);
                                    manager.addTask(task);
                                }
                                ConnectionClass.this.gui.update();
                                break;
                            }
                            case REMOVE: {
                                long idTask = (long) receivedCommand
                                        .getValue();
                                synchronized (ConnectionClass.this.manager) {
                                    synchronized (alertTasks) {
                                        alertTasks.remove(idTask);
                                        manager.removeTask(idTask);
                                    }
                                }
                                ConnectionClass.this.gui.update();
                                break;
                            }
                            case EDIT: {
                                synchronized (ConnectionClass.this.manager) {
                                    Task task = (Task) receivedCommand
                                            .getValue();

                                    long index = task.getId();
                                    //если задача была активной
                                    //после каждого изменения задача считается отложенной
                                    if (!task.getWorkOnTask()) {
                                        synchronized (alertTasks) {
                                            alertTasks.remove(index);
                                        }
                                    }
                                    synchronized (ConnectionClass.this.manager) {
                                        manager.setTask(task.getId(), task.getName(),
                                                task.getDescription(), task.getContacts(),
                                                task.getDate(), task.isFinished(),
                                                task.isHighPriority(), task.getSoundFileName(),
                                                task.getWorkOnTask(), connectUser);
                                    }

                                    ConnectionClass.this.gui.update();
                                }
                                break;
                            }
                        }
                    } catch (SocketException e) {
                        JOptionPane.showMessageDialog(ConnectionClass.this.gui,
                                "Потеряно соединение с клиентом",
                                "Ошибка", JOptionPane.INFORMATION_MESSAGE);
                        ConnectionClass.this.disconnect(ConnectionClass.this.gui);
                        break;
                    } catch (ClassNotFoundException | IOException ex) {
                        JOptionPane.showMessageDialog(ConnectionClass.this.gui,
                                "Ошибка при получении команды от клиента",
                                "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };

        receiveThread.start();
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

    //используется в gui после update, add, remove, new connect to client 
    public void sendTaskAndAlertTask(){
        Collection<Task> tasks= null;
        synchronized (this.manager) {
            tasks = manager.getCollectionTasksForUser(connectUser.getId());
            //manager.getCompletedTasks();
        }
        
        //если задача удалена на сервере, но была в работе
        //найти не существующий id в alertTasks и удалить
        synchronized (this.alertTasks){
            
            boolean flag = false;
            for(long id : alertTasks){
                flag = false;
                for(Task task : tasks){
                    if(task.getId() == id ){
                        //на случай редактирования задачи,
                        //которая была активной
                        if(!task.getWorkOnTask()){
                            flag = false;
                            break;
                        }
                        flag = true;
                    }
                }
                if(!flag){
                    alertTasks.remove(id);
                }
            }
            DataPackage data
                    = new DataPackage(Command.ALL_TASKS_AND_ALERT_SILENT, tasks, alertTasks);
            this.sendData(data);
        }
    }
    
    //оповещаем пользователя о наступивших задачах
    public void sendAlertTasks(List<Task> tasks) {
       //ArrayList<Task> list = new ArrayList<>();
        synchronized (this.alertTasks) {
            for (Task task : tasks) {
                if (task.getUser() == connectUser) {
                    //list.add(task);
                    alertTasks.add(task.getId());//только здесь добавляется
                }
            }
            Collection<Task> tasksUser = null;
            synchronized(this.manager){
                tasksUser = manager.getCollectionTasksForUser(connectUser.getId());
            }
            DataPackage data = new DataPackage(Command.ALL_TASKS_AND_ALERT, tasksUser, alertTasks);
            this.sendData(data);
        }
    }

    private void disconnect(GUI gui) {
        receiveThread.interrupt();
        gui.disconnect(this);
    }

    @Override
    public void update(Object value) {
        List<Task> tasks = (List<Task>)value;//!!! Warning
        sendAlertTasks(tasks);
    }

}
