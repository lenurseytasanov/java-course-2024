package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UntrackCommand implements Command {
    private final UserData userData;

    @Autowired
    public UntrackCommand(UserData userData) {
        this.userData = userData;
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
        String user = update.message().from().username();
        long chatId = update.message().chat().id();

        if (update.message().text().split(" ").length < 2) {
            return new SendMessage(chatId, "Введите ссылку");
        }
        String link = update.message().text().split(" ")[1];

        if (!userData.getData().get(user).contains(link)) {
            return new SendMessage(chatId, "Ресурс не отслеживается");
        }

        userData.getData().get(user).remove(link);
        return new SendMessage(chatId, "Ресурс %s удален.".formatted(link));
    }

    @Override
    public boolean supports(Update update) {
        String user = update.message().from().username();
        return userData.getData().containsKey(user);
    }
}
