/**
 * Created by Andru on 26.03.14.
 */

import java.io.*;
import java.net.*;
import java.util.Random;
import java.lang.String;

public class Client {
    private String name;

    public Client() {
        name = "user #" + new Random().nextInt(10);
    }

    public void run() throws IOException {
        String serverHostname = new String("127.0.0.1");
        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            echoSocket = new Socket(serverHostname, 10007);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverHostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to: " + serverHostname);
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String userInput;
        new Thread(new ClientReader(in)).start();

        //System.out.print("input: ");
        while ((userInput = stdIn.readLine()) != null) {
            out.println(name + "<=>" + userInput);
            // System.out.println("echo: " + in.readLine());
            //    System.out.print("input: ");
        }

        out.close();
        in.close();
        stdIn.close();
        echoSocket.close();
    }

    class ClientReader implements Runnable {
        private BufferedReader in;

        ClientReader(BufferedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println("echo: " + in.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
