package de.bht.vs.minimailbox.json;

import com.google.gson.Gson;

/**
 * Created by Timo on 30.11.2015.
 */
public class Request {
    private int sequence;
    private String command;
    private String[] params;

    public Request(int sequence, String command, String[] params) {
        this.sequence = sequence;
        this.command = command;
        this.params = params;
    }

    public int getSequence() {
        return sequence;
    }

    public String getCommand() {
        return command;
    }

    public String[] getParams() {
        return params;
    }

    public String toJson() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

    public static Request fromJson(String json) {
        Gson gson = new Gson();
        Request request = gson.fromJson(json, Request.class);
        return request;
    }
}
