package de.bht.vs.minimailbox.server;

import de.bht.vs.minimailbox.json.Request;
import de.bht.vs.minimailbox.json.Response;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by Timo on 11.01.2016.
 */
public class MailboxWebsocketHandler implements WebSocketListener, IMailboxServer {
    private final static Logger LOGGER = Logger.getLogger(MailboxWebsocketHandler.class.getName());
    private User user;
    private Session session;

    private void sendResponse(int sequence, int statuscode, String... response) {
        Response responseObj = new Response(response, statuscode, sequence);
        if (isLoggedIn()) {
            LOGGER.info("Sending response to \"" + this.user.getUsername() + "\": " + responseObj.toJson());
        } else {
            LOGGER.info("Sending response to unregistered user: " + responseObj.toJson());
        }
        send(responseObj.toJson());
    }

    private void handleRequest(Request request) {
        if (isLoggedIn()) {
            LOGGER.info("Request from \"" + this.user.getUsername() + "\" (Websocket) received: " + request.toJson());
        } else {
            LOGGER.info("Request from unregistered Client " + session.getLocalAddress() + " (Websocket) received: " + request.toJson());
        }

        int sequence = request.getSequence();
        if (!isLoggedIn()) {
            if (request.getCommand().equals("login")) {
                if (Server.getInstance().getUsers().size() < Server.MAX_CLIENTS) {
                    final String username = request.getParams()[0];
                    if (!usernameAlreadyTaken(username)) {
                        this.user = new User(username, this.session.getRemoteAddress().toString());
                        Server.getInstance().getUsers().add(this.user);
                        LOGGER.info("[" + Server.getInstance().getUsers().size() + "/" + Server.MAX_CLIENTS + "] User \"" +
                                this.user.getUsername() + "\" from \"" + this.user.getInetAddress().toString() +
                                "\" logged in!");
                        sendLoginSuccessful(sequence);
                    } else {
                        sendUsernameAlreadyTaken(sequence);
                    }
                } else {
                    sendMaxClientsReached(sequence);
                }
            } else {
                sendUnauthorized(sequence);
            }
        } else if (isLoggedIn()) {
            switch (request.getCommand()) {
                case "time":
                    sendTime(sequence);
                    break;
                case "ls":
                    sendLs(sequence, request.getParams()[0]);
                    break;
                case "who":
                    sendWho(sequence);
                    break;
                case "msg":
                    sendMessage(sequence, request.getParams()[0], request.getParams()[1]);
                    break;
                case "exit":
                    sendExit(sequence);
                    break;
                case "login":
                    sendAlreadyLoggedIn(sequence);
                    break;
                default:
                    sendNotImplemented(sequence);
                    break;
            }
        }
    }

    private void sendUsernameAlreadyTaken(int sequence) {
        sendResponse(sequence, 400, "username already taken");
    }

    private void sendLs(int sequence, String dir) {
        File file = new File(dir);
        if (file.exists()) {
            if (file.isDirectory()) {
                String[] ls = file.list();
                sendResponse(sequence, 200, ls);
            } else {
                sendResponse(sequence, 404, "is not a directory");
            }
        } else {
            sendResponse(sequence, 404, "directory not found");
        }
    }

    private void sendMessage(int sequence, String username, String message) {
        User user = findUserByName(username);
        if (user != null) {
            IMailboxServer serverThread = findThreadByUser(user);
            if (serverThread != null) {
                serverThread.sendMsg(sequence, this.user.getUsername(), message);
                sendResponse(sequence, 204);
            } else {
                sendResponse(sequence, 404, "User not found");
            }
        } else {
            sendResponse(sequence, 404, "User not found");
        }
    }

    private IMailboxServer findThreadByUser(User user) {
        for(IMailboxServer thread : Server.getInstance().getServerThreads()) {
            if (thread.getUser().equals(user)) {
                return thread;
            }
        }
        return null;
    }

    private User findUserByName(String username) {
        for(User user : Server.getInstance().getUsers()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    private void sendLoginSuccessful(int sequence) {
        sendResponse(sequence, 200, "hi");
    }

    private void sendMaxClientsReached(int sequence) {
        sendResponse(sequence, 503, "max clients reached", String.valueOf(Server.MAX_CLIENTS));
    }

    private void sendUnauthorized(int sequence) {
        sendResponse(sequence, 401, "unauthorized");
    }

    private void sendNotImplemented(int sequence) {
        sendResponse(sequence, 501, "not implemented");
    }

    private void sendAlreadyLoggedIn(int sequence) {
        sendResponse(sequence, 400, "already logged in");
    }

    private void sendExit(int sequence) {
        sendResponse(sequence, 204, "byebye");
        logout();
    }

    private void logout() {
        Thread.currentThread().interrupt();
        Server.getInstance().getUsers().remove(this.user);
        Server.getInstance().getServerThreads().remove(this);
        LOGGER.info("User logged out.");
    }

    private void sendWho(int sequence) {
        String[] clients = new String[Server.getInstance().getUsers().size()];
        for (int i = 0; i < Server.getInstance().getUsers().size(); i++) {
            clients[i] = Server.getInstance().getUsers().get(i).getUsername();
        }
        sendResponse(sequence, 200, clients);
    }

    private void sendTime(int sequence) {
        Date date = new Date();
        sendResponse(sequence, 200, date.toString());
    }

    private boolean usernameAlreadyTaken(String username) {
        Server.getInstance().getUsers();
        for (User user : Server.getInstance().getUsers()) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private boolean isLoggedIn() {
        return this.user != null;
    }

    @Override
    public void onWebSocketBinary(byte[] bytes, int i, int i1) {

    }

    @Override
    public void onWebSocketText(String s) {
        Request request = Request.fromJson(s);
        handleRequest(request);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        System.out.println("Connection closed with statusCode="
                + statusCode + ", reason=" + reason);
    }

    @Override
    public void onWebSocketConnect(Session session) {
        this.session = session;
        Server.getInstance().getServerThreads().add(this);
        LOGGER.info("Client from Web connected!");
        sendResponse(100, 200, "Welcome to Mini Mailbox Server. Please log in.");
    }

    @Override
    public void onWebSocketError(Throwable throwable) {

    }

    // sends message to browser
    public void send(String message) {
        try {
            if (session.isOpen()) {
                session.getRemote().sendString(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMsg(int sequence, String user, String message) {
        sendResponse(sequence, 200, user, message);
    }

    @Override
    public User getUser() {
        return this.user;
    }
}
