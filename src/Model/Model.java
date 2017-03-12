package Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasper Lankhorst on 17-11-2016.
 * All the operation done on the object.
 */
public class Model {

    private List<WorkerRunnable> connections = new ArrayList<WorkerRunnable>(); //all connections.
    public List<String> currentUserList; //string of users.

    /**
     * Reset the connections.
     */
    public void resetConnections() {
        if (connections.size() > 0) {
            for (WorkerRunnable connection : connections) {
                if (connection.clientSocket.isConnected()) {

                    // connection.sendMessageToClient("disconnected");
                    connection.isConnected = false;

                    try {
                        connection.clientSocket.close();
                        connection.clientSocket = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            connections = new ArrayList<WorkerRunnable>();
        }
    }

    public Model() {
    }

    /**
     * Gives back a list of all connected users.
     *
     * @return List<String>
     */
    public List<String> getConnectedPersons() {
        return currentUserList;
    }

    /**
     * Set the person to the list
     *
     * @param name is a String.
     */
    public void setPerson(String name) {

    }

    /**
     * Send to all the users a message.
     *
     * @param message String of text to send to the person.
     */
    public void sendToAnyone(String message) {
        for (WorkerRunnable connection : connections) {

            if (connection.isConnected) {
                connection.sendMessageToClient(message);
            }
        }
    }

    /**
     * Remove a WorkerRunnable from the list of currentConnections.
     *
     * @param connection to be killed.
     */
    public synchronized void removeConnection(WorkerRunnable connection) {
        connections.remove(connection);
    }

    /**
     * Update the current list of Nicknames in the connection.
     *
     * @return a list of people who are currently connected.
     */
    public synchronized List<String> updateListOfNicknames() {
        List<String> users = new ArrayList<>();
        for (WorkerRunnable connection : connections) {
            try {
                String nickname = connection.getNickname();
                users.add(nickname);
            } catch (Exception ex) {
                connections.remove(connection);
            }
        }
        return users;
    }

    /**
     * Get the current connections.
     *
     * @return List of all connections<WorkerRunnable> containing
     */
    public List<WorkerRunnable> getConnections() {
        return connections;
    }
}
