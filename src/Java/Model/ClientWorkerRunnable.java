package Java.Model;

import Java.Controller.Controller;
import Java.Controller.CommandController;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Jasper Lankhorst on 20-11-2016.
 */
public class ClientWorkerRunnable implements Runnable, UserInterface {

    protected Socket clientSocket = null;
    protected String serverText = null;
    protected boolean isConnected = true;
    protected Controller controller;
    private PrintWriter printWriter;
    public InputStream inputStream;
    public OutputStream outputStream;
    public BufferedReader reader = null;
    public String nickname;
    public int matchStatus;
    public CommandController commandController;

    public ClientWorkerRunnable(Socket clientSocket, String serverText, Controller controller) {
        this.clientSocket = clientSocket;
        this.serverText = serverText;
        this.controller = controller;
        commandController = controller.getCommandController();
    }

    @Override
    public void run() {

        try {
            inputStream = clientSocket.getInputStream();
            outputStream = clientSocket.getOutputStream();
            printWriter = new PrintWriter(outputStream, true);

            while (isConnected()) {
                String stringFromInput = readInputStream(inputStream); //Read input from stream.
                if (stringFromInput != null) {
                    controller.addStringToLog(stringFromInput); //add to screen
                    commandController.procesCommand(this, stringFromInput);
                }
            }

            controller.addStringToLog("Succesfully disconnected client.");
            controller.getModel().removeConnection(this);
            commandController.updateUserList();
            commandController.sendUserList();

        } catch (SocketException e) {
            System.err.println("Disconnected client by a Socket error, probably disconnected by user.");
            isConnected = false;
        } catch (IOException e) {
            //report somewhere
            e.printStackTrace();
            System.err.println("Disconnected client by Input output error");
            isConnected = false;
        } catch (Exception e) {
            e.getCause();
            System.err.println("Disconnected client by a general exception.");
            e.getStackTrace();
            isConnected = false;
        }
    }

    private boolean isConnected() {
        return isConnected;
    }

    private String readInputStream(InputStream inputStream) throws IOException {
        String line;
        if (reader == null) {
            reader = new BufferedReader(new InputStreamReader(inputStream));
        }

        while ((line = reader.readLine()) != null || isConnected()) {
            if (line != null) {
                if (!line.equals("")) {
                    if (line.equals("/disconnect")) {
                        isConnected = false; //disconnect client.
                    }
                    return line;
                }
            }
        }
        return null;
    }

    //LOCK WHERE?
    public void sendMessageToClient(String output) {
        printWriter = new PrintWriter(outputStream, true);
        printWriter.print(output + "\n");
        printWriter.flush();

        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        if (output.equals("/disconnected")) {
            isConnected = false;
        }
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public int getMatchStatus() {
        return matchStatus;
    }
}
