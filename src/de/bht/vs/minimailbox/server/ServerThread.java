package de.bht.vs.minimailbox.server;

import de.bht.vs.minimailbox.json.Request;

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

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        LOGGER.info("Client " + this.socket.getInetAddress().toString() + " connected!");
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    @Override
    public void run() {
        String inputline;
        try {
            while ((inputline = in.readLine()) != null) {
                LOGGER.info("New data received: " + inputline);
                Request request = Request.fromJson(inputline);
                // TODO Handle Request here...
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
