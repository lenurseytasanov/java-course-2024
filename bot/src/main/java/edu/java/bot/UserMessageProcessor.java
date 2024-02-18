package edu.java.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMessageProcessor {

    private final Map<String, Command> commands;

    public static final String UNKNOWN_COMMAND = "Неизвестная команда";

    public static final String UNAUTHORISED = "Пользователь не зарегистрирован";

    @Autowired
    public UserMessageProcessor(Map<String, Command> commands) {
        this.commands = commands;
    }

    SendMessage process(Update update) {
        long chatId = update.message().chat().id();
        String command = update.message().text().split(" ")[0];

        Command handler = this.commands.get(command.substring(1) + "Command");
        if (handler == null) {
            return new SendMessage(chatId, UNKNOWN_COMMAND);
        }
        if (!handler.supports(update)) {
            return new SendMessage(chatId, UNAUTHORISED);
        }
        return handler.handle(update);
    }
}
