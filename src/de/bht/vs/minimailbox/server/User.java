package de.bht.vs.minimailbox.server;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Timo on 23.11.2015.
 */
public class User {

    private String username;
    private Date lastAction;
    private int timeoutInSecs;

    public User(String username, int timeoutInSecs) {
        this.username = username;
        this.action();
        this.timeoutInSecs = timeoutInSecs;
    }

    public void action() {
        this.lastAction = new Date();
    }

    public boolean isStillAlive() {
        Calendar c = Calendar.getInstance();
        c.setTime(this.lastAction);
        c.add(Calendar.SECOND, this.timeoutInSecs);
        Date dateTimeout = c.getTime();
        Date now = new Date();
        if (now.getTime() > dateTimeout.getTime()) {
            return false;
        }
        return true;
    }

    public String getUsername() {
        return username;
    }

    public Date getLastAction() {
        return lastAction;
    }

    public int getTimeoutInSecs() {
        return timeoutInSecs;
    }
}
