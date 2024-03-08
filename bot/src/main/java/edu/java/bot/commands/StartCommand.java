package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.ScrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {

    private final ScrapperClient scrapperClient;

    @Autowired
    public StartCommand(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public String command() {
        return "start";
    }

    @Override
    public String description() {
        return "register chat";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();

        String message = scrapperClient.addChat(chatId);

        return new SendMessage(chatId, message);
    }

    @Override
    public boolean supports(Update update) {
        return true;
    }
}
