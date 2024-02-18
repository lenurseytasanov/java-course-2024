package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.UserData;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListCommand implements Command {
    private final UserData userData;

    @Autowired
    public ListCommand(UserData userData) {
        this.userData = userData;
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
        String user = update.message().from().username();
        long chatId = update.message().chat().id();

        Set<String> links = userData.getData().get(user);

        return new SendMessage(chatId, "Ресурсы: %s".formatted(String.join(", ", links)));
    }

    @Override
    public boolean supports(Update update) {
        String user = update.message().from().username();
        return userData.getData().containsKey(user);
    }
}
