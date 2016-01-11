package de.bht.vs.minimailbox.server;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

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
    private List<IMailboxServer> serverThreads;
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

    private void run() throws Exception {
        this.serverSocket = new ServerSocket(this.port);
        runJetty();
        LOGGER.info("Waiting for clients...");
        while(!stopServer) {
            Socket socket = serverSocket.accept();
            ServerThread serverThread = new ServerThread(socket);
            this.serverThreads.add(serverThread);
            serverThread.start();
        }
    }

    private void runJetty() throws Exception {
        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(8080);
        ServletContextHandler ctx = new ServletContextHandler();
        ctx.setContextPath("/");
        ctx.addServlet(MinimailboxSocketServlet.class, "/mailbox");

        server.setHandler(ctx);
        server.start();
        //server.join();
        //while(!stopServer) {

        //}
    }

    private void stop() throws IOException {
        LOGGER.info("Server shutting down...");
        stopServer = true;
        serverSocket.close();
    }

    public List<IMailboxServer> getServerThreads() {
        return serverThreads;
    }

    public List<User> getUsers() {
        return users;
    }

    public static void main(String[] args) throws Exception {
        new Server().run();
    }



    public static class MinimailboxSocketServlet extends WebSocketServlet {

        @Override
        public void configure(WebSocketServletFactory factory) {
            factory.register(MailboxWebsocketHandler.class);
        }
    }
}
