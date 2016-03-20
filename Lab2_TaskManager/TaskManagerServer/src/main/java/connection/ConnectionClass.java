package connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JOptionPane;
import protocol.Command;
import protocol.DataPackage;
import task.Task;
import taskmanagerserver.GUI;
import socket.ServerSocketListener;
import taskManager.TaskManager;
import user.User;

public class ConnectionClass implements Listener {
    //update
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Thread receiveThread;
    private GUI gui;
    private List<Long> alertTasks;
    private User connectUser;
    private TaskManager manager;
    private ServerSocketListener listener;
    
    public ConnectionClass(Socket s, final GUI gui, final TaskManager manager,
            ServerSocketListener ssl) throws IOException {
        alertTasks = new ArrayList<>();
        this.gui = gui;
        in = new ObjectInputStream(s.getInputStream());
        out = new ObjectOutputStream(s.getOutputStream());
        this.manager = manager;
        this.listener = ssl;

        receiveThread = new Thread() {
            @Override
            public void run() {
                DataPackage receivedCommand = null;
                while (true) {
                    try {
                        receivedCommand = (DataPackage) in.readObject();

                        switch (receivedCommand.getName()) {

                            case NAME_USER: {
                                synchronized (ConnectionClass.this.manager) {
                                    String userName = (String) receivedCommand.getValue();
                                    boolean flag
                                            = ConnectionClass.this.manager.containsUser(userName);
                                    if (flag) {
                                        User user = ConnectionClass.this.manager.searchUser(userName);
                                        connectUser = user;
                                    } else {
                                        User user = new User(userName);
                                        ConnectionClass.this.manager.addUser(user);
                                        connectUser = user;                                        
                                        gui.addUserToComboBox(user);                                        
                                    }
                                }
                                ConnectionClass.this.gui.update();
                                ConnectionClass.this.listener.notifyConnections();
                                break;
                            }
                            case ADD: {
                                synchronized (ConnectionClass.this.manager) {
                                    Task task = (Task) receivedCommand.getValue();
                                    task.setUser(connectUser);
                                    ConnectionClass.this.manager.addTask(task);
                                }
                                ConnectionClass.this.gui.update();
                                ConnectionClass.this.listener.notifyConnections();
                                break;
                            }
                            case REMOVE: {
                                long idTask = (long) receivedCommand
                                        .getValue();
                                synchronized (ConnectionClass.this.manager) {
                                    synchronized (alertTasks) {
                                        alertTasks.remove(idTask);
                                        ConnectionClass.this.manager.removeTask(idTask);
                                    }
                                }
                                ConnectionClass.this.gui.update();
                                ConnectionClass.this.listener.notifyConnections();
                                break;
                            }
                            case EDIT: {
                                synchronized (ConnectionClass.this.manager) {
                                    Task task = (Task) receivedCommand
                                            .getValue();

                                    long index = task.getId();
                                    //если задача была активной
                                    //после каждого изменения задача считается отложенной
                                    //при откладывании флаг workOnTask изменяется
                                    //на false на клиенте
                                    if (!task.getWorkOnTask()) {
                                        synchronized (alertTasks) {
                                            alertTasks.remove(index);
                                        }
                                    }
                                    ConnectionClass.this.manager.
                                            setTask(task.getId(), task.getName(),
                                                    task.getDescription(), task.getContacts(),
                                                    task.getDate(), task.isFinished(),
                                                    task.isHighPriority(),
                                                    task.getWorkOnTask(), connectUser);
                                }
                                ConnectionClass.this.gui.update();
                                ConnectionClass.this.listener.notifyConnections();
                                break;
                            }
                        }
                    } catch (SocketException e) {
                        JOptionPane.showMessageDialog(ConnectionClass.this.gui,
                                "Потеряно соединение с клиентом",
                                "Ошибка", JOptionPane.INFORMATION_MESSAGE);
                        ConnectionClass.this.listener.disconnect(ConnectionClass.this);
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
                out.writeObject(data);
                //out.flush();
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
        synchronized (manager) {
            tasks = manager.getCollectionTasksForUser(connectUser.getId());
        }
        
        //если задача удалена на сервере, но была в работе
        //найти не существующий id в alertTasks и удалить
        synchronized (alertTasks){
            ArrayList<Long> removeIdList = new ArrayList<>();
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
                    //alertTasks.remove(id);
                    removeIdList.add(id);
                }
            }
            
            for(long id : removeIdList){
                alertTasks.remove(id);
            }
            removeIdList.clear();
            ArrayList<Long> list = new ArrayList<>(alertTasks);
            DataPackage data
                    = new DataPackage(Command.ALL_TASKS_AND_ALERT_SILENT, tasks, list);
            this.sendData(data);
        }
    }
    
    //оповещаем пользователя о наступивших задачах
    public void sendAlertTasks(List<Task> tasks) {
        synchronized (alertTasks) {
            for (Task task : tasks) {
                if (task.getUser() == connectUser) {
                    //list.add(task);
                    alertTasks.add(task.getId());//только здесь добавляется
                }
            }
            if(alertTasks.isEmpty()){
                return;
            }
            Collection<Task> tasksUser = null;
            synchronized(manager){
                tasksUser = manager.getCollectionTasksForUser(connectUser.getId());
            }
            ArrayList<Long> list = new ArrayList<>(alertTasks);
            DataPackage data = new DataPackage(Command.ALL_TASKS_AND_ALERT, tasksUser, list);
            this.sendData(data);
        }
    }

//    private void disconnect(GUI gui) {
//        receiveThread.interrupt();
//        gui.disconnect(this);
//    }

    @Override
    public void update(Object value) {
        List<Task> tasks = (List<Task>)value;
        sendAlertTasks(tasks);
    }

}
