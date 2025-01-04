package org.example.telegramBot.DAO;

import org.example.telegramBot.DAO.DAOInterfaces.UserDaoImpl;
import org.example.telegramBot.TelegramBot.MyBot;
import org.example.telegramBot.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager implements UserDaoImpl {
    private static final String url = "jdbc:postgresql://localhost:6969/ChochokbashBot";
    private static final String user = "postgres";
    private static final String password = "7482040607";

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Успешно подключено!");
        } catch (SQLException error) {
            System.out.println("Ошибка при подлючении :(");
            error.printStackTrace();
        }
        return connection;
    }


    public boolean isUserRegistered(Long chatId) {
        String query = "SELECT COUNT(*) FROM users WHERE chat_id = ?";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, chatId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
        return false;
    }

    @Override
    public void registerUser(Long chatId, String username, String firstName, String lastName) {
        String insertQuery = "INSERT INTO users (chat_id, username, first_name, last_name, chochok) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setLong(1, chatId);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, firstName);
            preparedStatement.setString(4, lastName);
            preparedStatement.setInt(5, 0);

            preparedStatement.executeQuery();

        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    @Override
    public int getChochok(Long chatId) {
        String query = "SELECT chochok FROM users WHERE chat_id = ?";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, chatId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("chochok");
            }

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return 0;
    }

    @Override
    public void updateChochok(Long chatId, int change) {
        String updateQuery = "UPDATE users SET chochok = chochok + ? WHERE chat_id = ?";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setInt(1, change);
            preparedStatement.setLong(2, chatId);
            preparedStatement.executeQuery();

        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public void getAllUsersSortedByChochok(Long chatId) {
        MyBot myBot = new MyBot();
        String query = "SELECT username, chochok FROM users ORDER BY chochok DESC";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            StringBuilder userList = new StringBuilder();

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                int chochok = resultSet.getInt("chochok");

                userList.append(username)
                        .append(" - ")
                        .append(chochok)
                        .append("\n");
            }

            myBot.sendMessage(chatId, userList.toString());
        }  catch (SQLException error) {
            error.printStackTrace();
        }
    }
}
