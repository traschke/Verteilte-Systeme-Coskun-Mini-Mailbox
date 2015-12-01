package de.bht.vs.minimailbox.server;

import de.bht.vs.minimailbox.json.Request;

import javax.swing.event.EventListenerList;
import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Created by Timo on 30.11.2015.
 */
public class ServerThread extends Thread {

    private final static Logger LOGGER = Logger.getLogger(ServerThread.class.getName());
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private EventListenerList clientListeners = new EventListenerList();

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        LOGGER.info("Client " + this.socket.getInetAddress().toString() + " connected!");
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    public void addClientListener(ClientListener clientListener) {
        this.clientListeners.add(ClientListener.class, clientListener);
    }

    public void removeCLientListener(ClientListener clientListener) {
        this.clientListeners.remove(ClientListener.class, clientListener);
    }

    protected synchronized  void notifyClientConnected(ClientEvent clientEvent) {
        for (ClientListener clientListener : this.clientListeners.getListeners(ClientListener.class)) {
            clientListener.userLoggedIn(clientEvent);
        }
    }

    @Override
    public void run() {
        String inputline;
        try {
            while ((inputline = in.readLine()) != null) {
                LOGGER.info("New data received: " + inputline);
                Request request = Request.fromJson(inputline);
                handleRequest(request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRequest(Request request) {
        switch (request.getCommand()) {
            case "login":
                User user = new User(request.getParams()[0], 3600);
                notifyClientConnected(new ClientEvent(user));
                break;
            case "time":
                break;
            case "ls":
                break;
            case "who":
                break;
            case "msg":
                break;
            case "exit":
                break;
            default:
                break;
        }
    }
}
