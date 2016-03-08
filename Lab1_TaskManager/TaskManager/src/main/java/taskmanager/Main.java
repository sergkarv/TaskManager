/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskmanager;

import taskmanager.core.AlertSystem;
import taskmanager.core.ConcreteAlertSystem;
import taskmanager.core.ConcreteTaskManager;
import taskmanager.core.TaskManager;

/**
 *
 * @author Сергей
 */
public class Main {

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info
                    : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException |
                javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(gui.class.getName()).
                    log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {                
                TaskManager manager = ConcreteTaskManager.getInstance();
                gui g = new gui(manager);
                g.setVisible(false);
                g.addTrayIcon();
                
                /*Инициализация и запуск системы оповещения*/
                AlertSystem alertSystem = ConcreteAlertSystem.getInstance();
                alertSystem.addListener(g);
                Thread alertThread = new Thread((Runnable) alertSystem);
                alertThread.start();

            }
        });
    }
}
