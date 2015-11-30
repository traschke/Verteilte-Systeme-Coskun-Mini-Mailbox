package de.bht.vs.minimailbox.json;

import com.google.gson.Gson;

/**
 * Created by Timo on 30.11.2015.
 */
public class Response {
    private int statuscode;
    private int sequence;
    private String[] response;

    public Response(String[] response, int statuscode, int sequence) {
        this.response = response;
        this.statuscode = statuscode;
        this.sequence = sequence;
    }

    public int getStatuscode() {
        return statuscode;
    }

    public int getSequence() {
        return sequence;
    }

    public String[] getResponse() {
        return response;
    }

    public String toJson() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

    public static Response fromJson(String json) {
        Gson gson = new Gson();
        Response response = gson.fromJson(json, Response.class);
        return response;
    }
}
