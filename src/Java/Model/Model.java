package Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jasper Lankhorst on 17-11-2016.
 * All the operation done on the object.
 */
public class Model {

    private List<Model.ClientWorkerRunnable> connections = new ArrayList<Model.ClientWorkerRunnable>(); //all connections.
    private List<String> currentUserList; //string of users.
    private HashMap<String, String> duelList = new HashMap<>();

    /**
     * Reset the connections.
     */
    public void resetConnections() {
        if (connections.size() > 0) {
            for (Model.ClientWorkerRunnable connection : connections) {
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

            connections = new ArrayList<Model.ClientWorkerRunnable>();
        }
    }

    public Model() {
    }

    /**
     * Gives back a list of all connected users.
     * @return List<String>
     */
    public List<String> getConnectedPersons() {
        return currentUserList;
    }

    /**
     * Send to all the users a message.
     * @param message String of text to send to the person.
     */
    public void sendToAnyone(String message) {
        for (Model.ClientWorkerRunnable connection : connections) {

            if (connection.isConnected) {
                connection.sendMessageToClient(message);
            }
        }
    }

    /**
     * Send a message only to one person.
     */
    public void sendToOnePerson(String player, String message) {
        for (Model.ClientWorkerRunnable connection : connections) {
            if (connection.isConnected && connection.getNickname().equals(player)) {
                connection.sendMessageToClient(message);
            }
        }
    }

    /**
     * Remove a ClientWorkerRunnable from the list of currentConnections.
     * @param connection to be killed.
     */
    public synchronized void removeConnection(Model.ClientWorkerRunnable connection) {
        connections.remove(connection);
    }

    /**
     * Update the current list of Nicknames in the connection.
     * @return a list of people who are currently connected.
     */
    public synchronized List<String> updateListOfNicknames() {
        List<String> users = new ArrayList<>();
        for (Model.ClientWorkerRunnable connection : connections) {
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
     * @return List of all connections<ClientWorkerRunnable> containing
     */
    public List<Model.ClientWorkerRunnable> getConnections() {
        return connections;
    }


    /**
     * @return the duelList
     */
    public synchronized HashMap<String, String> getDuelList() {
        return duelList;
    }

    /**
     * Duel consist of a player who starts the duel and the player it wants to duel.
     * @param playerFrom a String containing the player.
     * @param game       the game like TicTacToe
     * @param playerTo   the player String which is actually online
     */
    public synchronized void insertDuel(String playerFrom, String game, String playerTo) {
        duelList.put(playerFrom + ":" + game, playerTo);
    }

    /**
     * Check if a player is online based on name.
     * @param playerName
     * @return Boolean stating true or false.
     */
    public synchronized boolean checkOnline(String playerName) {
        Boolean found = false;
        for (Model.ClientWorkerRunnable player : getConnections()) {
            if (player.getNickname().equals(playerName)) {
                found = true;
                break;
            }
        }
        return found;
    }
}
