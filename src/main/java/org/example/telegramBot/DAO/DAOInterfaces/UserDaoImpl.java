package org.example.telegramBot.DAO.DAOInterfaces;

public interface UserDaoImpl {
    boolean isUserRegistered(Long chatId, String username);
    void registerUser(Long chatId, String username, String firstName, String lastName);
    int getChochok(Long chatId);
    void updateChochok(Long chatId, int change);

}
