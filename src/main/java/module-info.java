module com.example.lan_chat {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lan_chat to javafx.fxml;
    exports com.example.lan_chat;
}