package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.configuration.ApplicationConfig;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Bot extends TelegramBot implements UpdatesListener {

    @Autowired
    private UserMessageProcessor userMessageProcessor;

    public Bot(ApplicationConfig config) {
        super(config.telegramToken());
    }

    public void start() {
        this.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> list) {
        list.forEach(update -> {
            SendMessage message = userMessageProcessor.process(update);
            this.execute(message);
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
