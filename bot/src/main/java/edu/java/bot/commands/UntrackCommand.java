package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.ScrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UntrackCommand implements Command {

    private final ScrapperClient scrapperClient;

    @Autowired
    public UntrackCommand(ScrapperClient userData) {
        this.scrapperClient = userData;
    }

    @Override
    public String command() {
        return "untrack";
    }

    @Override
    public String description() {
        return "remove source";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();

        if (update.message().text().split(" ").length < 2) {
            return new SendMessage(chatId, "Введите ссылку");
        }
        String link = update.message().text().split(" ")[1];

        scrapperClient.removeLink(chatId, link).block();
        return new SendMessage(chatId, "Ресурс %s удален.".formatted(link));
    }

    @Override
    public boolean supports(Update update) {
        long chatId = update.message().chat().id();

        try {
            scrapperClient.getLinks(chatId).block();
        } catch (RuntimeException ex) {
            return false;
        }

        return true;
    }
}
