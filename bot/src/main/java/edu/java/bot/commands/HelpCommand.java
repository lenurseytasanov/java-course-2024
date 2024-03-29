package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {

    @Override
    public String command() {
        return "help";
    }

    @Override
    public String description() {
        return "get list of commands";
    }

    @Override
    public SendMessage handle(Update update) {
        String message = """
                /start -- зарегистрировать пользователя
                /help -- вывести окно с командами
                /track -- начать отслеживание ссылки
                /untrack -- прекратить отслеживание ссылки
                /list -- показать список отслеживаемых ссылок
                """;

        long chatId = update.message().chat().id();
        return new SendMessage(chatId, message);
    }

    @Override
    public boolean supports(Update update) {
        return true;
    }
}
