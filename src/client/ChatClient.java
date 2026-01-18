package client;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class ChatClient {
    private Socket socket = null;
    private BufferedReader inputConsole = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private Consumer<String> onMessageReceived;

    public ChatClient(String address, int port) {
        try {

            //server connection
            socket = new Socket(address, port);
            System.out.println("Connected to the chat server");

            //user/keyboard input
            inputConsole = new BufferedReader(new InputStreamReader(System.in));
            //send message to server
            out = new PrintWriter(socket.getOutputStream(), true);
            //receive message from server
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            //when user input "exit" still loop continue
            String line = "";
            while (!line.equals("exit")) {
                //user write message from keyboard
                line = inputConsole.readLine();
                //send message to server
                out.println(line);
                //receive server message then print message on screen
                System.out.println(in.readLine());
            }

            //connection close
            socket.close();
            inputConsole.close();
            out.close();
        } catch (UnknownHostException u) {
            System.out.println("Host unknown: " + u.getMessage());
        } catch (IOException i) {
            System.out.println("Unexpected exception: " + i.getMessage());
        }
    }


    public ChatClient(String serverAddress, int serverPort, Consumer<String> onMessageReceived) throws IOException {
        this.socket = new Socket(serverAddress, serverPort);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.onMessageReceived = onMessageReceived;
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public void startClient() {
        new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    onMessageReceived.accept(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    public static void main(String args[]) {
        ChatClient client = new ChatClient("127.0.0.1", 5000);
    }
}