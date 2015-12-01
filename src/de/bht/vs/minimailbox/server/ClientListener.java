package de.bht.vs.minimailbox.server;

import java.util.EventListener;

/**
 * Created by Timo on 01.12.2015.
 */
public interface ClientListener extends EventListener {
    void userLoggedIn(ClientEvent e);
}
