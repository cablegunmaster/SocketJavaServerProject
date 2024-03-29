package Java.Controller;

import Java.Model.*;
import Java.View.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * Created by Jasper Lankhorst on 17-11-2016.
 */
public class Controller {

    private Model model;
    private View view;
    private MultiThreadedServer multiThreadedServer;
    private Controller controller;
    private int portnumber;
    private Thread multiThreadedServerThread;
    private CommandController commandController;

    private static Boolean restartProces = false;

    /**
     * Java.Controller class which grabs all.
     * @param m
     * @param v
     * @param portNumber
     */
    public Controller(Model m, View v, Integer portNumber) {
        this.model = m;
        this.view = v;
        this.controller = this;
        this.commandController = new CommandController(this);

        portnumber = portNumber;
        setListeners(); //all listeners on 1 function.
        controller.startServer(portnumber);
    }

    /**
     * Set all listeners.
     */
    public void setListeners() {
        //Build up all the actions for the current view.
        view.getRestartMenuItem().addActionListener(getRestartListener());
        view.getstopMenuItem().addActionListener(getStopListener());
    }

    /**
     * Add a String to the log.
     * @param line String value put in the view.
     */
    public synchronized void addStringToLog(String line) {
        view.getLogTextArea().append(line + "\r\n");
        int len = view.getLogTextArea().getDocument().getLength();

        view.getLogTextArea().setCaretPosition(len);
        view.refresh();
    }

    /**
     * Clear the log.
     */
    public synchronized void clearLog() {
        view.setLogTextArea();
    }

    /**
     * Starting the server, if no server has been started already.
     * @param portNumber Integer
     */
    public synchronized void startServer(Integer portNumber) {

        multiThreadedServer = new MultiThreadedServer(portNumber, controller); //gets view to send log messages.

        if (multiThreadedServerThread == null) {
            //add lines to Textarea view.
            controller.addStringToLog("Starting up server. PID: " + ManagementFactory.getRuntimeMXBean().getName());
            multiThreadedServerThread = new Thread(multiThreadedServer);
            multiThreadedServerThread.start();

            controller.addStringToLog("[ OK ] Server is started on portnumber: " + portnumber);
        }

    }

    private ActionListener getRestartListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                //lock to ensure only one proces can restart the server.
                if (!restartProces) {
                    restartProces = true;
                    if (multiThreadedServer != null) {
                        disconnectAllUsers();
                        stopMultiThreadedServer(multiThreadedServer);
                        multiThreadedServer = null; //reset the server as well.
                    }

                    stopMultiServerThread(multiThreadedServerThread);
                    restartProces = false;
                    startServer(portnumber);
                }
            }
        };
    }

    public void stopMultiServerThread(Thread multiThreadedServer) {
        try {
            //deleting leftover variables.
            multiThreadedServerThread.join(); // wait for the thread to stop
            multiThreadedServerThread = null; //this should be the place to disconnect itself.
        } catch (InterruptedException e) {
            e.getStackTrace();
        }

    }

    /**
     * Stop the server.
     * @param multiThreadedServer
     */
    private void stopMultiThreadedServer(MultiThreadedServer multiThreadedServer) {
        try {
            //end the server.
            multiThreadedServer.setStopped(true);
            if (multiThreadedServer.getServerSocket() != null) {
                multiThreadedServer.getServerSocket().close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        view.refresh();
    }

    /**
     * Disconnect All users by sending disconnect message to everyone.
     */
    private void disconnectAllUsers() {
        //disconnect users.
        commandController.disconnectAllUsers();
    }

    private ActionListener getStopListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        };
    }

    /**
     * Add 'clientWorkerRunnable' connections to the list.
     * @param clientWorkerRunnable
     */
    public void addConnection(ClientWorkerRunnable clientWorkerRunnable) {
        model.getConnections().add(clientWorkerRunnable);
    }

    public Model getModel() {
        return model;
    }

    public View getView() {
        return view;
    }

    public CommandController getCommandController() {
        return commandController;
    }

}
