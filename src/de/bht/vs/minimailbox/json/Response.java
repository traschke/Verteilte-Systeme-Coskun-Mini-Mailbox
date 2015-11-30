package de.bht.vs.minimailbox.json;

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
}
