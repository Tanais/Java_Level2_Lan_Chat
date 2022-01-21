module com.example.lan_chat {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.as.homeworks.lan_chat_start to javafx.fxml;
    exports ru.as.homeworks.lan_chat_start;
}