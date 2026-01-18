package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    // List to keep track of all connected clients
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        //server connected on 5000
        //server waiting for client
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server started. Waiting for clients...");

        while (true) {

            //accept() method untill not stop when any client do not connect
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket);

            // Spawn a new thread for each client
            ClientHandler clientThread = new ClientHandler(clientSocket, clients);
            clients.add(clientThread);
            new Thread(clientThread).start();
        }
    }
}

class ClientHandler implements Runnable {
    //particular client connection
    private Socket clientSocket;
    //all client lists
    private List<ClientHandler> clients;
    //send message to client
    private PrintWriter out;
    //receive message from client
    private BufferedReader in;

    public ClientHandler(Socket socket, List<ClientHandler> clients) throws IOException {
        this.clientSocket = socket;
        this.clients = clients;
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }


    //this method call automatically when thread is started
    public void run() {
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                // Broadcast message to all clients
                for (ClientHandler aClient : clients) {
                    aClient.out.println(inputLine);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}