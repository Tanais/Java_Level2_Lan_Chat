package ru.as.homeworks.lan_chat_start.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private ChatServer chatServer;
    private Socket socket;
    private String nick;
    private DataInputStream in;
    private DataOutputStream out;


    public ClientHandler(Socket socket, ChatServer chatServer) {
        new Thread(() -> {
            this.nick = "";
            this.socket = socket;
            this.chatServer = chatServer;
//            Запускаем новый поток и ждем что клиент авторизуется
            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Start wait auth");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("End wait");
//            Если получил пустой ник значит не прошла авторизация.
            if (this.nick == ""){
//                закрываем сессию (Кикаем).
                this.closeConnection();
                System.out.println("KICK");
            }

        }).start();
        new Thread(() -> {
            try {
                authenticate();
                readMessage();
            } finally {
                closeConnection();
            }
        }).start();
    }

    private void authenticate() {
        while (true) {
            try {
                String message = in.readUTF();
                if (message.startsWith("/auth")) {
                    final String[] split = message.split(" ");
                    final String login = split[1];
                    final String password = split[2];
                    final String nick = chatServer.getAuthService().getNickByLoginAndPassword(login, password);
                    if (nick != null) {
                        if (chatServer.isNickBusy(nick)) {
                            sendMessage("Nick busy");
                            continue;
                        }
                        sendMessage("/authok " + nick);
                        this.nick = nick;
                        chatServer.broadcast("User " + nick + " enter");
                        chatServer.subscribe(this);
                        break;
                    } else {
                        sendMessage("Wrong data");
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readMessage() {
        try {
            while (true) {
                final String msg = in.readUTF();
                if (msg.startsWith("/")) {
                    if ("/end".equals(msg)) {
                        break;
                    }
                    if (msg.startsWith("/w")) {
                        String[] split = msg.split(" ");
                        final String nickTo = split[1];
                        chatServer.sendMessageToClient(this, nickTo, msg.substring("/w".length() + 2 + nickTo.length()));
                    }
                    continue;
                }
                chatServer.broadcast(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void closeConnection() {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (socket != null) {
            try {
                socket.close();
                chatServer.unsubscribe(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String getNick() {
        return nick;
    }
}
