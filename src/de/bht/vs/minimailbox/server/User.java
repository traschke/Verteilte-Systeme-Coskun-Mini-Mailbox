package de.bht.vs.minimailbox.server;

import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Timo on 23.11.2015.
 */
public class User {

    private String username;
    private String inetAddress;

    public User(String username, String inetAddress) {
        this.username = username;
        this.inetAddress = inetAddress;
    }

    public String getUsername() {
        return username;
    }

    public String getInetAddress() {
        return inetAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        return !(inetAddress != null ? !inetAddress.equals(user.inetAddress) : user.inetAddress != null);

    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (inetAddress != null ? inetAddress.hashCode() : 0);
        return result;
    }
}
