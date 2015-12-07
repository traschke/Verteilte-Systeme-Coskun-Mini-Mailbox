package de.bht.vs.minimailbox.server;

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
    private static Server instance;
    private final static Logger LOGGER = Logger.getLogger(Server.class.getName());
    private final static int STANDARD_PORT = 8090;
    public final static int MAX_CLIENTS = 5;
    private int port;
    private ServerSocket serverSocket;
    private List<ServerThread> serverThreads;
    private List<User> users;
    private boolean stopServer = false;

    private Server() {
        this(STANDARD_PORT);
    }

    private Server(int port) {
        this.port = port;
        this.serverThreads = new ArrayList<>();
        this.users = new ArrayList<>();
        instance = this;
    }

    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    private void run() throws IOException {
        this.serverSocket = new ServerSocket(this.port);
        LOGGER.info("Waiting for clients...");
        while(!stopServer) {
            Socket socket = serverSocket.accept();
            ServerThread serverThread = new ServerThread(socket);
            this.serverThreads.add(serverThread);
            serverThread.start();
        }
    }

    private void stop() throws IOException {
        LOGGER.info("Server shutting down...");
        stopServer = true;
        serverSocket.close();
    }

    public List<ServerThread> getServerThreads() {
        return serverThreads;
    }

    public List<User> getUsers() {
        return users;
    }

    public static void main(String[] args) throws IOException {
        new Server().run();
    }
}
