package taskmanagerserver;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import alert.ConcreteAlertSystem;
import socket.ServerSocketRunnable;
import taskManager.ConcreteTaskManager;
import connection.ConnectionClass;

/**
 *
 * @author Сергей
 */
public class Main {
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | 
                IllegalAccessException | 
                javax.swing.UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                GUI gui = new GUI();
                final ArrayList<ConnectionClass> list = new ArrayList<>();
                gui.setConnections(list);
                int port = 0;
                ServerSocketRunnable ssr = null;
                boolean flag = true;
                Thread tAlert = new Thread(ConcreteAlertSystem.getInstance());
                tAlert.setDaemon(true);
                tAlert.start();
                while(flag){
                    try {
                        port = Integer.parseInt(JOptionPane.
                                showInputDialog("Введите номер порта"));
                        ssr = new ServerSocketRunnable(ConcreteAlertSystem.getInstance(),
                                ConcreteTaskManager.getInstance(), list, gui, port);
                        flag = false;
                    }catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(gui,
                                "Введены некорректные данные.", "Ошибка",
                                JOptionPane.ERROR_MESSAGE);
                    }catch (RuntimeException e) {
                        JOptionPane.showMessageDialog(gui,
                                "Ошибка при старте сервера.", "Ошибка",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                Thread thread = new Thread(ssr);
                thread.setDaemon(true);
                thread.start();
                gui.setVisible(true);
            }
        });
    }
}
