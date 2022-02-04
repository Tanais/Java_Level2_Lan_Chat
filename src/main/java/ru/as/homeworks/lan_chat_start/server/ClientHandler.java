package ru.as.homeworks.lan_chat_start.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private final ChatServer chatServer;
    private final Socket socket;
    private String nick;
    private final DataInputStream in;
    private final DataOutputStream out;


    public ClientHandler(Socket socket, ChatServer chatServer) {
        try {
            this.nick = "";
            this.socket = socket;
            this.chatServer = chatServer;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    authenticate();
                    readMessage();
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                    } continue;
                } chatServer.broadcast(msg);
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
