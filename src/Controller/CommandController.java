package Controller;

import Model.WorkerRunnable;

import java.util.List;

/**
 * Created by jasper wil.lankhorst on 12-3-2017.
 */
public class CommandController {

    Controller controller;

    public CommandController(Controller controller){
        this.controller = controller;
    }

    public void procesCommand(WorkerRunnable runnable, String input) {
        //TODO split this better and make a format for command  <arg[0] command> <arg[1] player> <arg [2] ignore || chatmsg >
        String[] pieces = input.split(" ", 3);

        String command = null;
        String player = null;
        String chat = null;

        if (pieces.length >= 1) {
            command = pieces[0];
        } else {
            command = input;
        }

        if (pieces.length >= 2) {
            player = pieces[1];
        }

        if (pieces.length >= 3) {
            chat = pieces[2];
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
            case "/list":
                sendUserListToOneUser(runnable);
                break;
            case "/chat":
                sendToAllMessage(player + " " + chat);
                break;
            default:
                runnable.sendMessageToClient("Wrong command found.");
                break;
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

    public void sendUserListToOneUser(WorkerRunnable runnable) {
        runnable.sendMessageToClient("/userlist " + getAllUserNicknames());
    }

    public void sendToAllMessage(String message) {
        controller.getModel().sendToAnyone(message);
    }


    public void sendUserList() {
        sendToAllMessage("/userlist " + getAllUserNicknames());
    }


    public String getAllUserNicknames() {
        List<WorkerRunnable> connections = controller.getModel().getConnections();
        String playerList = "";
        for (WorkerRunnable runnable : connections) {
            playerList += runnable.getNickname() + " ";
        }
        return playerList;
    }

    public void disconnectAllUsers(){
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
