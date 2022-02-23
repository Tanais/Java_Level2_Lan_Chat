package ru.as.homeworks.lan_chat_start.server;


import java.sql.*;


public class dbAuthService implements AuthService {


    private static Connection connection;
    private static Statement statement;


//    public static void main(String[] args) {
//        dbAuthService db = new dbAuthService();
//        db.connect();
//        db.createTable();
////        db.addUsers("nick1", "login1", "pass1");
//
////        System.out.println(db.getNickByLoginAndPasswordFromDB("login1", "pass1"));
////        System.out.println(db.getNickByLoginAndPasswordFromDB("user", "p"));
//    }


    public dbAuthService() {
        connection = connect();
    }

    static Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:authDB.db");
//            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static void createTable(){
        try {
            statement.executeUpdate("" +
                    "create table if not exists users (" +
                    " id integer primary key autoincrement, " +
                    " nick text, "  +
                    " login text, " +
                    " pass text " +
                    ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void addUsers (String nick, String login, String password){

        try (final PreparedStatement ps = connection.prepareStatement("insert into users(nick, login, pass) values (?,?,?)")){
            ps.setString(1, nick);
            ps.setString(2, login);
            ps.setString(3, password);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

//    static String getNickByLoginAndPasswordFromDB (String login, String password) {
//        try (final PreparedStatement ps = connection.prepareStatement("select nick from users where login = ? and pass = ?")) {
//            ps.setString(1, login);
//            ps.setString(2, password);
//            final ResultSet resultSet = ps.executeQuery();
//            if (resultSet.next()) {
////                nick = resultSet.getString(1);
//                return resultSet.getString(1);
//            } else {
//                return null;
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }


    @Override
    public String getNickByLoginAndPassword(String login, String password) {
        try (final PreparedStatement ps = connection.prepareStatement("select nick from users where login = ? and pass = ?")) {
            ps.setString(1, login);
            ps.setString(2, password);
            final ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
//                nick = resultSet.getString(1);
                return resultSet.getString(1);
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
