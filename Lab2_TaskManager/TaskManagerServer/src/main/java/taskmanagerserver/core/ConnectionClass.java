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

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Thread sendThread;
    private Thread receiveThread;
    private volatile DataPackage dataPackage;
    private GUI gui;
    private static List<Long> alertTasks = new ArrayList<>();
    private User connectUser;
    private TaskManager manager;
    
    public ConnectionClass(Socket s, final GUI gui, final TaskManager manager) throws IOException {
        this.gui = gui;
        in = new ObjectInputStream(s.getInputStream());
        out = new ObjectOutputStream(s.getOutputStream());
        this.manager = manager;
        //отправляем пользователю нужные задания упакованные в объект класс DataPackage
        sendThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (dataPackage != null) {
                        try {

                            out.flush();
                            out.writeObject(dataPackage);
                            out.flush();
                        } catch (IOException ioe) {
                            JOptionPane.showMessageDialog(gui,
                                    "Ошибка при передаче команды клиенту",
                                    "Ошибка",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        dataPackage = null;
                    }

                }
            }
        };
        receiveThread = new Thread() {
            @Override
            public void run() {
                DataPackage receivedCommand = null;
                while (true) {
                    try {
                        receivedCommand = (DataPackage) in.readObject();
                        //Command com = receivedCommand.getName();
                        
                        switch (receivedCommand.getName()) {
                            
                            case NAME_USER : {
                                synchronized (manager) {
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
                                    }
                                }
                                
                                gui.update();
                                sendTaskAndAlertTask();
                                break;
                            }
                            case ADD : {
                                synchronized (manager) {
                                    Task task = (Task)receivedCommand.getValue();
                                    task.setUser(connectUser);
                                    manager.addTask(task);
                                }
                                gui.update();
                                break;
                            }
                            case REMOVE : {
                                try {
                                    long idTask = (long) receivedCommand
                                            .getValue();
                                    synchronized (manager) {
                                        synchronized(alertTasks){
                                            alertTasks.remove(idTask);
                                            manager.removeTask(idTask);
                                        }
                                    }
                                    gui.update();
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    dataPackage = new DataPackage(Command.FAIL);
                                }
                                break;
                            }
                            case REMOVE_ALERT : {//вероятно лишнее
                                try {
                                    long idTask = (long) receivedCommand
                                            .getValue();
                                    synchronized (manager) {
                                        synchronized(alertTasks){
                                            alertTasks.remove(idTask);
                                            manager.removeTask(idTask);
                                        }
                                    }
                                    gui.update();
                                } catch (ArrayIndexOutOfBoundsException ex) {
                                    dataPackage = new DataPackage(Command.FAIL);
                                }
                                break;
                            }
                            case EDIT : {
                                synchronized (manager) {
                                    Task task = (Task) receivedCommand
                                            .getValue();
                                    try {

                                        long index = task.getId();
                                        if(!task.getWorkOnTask()){
                                            synchronized(alertTasks){
                                                alertTasks.remove(index);
                                            }                                            
                                        }
                                        synchronized(manager){
                                            manager.setTask(task.getId(), task.getName(),
                                                    task.getDescription(), task.getContacts(),
                                                    task.getDate(), task.isFinished(),
                                                    task.isHighPriority(), task.getSoundFileName(),
                                                    task.getWorkOnTask(), connectUser);
                                        }
                                                                                
                                        gui.update();
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        dataPackage = new DataPackage(Command.FAIL);
                                    }                                                                        
                                }
                                break;
                            }
                            case EDIT_ALERT : {
                                synchronized (manager) {
                                    Task task = (Task) receivedCommand
                                            .getValue();
                                    try {
                                        synchronized(alertTasks){
                                            alertTasks.remove(task.getId());
                                        }                                        
                                        task.setWorkOnTask(false);
                                        manager.setTask(task.getId(), task.getName(),
                                                    task.getDescription(), task.getContacts(),
                                                    task.getDate(), task.isFinished(),
                                                    task.isHighPriority(), task.getSoundFileName(),
                                                    task.getWorkOnTask(), connectUser);
                                        //manager.removeTask(task.getId());
                                        //manager.addTask(task);
                                        gui.update();
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        dataPackage = new DataPackage(Command.FAIL);
                                    }
                                }
                                break;
                            }

                            case GET_ALL : {
                                //sendTasks();
                                sendTaskAndAlertTask();
                                break;
                            }
                        }
                    } catch (SocketException e) {
                        JOptionPane.showMessageDialog(gui,
                                "Потеряно соединение с клиентом",
                                "Ошибка", JOptionPane.INFORMATION_MESSAGE);
                        ConnectionClass.this.disconnect(gui);
                        break;
                    } catch (ClassNotFoundException | IOException ex) {
                        JOptionPane.showMessageDialog(gui,
                                "Ошибка при получении команды от клиента",
                                "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };
        //sendThread.start();
        receiveThread.start();
    }
    
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

    //Передаёт все задачи похоже не используется
    public void sendTasks() {
        Collection<Task> tasks= null;
        synchronized (this.manager) {
            tasks = manager.getCollectionTasksForUser(connectUser.getId());
            
            //manager.getCompletedTasks();
        }
        DataPackage data = new DataPackage(Command.ALL_TASKS, tasks.toArray(), alertTasks.toArray());
        this.sendData(data);
    }
    
    //используется в gui после update или еще чего-нибудь
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
            
            DataPackage data = dataPackage
                    = new DataPackage(Command.ALL_TASKS_AND_ALERT, tasks.toArray(), alertTasks.toArray());
            this.sendData(data);
        }
    }
    
    //оповещаем пользователя о наступивших задачах
    public void sendAlertTasks(Task[] tasks) {
       //ArrayList<Task> list = new ArrayList<>();
        synchronized (this.alertTasks) {
            for (Task task : tasks) {
                if (task.getUser() == connectUser) {
                    //list.add(task);
                    alertTasks.add(task.getId());//только здесь добавляется
                }
            }
            DataPackage data = new DataPackage(Command.ALERT_TASKS, null, alertTasks.toArray());
            this.sendData(data);
        }
    }

    private void disconnect(GUI gui) {
        //sendThread.interrupt();
        receiveThread.interrupt();
        gui.disconnect(this);
    }

    @Override
    public void update(Object value) {
        Task[] tasks = (Task[])value;//!!! Возможно вызовет исключение
        sendAlertTasks(tasks);
    }

    //Сервер удалил задачу i
    //Нужно поправить список индексов
//    public void deleteAlert(int index) {
//        alertTasks.remove((Object) index);
//        for (int i = 0; i < alertTasks.size(); i++) {
//            int value = (int) alertTasks.get(i);
//            if (value > index) {
//                alertTasks.set(i, --value);
//            }
//        }
//        gui.update();
//    }
    
    public void addIndexTasks(long[] array){
        for (long i : array) {
            alertTasks.add(i);
        }
    }
    
    public void removeIndexTasks(long id){
        alertTasks.remove(id);
    }
    
    private Task[] toArrayTask(List<Task> list){
        Task[] tasks = new Task[list.size()];
        for(int i = 0; i< list.size(); i++){
            tasks[i] = list.get(i);
        }
        return tasks;
    }
    
}
