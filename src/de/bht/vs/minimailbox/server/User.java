package de.bht.vs.minimailbox.server;

import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Timo on 23.11.2015.
 */
public class User {

    private String username;
    private InetAddress inetAddress;

    public User(String username, InetAddress inetAddress) {
        this.username = username;
        this.inetAddress = inetAddress;
    }

    public String getUsername() {
        return username;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }
}
