package socket;

import taskManager.TaskManager;
import connection.ConnectionClass;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import taskmanagerserver.GUI;
import alert.AlertSystem;

/**
 *
 * @author Сергей
 */
public class ServerSocketRunnable implements Runnable, ServerSocketListener{
    /* Поток, обрабатывающий подключения к серверу */
    private ServerSocket serverSocket;
    private final int port;
    private final GUI gui;
    private final List<ConnectionClass> connections;
    private final AlertSystem alertSystem;
    private final TaskManager manager;
    
    public ServerSocketRunnable(AlertSystem alertSystem, TaskManager manager,
            List<ConnectionClass> list, GUI gui, int port){
        this.port = port;
        this.gui = gui;
        connections = list;
        this.alertSystem = alertSystem;
        this.manager = manager;
    }

    @Override
    public void run() {
        //Пытаемся поднять сервер
        boolean flag = true;
        while (flag) {
            try {
                serverSocket = new ServerSocket(port);
                flag = false;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //Ожидаем подключения
        while (true) {
            try {
                ConnectionClass connection = new ConnectionClass(
                        serverSocket.accept(), gui,
                        manager, this);
                connections.add(connection);
                alertSystem.addListener(connection);
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
    }
    
    @Override
    public void notifyConnections() {
        for (ConnectionClass c : connections) {
            c.sendTaskAndAlertTask();
        }
    }
    
    @Override
    public void disconnect(ConnectionClass aThis) {
        connections.remove(aThis);
    }
}
