package ru.as.homeworks.lan_chat_start.server;

public interface AuthService {
    String getNickByLoginAndPassword(String login, String password);
}
