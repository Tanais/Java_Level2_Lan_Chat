module com.example.lan_chat {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens ru.as.homeworks.lan_chat_start to javafx.fxml;
    exports ru.as.homeworks.lan_chat_start;
}