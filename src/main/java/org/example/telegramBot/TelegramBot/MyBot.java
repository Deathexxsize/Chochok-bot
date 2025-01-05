package org.example.telegramBot.TelegramBot;

import org.example.telegramBot.DAO.DBManager;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.util.List;


public class MyBot extends TelegramLongPollingBot {

    @Override
    public String getBotToken() {
        return "8176701540:AAEZqZHCZ8H2_d98qH1JqAnoHYPX4af5Xk8";
    }

    @Override
    public String getBotUsername() {
        return "chochokbash_bot";
    }

    public void sendMessage(Long chatId, String text) {
        try {
            // Создаём объект SendMessage с ID чата и текстом
            SendMessage message = new SendMessage(chatId.toString(), text);
            // Отправляем сообщение через execute()
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {

        DBManager dbManager = new DBManager();

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getFrom().getUserName();
            String firstName = update.getMessage().getFrom().getFirstName();
            String lastName = update.getMessage().getFrom().getLastName();

            if (messageText.equals("/start")) {
                String startMessage = "Привет! \nЯ бот, который увеличивает твой чочок раз в день. Просто напиши /chochok, чтобы увидеть результат!";
                sendMessage(chatId, startMessage);
            } else if (messageText.equals("/info")) {
                String infoMessage = "Вот список доступных команд: \n\n " +
                                     "/chochok - увеличивает твой чочок от  -10  до  +10 раз в день. Используй команду каждый день, чтобы увидеть изменения.\n" +
                                     "/EnKattaChochoktor - выводит список всех участников с самым большим чочок.\n\nПриятного общения с ботом!";
                sendMessage(chatId, infoMessage);
            } else if (messageText.equals("/chochok")) {
                if (!dbManager.isUserRegistered(chatId, username)) {
                    dbManager.registerUser(chatId, username, firstName, lastName);
                    sendMessage(chatId, "Вы были успешно зарегистрированы! Теперь можно использовать команду /chochok.");
                } else {
                    updateChochok(chatId, username);
                }
            } else if (messageText.equals("EnKattaChochoktor")) {
                dbManager.getAllUsersSortedByChochok(chatId);
            }
        }


    }

    private void updateChochok(Long chatId, String username) {

        DBManager dbManager = new DBManager();

        int randomChange = (int) (Math.random() * 21) - 10;  // Генерируем случайное число от -10 до +10
        dbManager.updateChochok(chatId, randomChange);
        int chochok = dbManager.getChochok(chatId);  // Получаем обновленное значение чочока
        sendMessage(chatId, "@" + username + ", ваш чочок был изменен на " + randomChange + " см.\n Текущий размер вашего чочок:  " + chochok + " см");
    }


}
