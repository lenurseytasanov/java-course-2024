package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.ScrapperClient;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand implements Command {

    private final ScrapperClient scrapperClient;

    @Autowired
    public TrackCommand(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public String command() {
        return "track";
    }

    @Override
    public String description() {
        return "add source";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();

        if (update.message().text().split(" ").length < 2) {
            return new SendMessage(chatId, "Введите ссылку");
        }
        String link = update.message().text().split(" ")[1];

        if (URI.create(link).getHost() == null || (!URI.create(link).getHost().equals("github.com")
            && !URI.create(link).getHost().equals("stackoverflow.com"))) {
            return new SendMessage(chatId, "Некорректная ссылка");
        }

        scrapperClient.addLink(chatId, link).block();
        return new SendMessage(chatId, "Ресурс %s добавлен.".formatted(link));
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
