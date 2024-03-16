package edu.java.bot.controller;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.Bot;
import edu.java.dto.LinkUpdateRequest;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
public class UpdatesController {

    private final Bot bot;

    @Autowired
    public UpdatesController(Bot bot) {
        this.bot = bot;
    }

    @PostMapping
    public void sendUpdate(@RequestBody LinkUpdateRequest linkUpdateRequest) {
        Arrays.stream(linkUpdateRequest.tgChatIds())
            .forEach(chatId -> bot.execute(new SendMessage(
                chatId,
                "Ресурс %s был обновлен".formatted(linkUpdateRequest.url())
            )));
    }
}
