package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.ScrapperClient;
import edu.java.dto.Link;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListCommand implements Command {
    private final ScrapperClient scrapperClient;

    @Autowired
    public ListCommand(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public String command() {
        return "list";
    }

    @Override
    public String description() {
        return "get sources";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();

        String message = Arrays.stream(scrapperClient.getLinks(chatId)
            .block()
            .links())
            .map(Link::getUrl)
            .collect(Collectors.joining(", "));

        return new SendMessage(chatId, "Отслеживаются: %s".formatted(message));
    }

    @Override
    public boolean supports(Update update) {
        long chatId = update.message().chat().id();

        try {
            scrapperClient.getChat(chatId).block();
        } catch (IllegalArgumentException ex) {
            return false;
        }

        return true;
    }
}
