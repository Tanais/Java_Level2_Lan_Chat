package ru.as.homeworks.lan_chat_start;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.as.homeworks.lan_chat_start.client.ChatClient;

public class LanChatController {
    @FXML
    private HBox loginBox;
    @FXML
    private TextField loginField;

    public TextArea getChatField() {
        return chatField;
    }

    @FXML
    private PasswordField passwordField;
    @FXML
    private HBox messageBox;
    @FXML
    private ListView<String> clientList;
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

    public void btnAuthClick(ActionEvent actionEvent) {
        client.sendMessage("/auth " + loginField.getText() + " "+ passwordField.getText());

    }

    public void selectClient(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() ==2){
            final String message = messageField.getText();
            final String client = clientList.getSelectionModel().getSelectedItem();
            messageField.setText("/w " + client + " " + message);
            messageField.requestFocus();
            messageField.selectEnd();
        }
    }

    public void setAuth (boolean isAuthSuccess) {
        loginBox.setVisible(isAuthSuccess);
        messageBox.setVisible(isAuthSuccess);
    }


    public void updateClientsList(String[] clients) {
        clientList.getItems().clear();
        clientList.getItems().addAll(clients);
    }
}