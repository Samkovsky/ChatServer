/**
 * Created by Andru on 26.03.14.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.io.*;
public class Server {
    private List<ClientHandler> clients = new ArrayList<ClientHandler>();

    public void run() throws IOException {
        ServerSocket serverSocket = null;
        serverSocket = new ServerSocket(10007);
        try {
            while (true) {
                System.out.println("Waiting for connection.....");
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket);
                clients.add(handler);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }


        serverSocket.close();
    }

    private void saveToHistory(Message message) throws IOException{
        File file = new File("History.txt");
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        out.print(message);
        out.close();
       // TODO : Save to file
        // file append
        // 1 - text file
        // 2 - binary file
    }
    class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public PrintWriter getWriter() {
            return out;
        }

        public void responseAll(String msg) {
            for (ClientHandler client : clients)
                client.getWriter().println(msg);
        }

        @Override
        public void run() {
            try {
                System.out.println("Connection successful");
                System.out.println("Waiting for input.....");

                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Server: " + inputLine);
                    StringTokenizer st = new StringTokenizer(inputLine,"<=>");
                    st.nextToken();
                    st.nextToken();
                    // TODO: parse message save to History
                    // saveToHistory(message);
                    responseAll(inputLine);
                    if (inputLine.equals("Bye."))
                        break;
                }

                out.close();
                in.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
