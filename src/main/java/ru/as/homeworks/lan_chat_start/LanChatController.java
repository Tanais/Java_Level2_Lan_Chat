package ru.as.homeworks.lan_chat_start;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.as.homeworks.lan_chat_start.client.ChatClient;
import ru.as.homeworks.lan_chat_start.server.ChatServer;

public class LanChatController {
    @FXML
    private TextArea chatField;
    @FXML
    private TextField messageField;
    final ChatClient client;

    public LanChatController() {
        client = new ChatClient(this);
    }

    @FXML
    protected void onSendMessageClick() {

        String messageText = messageField.getText();
        if (messageText != null && !messageText.isEmpty()) {
//            chatField.appendText(messageText + "\n");
            client.sendMessage(messageText);
            messageField.clear();
            messageField.requestFocus();
        }


    }

    public void onAboutClick(ActionEvent actionEvent) {
        Stage windowAbout = new Stage();
        Pane pane = new Pane();
        Label labelAbout = new Label();
        labelAbout.setText("Welcome to alpha version LanChat");
        pane.getChildren().add(labelAbout);
        Scene scene = new Scene(pane, 200, 100);
        windowAbout.setScene(scene);
        windowAbout.setTitle("About");
        windowAbout.show();


    }

    public void onExitClick(ActionEvent actionEvent) {
        client.sendMessage("/end");
    }

    public void addMessage(String msg) {
        chatField.appendText(msg + "\n");
    }
}