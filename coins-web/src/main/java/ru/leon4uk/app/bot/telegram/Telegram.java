package ru.leon4uk.app.bot.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@Component
public class Telegram extends TelegramLongPollingBot {

    @Value("bot")
    private String userName;

    @Value("527844587:AAGkYqISxP0gcNTAN3telufS5mr174C5E8Y")
    private String token;

    @Value("-312925012")
    private String chatid;

    @Override
    public void onUpdateReceived(Update update) {
            System.out.println("TEST");
    }

    @Override
    public String getBotUsername() {
            return this.userName;
    }

    @Override
    public String getBotToken() {
            return this.token;
    }

    public void sendMessage(String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatid);
        sendMessage.disableNotification();
        sendMessage.enableHtml(true);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        }
}