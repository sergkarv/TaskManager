/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;

import connection.ConnectionClass;

/**
 *
 * @author Сергей
 */
public interface ServerSocketListener {
    void notifyConnections();
    void disconnect(ConnectionClass aThis);
}
