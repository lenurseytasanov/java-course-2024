package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.ScrapperClient;
import edu.java.bot.UserData;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import edu.java.dto.LinkResponse;
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

        List<String> links = Arrays.stream(scrapperClient.getLinks(chatId).links())
            .map(linkResponse -> linkResponse.url().toString())
            .toList();

        return new SendMessage(chatId, "Отслеживаются: %s".formatted(String.join(", ", links)));
    }

    @Override
    public boolean supports(Update update) {
        long chatId = update.message().chat().id();

        return scrapperClient.getLinks(chatId) != null;
    }
}
