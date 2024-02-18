package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.UserData;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {

    private final UserData userData;

    @Autowired
    public StartCommand(UserData userData) {
        this.userData = userData;
    }

    @Override
    public String command() {
        return "start";
    }

    @Override
    public String description() {
        return "register user";
    }

    @Override
    public SendMessage handle(Update update) {
        String user = update.message().from().username();
        long chatId = update.message().chat().id();

        if (userData.getData().containsKey(user)) {
            return new SendMessage(chatId, "Пользователь уже существует");
        }

        userData.getData().put(user, new HashSet<>());

        return new SendMessage(chatId, "Пользователь %s зарегистрирован.".formatted(user));
    }

    @Override
    public boolean supports(Update update) {
        return true;
    }
}
