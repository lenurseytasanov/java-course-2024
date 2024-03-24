package edu.java.scrapper.controller;

import edu.java.scrapper.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
public class TgChatController {

    private final ChatService chatService;


    @Autowired
    public TgChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Long> addChat(@PathVariable long id) {
        chatService.register(id);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Long> getChat(@PathVariable long id) {
        chatService.get(id);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteChat(@PathVariable long id) {
        chatService.unregister(id);
        return ResponseEntity.ok(id);
    }
}
