package de.bht.vs.minimailbox.server;

import de.bht.vs.minimailbox.json.Request;

/**
 * Created by Timo on 11.01.2016.
 */
public interface IMailboxServer {
    void sendMsg(int sequence, String user, String message);
    User getUser();
}
