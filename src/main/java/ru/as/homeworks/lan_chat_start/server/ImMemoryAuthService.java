package ru.as.homeworks.lan_chat_start.server;

import java.util.ArrayList;
import java.util.List;

public class ImMemoryAuthService implements AuthService {


    private final List<UserData> users;

    public ImMemoryAuthService(){
        users = new ArrayList<>();
        for (int i = 1; i < 11; i++){
            users.add(new UserData("login" + i, "pass" + i, "nick" + i));
        }
    }


    @Override
    public String getNickByLoginAndPassword(String login, String password) {
        for (UserData user : users){
            if (user.getLogin().equals(login) && user.getPassword().equals(password)){
                return user.getNick();
            }
        }
        return null;
    }




    private static class UserData {
        private final String login;
        private final String password;
        private final String nick;

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        public String getNick() {
            return nick;
        }

        public UserData(String login, String password, String nick) {
            this.login = login;
            this.password = password;
            this.nick = nick;
        }
    }



}
