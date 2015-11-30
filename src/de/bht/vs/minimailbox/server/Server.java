package de.bht.vs.minimailbox.server;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Timo on 30.11.2015.
 */
public class Server {
    private final static Logger LOGGER = Logger.getLogger(Server.class.getName());
    private static int STANDARD_PORT = 8090;
    private static int MAX_CLIENTS = 5;
    private ServerSocket serverSocket;
    private int connectedClients = 0;
    private List<ServerThread> serverThreads;

    public Server() throws IOException {
        this(STANDARD_PORT);
    }

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.serverThreads = new ArrayList<ServerThread>();
    }

    public void run() throws IOException {
        LOGGER.info("Wating for clients...");
        while(connectedClients < MAX_CLIENTS) {
            Socket socket = serverSocket.accept();
            connectedClients++;
            LOGGER.info(connectedClients + " of max. " + MAX_CLIENTS + " connected.");
            ServerThread serverThread = new ServerThread(socket);
            this.serverThreads.add(serverThread);
            serverThread.start();
        }
    }

    public void stop() throws IOException {
        LOGGER.info("Server shutting down...");
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException {
        new Server().run();
    }
}
