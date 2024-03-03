package edu.java.controller;

import edu.java.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.HashSet;

@RestController
@RequestMapping("/tg-chat")
public class TgChatController {

    private final ChatRepository chatRepository;

    @Autowired
    public TgChatController(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @PostMapping("/{id}")
    public void addChat(@PathVariable long id) {
        chatRepository.addChat(id);
    }

    @DeleteMapping("/{id}")
    public void deleteChat(@PathVariable long id) {
        chatRepository.removeChat(id);
    }
}
