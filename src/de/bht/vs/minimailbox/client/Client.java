package de.bht.vs.minimailbox.client;

import de.bht.vs.minimailbox.json.Request;

import java.io.*;
import java.net.Socket;

/**
 * Created by Khaled on 30.11.2015.
 */
public class Client {

    private static String input;
    private static String jsonRequest;
    private static DataOutputStream out;
    private static DataInputStream in;
    private static Request request;
    private static int rndSequence;

    public static void main(String[] args) throws IOException {
        rndSequence = (int) (Math.random() * 100);
        if (args[1] == null) {
            args[1] = "8090";
        }
        Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
        out = new DataOutputStream(socket.getOutputStream());
        in = new DataInputStream(socket.getInputStream());
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("$>");
        while ((input = stdIn.readLine()) != null) {
            if (input.equals("hilfe")) {
                hilfe();
                System.out.println("\n\n$>");
            }
            if (input.equals("login")) {
                login();
                break;
            }
            if (input.equals("time")) {
                break;
            }
            if (input.equals("ls")) {
                break;
            }
            if (input.equals("who")) {
                break;
            }
            if (input.equals("msg")) {
                break;
            }
            if (input.equals("exit")) {
                break;
            }
            System.out.print("$>");
        }
    }

    private static void hilfe() {
        System.out.println("hilfe\t\t -\tRuft die Hilfe auf.\n" +
                "login <username>\t\t -\tLoggt den User ein.\n" +
                "time\t -\tGibt die aktuelle Zeit aus.\n" +
                "ls <Pfad>\t\t -\tGibt die Dateiliste des Pfades aus.\n" +
                "who -\tGibt die Liste der verbundenen Clients aus.\n" +
                "msg <Client> <message>\t\t -\tSendet Nachricht an <Client>\n" +
                "exit\t\t -\tBeendet das Programm.");
    }

    public static void login() throws IOException {
        // System.out.println("Logging in at: ");
        request = new Request(rndSequence, input.split(" ")[0], new String[]{input.split(" ")[1]});
        out.writeUTF(request.toJson());
        System.out.println("Login successful.");
        return;
    }

    public static void time() {
        System.out.println("Logging in at: ");
        if (input.startsWith("login")) {
            input = input.split(" ")[1];
            //out.writeUTF();
            // System.out.println("Login failed.");
            return;
        }
        System.out.println("Login successful.");
    }
}
