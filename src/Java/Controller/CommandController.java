package Controller;

import Model.ClientWorkerRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by jasper wil.lankhorst on 12-3-2017.
 */
public class
CommandController {

    Controller controller;

    public CommandController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Command to be processed by the server.
     * @param runnable Runnable client.
     * @param input    String of input from a client.
     */
    public void procesCommand(ClientWorkerRunnable runnable, String input) {
        //TODO split this better and make a format for command  <arg[0] command> <arg[1] player> <arg [2] ignore || chatmsg >
        String[] pieces = input.split(" ", 3);

        String command = null;
        String player = null;
        String rest = null;

        if (pieces.length >= 1) {
            command = pieces[0];
        } else {
            command = input;
        }

        if (pieces.length >= 2) {
            player = pieces[1];
        }

        if (pieces.length >= 3) {
            rest = pieces[2];
        }

        switch (command) {
            case "/connect":
                if (player != null) {
                    runnable.setNickname(player);
                    updateUserList();
                    sendUserList();
                    //sendToAllMessage("/connected "+ player); later?
                }
                break;
            case "/nickname":
                if (player != null) {
                    runnable.setNickname(player);
                    updateUserList();
                    sendUserList();
                }
                break;
            case "/players":
                sendUserListToOneUser(runnable);
                break;
            case "/duels":
                sendListOfDuels(runnable);
            case "/chat":
                sendToAllMessage(player + " " + rest);
                break;
            case "/duel":
                sendDuelRequest(runnable, player, rest);
            case "/accept":
                sendAcceptRequest(runnable, player, rest);
            case "/cancel":
                sendCancelRequest(runnable, player, rest);
            case "/move":
                sendMove(player, rest);
            default:
                runnable.sendMessageToClient("Wrong command found.");
                break;
        }
    }

    public void sendCancelRequest(ClientWorkerRunnable player, String playerFrom, String game) {
        HashMap<String, String> duelList = controller.getModel().getDuelList();
        if (duelList.containsKey(playerFrom + ":" + game)) {
            duelList.remove(playerFrom + ":" + game);
            player.sendMessageToClient("Duel:" + playerFrom + "game:" + game + " cancelled ");
            controller.getModel().sendToOnePerson(playerFrom, player.getNickname() + " has cancelled your duel" + game);
        } else {
            player.sendMessageToClient("duel cancellation invalid");
        }
    }

    public void sendMove(String player, String move) {
        controller.getModel().sendToOnePerson(player, "/move " + move);
    }

    public void sendAcceptRequest(ClientWorkerRunnable player, String playerFrom, String game) {
        HashMap<String, String> duelList = controller.getModel().getDuelList();
        if (duelList.containsKey(playerFrom + ":" + game)) {
            duelList.remove(playerFrom + ":" + game);
            player.sendMessageToClient("Duel:" + playerFrom + "game:" + game + " accepted ");
            controller.getModel().sendToOnePerson(playerFrom, player.getNickname() + " has accepted your duel" + game);

            //@TODO Send command to begin game.
        } else {
            player.sendMessageToClient("duel accepting is invalid format");
        }
    }

    public void sendDuelRequest(ClientWorkerRunnable runnable, String challengePlayer, String game) {

        String nickname = runnable.getNickname();
        if (controller.getModel().checkOnline(challengePlayer)) {
            controller.getModel().sendToOnePerson(challengePlayer, "Duel request from" + nickname + " game:" + game + " accept with sending '/acceptDuel " + nickname + " " + game + "'"); //send Duel Request.
            controller.getModel().insertDuel(nickname, game, challengePlayer);
        }
    }


    /**
     * Request the duelList.
     * @param runnable the player requesting the list.
     */
    public void sendListOfDuels(ClientWorkerRunnable runnable) {
        //send a duel.
        String nickname = runnable.getNickname();
        runnable.sendMessageToClient("Duels:");

        HashMap<String, String> duelList = controller.getModel().getDuelList();
        Set<Entry<String, String>> set = duelList.entrySet();
        List<Entry<String, String>> list = new ArrayList<Entry<String, String>>(set);

        //Send list to player requesting it.
        for (Entry<String, String> entry : list) {
            runnable.sendMessageToClient("From:" + entry.getKey() + " Challenges:" + entry.getValue());
        }
    }

    public void updateUserList() {
        List<String> listOfNicknames = controller.getModel().updateListOfNicknames();
        controller.getView().getLoggedInUsersTextArea().setText(""); //empty it.
        for (String nickname : listOfNicknames) {
            addUser(nickname);
        }
        controller.getView().refresh();
    }

    public void sendUserListToOneUser(ClientWorkerRunnable runnable) {
        runnable.sendMessageToClient("/userlist " + getAllUserNicknames());
    }

    public void sendToAllMessage(String message) {
        controller.getModel().sendToAnyone(message);
    }


    public void sendUserList() {
        sendToAllMessage("/userlist " + getAllUserNicknames());
    }


    public String getAllUserNicknames() {
        List<ClientWorkerRunnable> connections = controller.getModel().getConnections();
        String playerList = "";
        for (ClientWorkerRunnable runnable : connections) {
            playerList += runnable.getNickname() + " ";
        }
        return playerList;
    }

    public void disconnectAllUsers() {
        sendToAllMessage("disconnected\n");
        controller.getModel().resetConnections();
        updateUserList();
    }


    /**
     * Add a user logged in to the view.
     * @param line
     */
    public synchronized void addUser(String line) {
        controller.getView().getLoggedInUsersTextArea().append(line + "\r\n");
        controller.getView().refresh();
    }

}
