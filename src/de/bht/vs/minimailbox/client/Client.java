package de.bht.vs.minimailbox.client;

import de.bht.vs.minimailbox.json.Request;
import de.bht.vs.minimailbox.json.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Khaled on 30.11.2015.
 */
public class Client {

    private static String input;
    private static PrintWriter out;
    private static BufferedReader in;
    private static Request request;
    private static int rndSequence;

    public static void main(String[] args) throws IOException {
        rndSequence = (int) (Math.random() * 100);
        if (args[1] == null) {
            args[1] = "8090";
        }
        Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        final BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        hilfe();
        System.out.println("\n");
        System.out.print("$>");
        new Thread(() -> {
            readConsole(stdIn);
        }).start();

        new Thread(() -> {
            getResponse();
        }).start();
    }

    private static void getResponse() {
        Response response = null;
        try {
            while (true) {
                response = Response.fromJson(in.readLine());
                System.out.println(response.toJson() + "\n\n$>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readConsole(BufferedReader stdIn) {
        try {
            while ((input = stdIn.readLine()) != null) {
                if (input.equals("hilfe")) {
                    hilfe();
                }
                if (input.startsWith("login")) {
                    login();
                }
                if (input.equals("time")) {
                    time();
                }
                if (input.startsWith("ls")) {
                    listDataInPath();
                }
                if (input.equals("who")) {
                    listUsersInChat();
                }
                if (input.startsWith("msg")) {
                    sendMsgAt();
                }
                if (input.equals("exit")) {
                    sendExit();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void hilfe() {
        System.out.println("hilfe\t\t\t\t\t -\tRuft die Hilfe auf.\n" +
                "login <username>\t\t -\tLoggt den User ein.\n" +
                "time\t\t\t\t\t -\tGibt die aktuelle Zeit aus.\n" +
                "ls <Pfad>\t\t\t\t -\tGibt die Dateiliste des Pfades aus.\n" +
                "who\t\t\t\t\t\t -\tGibt die Liste der verbundenen Clients aus.\n" +
                "msg <Client> <message>\t -\tSendet Nachricht an <Client>\n" +
                "exit\t\t\t\t\t -\tBeendet das Programm.");
    }

    public static void login() throws IOException {
        System.out.println("Logging in...");
        request = new Request(rndSequence, input.split(" ")[0], new String[]{input.split(" ")[1]});
        out.println(request.toJson());
        return;
    }

    public static void time() throws IOException {
        request = new Request(rndSequence, input, new String[]{});
        out.println(request.toJson());
        return;
    }

    private static void listDataInPath() {
        request = new Request(rndSequence, input.split(" ")[0], new String[]{input.split(" ")[1]});
        out.println(request.toJson());
        return;
    }

    private static void listUsersInChat() {
        request = new Request(rndSequence, input, new String[]{});
        out.println(request.toJson());
        return;
    }

    private static void sendMsgAt() {
        request = new Request(rndSequence, input.split(" ")[0], new String[]{input.split(" ")[1], input.split(" ")[2]});
        out.println(request.toJson());
        return;
    }

    private static void sendExit() {
        request = new Request(rndSequence, input, new String[]{});
        out.println(request.toJson());
        return;
    }
}
