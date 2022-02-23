package ru.as.homeworks.lan_chat_start.client;

import ru.as.homeworks.lan_chat_start.LanChatController;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ChatClient {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private final LanChatController controller;


    public ChatClient(LanChatController controller) {
        this.controller = controller;
        openConnection();
    }

    private void openConnection() {
        try {
            socket = new Socket("127.0.0.1", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    while (true) {
                        final String authMsg = in.readUTF();
                        if (authMsg.startsWith("/authok")) {
                            final String nick = authMsg.split(" ")[1];
                            controller.addMessage("Good auth " + nick);
                            controller.setAuth(true);
                            loadHistory();
                            break;
                        }
                    }
                    while (true) {
                        final String msg = in.readUTF();
                        if ("/end".equals(msg)) {
                            controller.setAuth(false);
                            break;
                        }
                        if (msg.startsWith("/clients")){
                            final String[] clients = msg.replace("/clients", "").split(" ");
                            controller.updateClientsList(clients);
                        }
                        controller.addMessage(msg);
                        saveHistory();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }

            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveHistory() {
        try {
            File history = new File("history.txt");
            if (!history.exists()) {
                history.createNewFile();
            }
            PrintWriter fileWriter = new PrintWriter(new FileWriter(history, false));
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(controller.getChatField().getText());
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadHistory() throws IOException {
        int posHistory = 100;
        File history = new File("history.txt");
        List<String> historyList = new ArrayList<>();
        FileInputStream in = new FileInputStream(history);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        String temp;
        while ((temp = bufferedReader.readLine()) != null) {
            historyList.add(temp);
        }

        if (historyList.size() > posHistory) {
            for (int i = historyList.size() - posHistory; i <= (historyList.size() - 1); i++) {

            }
        } else {
            for (int i = 0; i < posHistory; i++) {
                controller.getChatField().appendText(historyList.get(i) + "\n");
            }
        }
    }



    public void closeConnection() {
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

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void sendMessage(String messageText) {
        try {
            out.writeUTF(messageText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
