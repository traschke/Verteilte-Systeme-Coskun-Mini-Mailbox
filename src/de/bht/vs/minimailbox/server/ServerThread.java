package de.bht.vs.minimailbox.server;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Created by Timo on 30.11.2015.
 */
public class ServerThread extends Thread {

    private final static Logger LOGGER = Logger.getLogger(ServerThread.class.getName());
    private Socket socket;
    private DataOutputStream out;
    private DataInput in;

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;LOGGER.info("Client " + this.socket.getInetAddress().toString() + " connected!");
        this.out = new DataOutputStream(this.socket.getOutputStream());
        this.in = new DataInputStream(this.socket.getInputStream());
    }

    private void parseJson() {

    }

    @Override
    public void run() {
        while(!isInterrupted()) {
            String inputline;
            try {
                while ((inputline = in.readLine()) != null) {
                    LOGGER.info("New data received: " + inputline);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
