package Model;

import Controller.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasper Lankhorst on 19-11-2016.
 */
public class MultiThreadedServer implements Runnable {

    protected int serverPort = 80;
    private ServerSocket serverSocket = null;
    private static boolean isStopped = false;
    protected Controller controller = null;

    public MultiThreadedServer(int port, Controller controller) {
        this.serverPort = port;
        this.controller = controller;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    @Override
    public void run() {
        isStopped = false;
        openServerSocket();

        while (!isStopped()) {
            Socket clientSocket = awaitIncomingConnection();
            connectingProces(clientSocket);
        }
        controller.addStringToLog("[ OK ] Server Thread exiting....");
    }

    public Socket awaitIncomingConnection() {
        Socket clientSocket = null;
        try {
            controller.addStringToLog("[ OK ] Server is awaiting connections...");
            clientSocket = this.serverSocket.accept();
        } catch (SocketException e) {
            isStopped = true;
            clientSocket = null;
        } catch (IOException e) {
            isStopped = true;
            clientSocket = null;
            throw new RuntimeException("[ Error ] accepting client connection", e);
        }
        return clientSocket;
    }

    public void connectingProces(Socket clientSocket) {
        if (clientSocket != null) {
            //Connecting client.
            controller.addStringToLog("Connection made..");
            WorkerRunnable workerRunnable = new WorkerRunnable(
                    clientSocket, "Multithreaded Server", controller);
            controller.addConnection(workerRunnable);
            Thread t = new Thread(workerRunnable);
            t.start();
        }
    }

    private void openServerSocket() {
        if (serverSocket == null || serverSocket.isClosed()) {
            this.isStopped = false;
            try {
                this.serverSocket = new ServerSocket(this.serverPort);
            } catch (IOException e) {
                isStopped = true;
                return;
            }
        }
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }
}
