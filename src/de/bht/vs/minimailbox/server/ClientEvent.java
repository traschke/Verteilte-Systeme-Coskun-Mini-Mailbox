package de.bht.vs.minimailbox.server;

import java.util.EventObject;

/**
 * Created by Timo on 01.12.2015.
 */
public class ClientEvent extends EventObject {

    private User user;
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ClientEvent(Object source) {
        super(source);
    }

    public ClientEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
