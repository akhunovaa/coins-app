package ru.leon4uk.app.bot.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@Component
@PropertySource("classpath:bot.properties")
public class Telegram extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String userName;

    @Value("527844587:AAGkYqISxP0gcNTAN3telufS5mr174C5E8Y")
    private String token;

    @Value("-1001282888524")
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