package connection;

import task.Task;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import protocol.Command;
import protocol.DataPackage;
import taskmanagerclient.GUI;

public class ConnectionClass {

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Thread receiveThread;
    private Socket socket;
    private GUI gui;
    private final String STANDART_SIGNAL = "signal/bell.wav";
    private String nameUser;
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
                            case ALL_TASKS_AND_ALERT: {
                                //получаем все данные при первом обращении
                                //получаем все данные при появлении активных задач
                                Collection<Task> tasks
                                        = (Collection<Task>) receivedCommand.getValue();
                                List<Long> alertList = (List<Long>) receivedCommand
                                        .getAlert();
                                //gui.setEnabled(true);
                                ConnectionClass.this.allUserTasks = tasks;
                                ConnectionClass.this.alertTasks = alertList;
                                ConnectionClass.this.gui.fillTable(tasks);
                                ConnectionClass.this.gui.alert(alertList, true, true);
                                break;
                            }
                            case ALL_TASKS_AND_ALERT_SILENT: {
                                Collection<Task> tasks = (Collection<Task>) receivedCommand.getValue();
                                List<Long> alertList = (List<Long>) receivedCommand
                                        .getAlert();
                                ConnectionClass.this.allUserTasks = tasks;
                                ConnectionClass.this.alertTasks = alertList;
                                ConnectionClass.this.gui.fillTable(tasks);
                                ConnectionClass.this.gui.alert(alertList, false, false);
                                break;
                            }
                        }
                    } catch (SocketException e) {
                        JOptionPane.showMessageDialog(ConnectionClass.this.gui,
                                "Соединение с сервером потеряно",
                                "Ошибка", JOptionPane.ERROR_MESSAGE);
                        try {
                            Thread.currentThread().interrupt();
                            ConnectionClass.this.in.close();
                            ConnectionClass.this.out.close();
                            ConnectionClass.this.gui.disconnect();
                        } catch (IOException ex) {
                            System.out.println("Fail");
                            System.out.println(ex.getMessage());
                        }
                        break;
                    } catch (IOException | ClassNotFoundException ex) {
                        JOptionPane.showMessageDialog(ConnectionClass.this.gui,
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

    public Task getTask(long id) {
        for (Task task : allUserTasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    //отправляем пользователю нужные задания упакованные в объект класс DataPackage
    public void sendData(DataPackage data) {
        if (data != null) {
            try {
                out.writeObject(data);
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(ConnectionClass.this.gui,
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
