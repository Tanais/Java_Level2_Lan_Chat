package ru.as.homeworks.lan_chat_start.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.HashMap;

import java.util.Map;

public class ChatServer {

    private final Map<String, ClientHandler> clients;
    private final AuthService authService;

    public ChatServer() {

        clients = new HashMap<>();
        authService = new ImMemoryAuthServise();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            while (true) {
                System.out.println("Wait Clients");
                Socket socket = serverSocket.accept();
                new ClientHandler(socket, this);
                System.out.println("Client connected");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AuthService getAuthService() {
        return authService;
    }

    public boolean isNickBusy(String nick) {
        return clients.containsKey(nick);
    }

    public void subscribe(ClientHandler client) {
        clients.put(client.getNick(), client);
        broadcastClientList();
    }

    public void unsubscribe(ClientHandler client) {
        clients.remove(client.getNick());
        broadcastClientList();
    }

    public void broadcast(String message) {
        for (ClientHandler client : clients.values()) {
            client.sendMessage(message);
        }
    }

    public void sendMessageToClient(ClientHandler from, String nickTo, String message){
        ClientHandler clientTo = clients.get(nickTo);
        if (clientTo != null){
            clientTo.sendMessage("from " + from.getNick() + ":" + message);
            from.sendMessage("to client " + nickTo + ": " + message);
            return;
        }
        from.sendMessage("client: " + nickTo + " not in chat");
    }

    public void broadcastClientList(){
        StringBuilder message = new StringBuilder("/clients");
        clients.values().forEach(client -> message.append(client.getNick()).append(" "));
        broadcast(message.toString());
    }


}
