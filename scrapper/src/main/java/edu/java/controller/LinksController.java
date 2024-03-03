package edu.java.controller;

import edu.java.dto.LinkRequest;
import edu.java.dto.LinkResponse;
import edu.java.dto.ListLinksResponse;
import edu.java.repository.ChatRepository;
import edu.java.repository.LinkData;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
public class LinksController {

    private final ChatRepository chatRepository;

    @Autowired
    public LinksController(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @GetMapping
    public ListLinksResponse getLinks(@RequestHeader("Tg-Chat-Id") long tgChatId) {
        Map<String, LinkData> links = chatRepository.getLinks(tgChatId);
        return new ListLinksResponse(links.entrySet().stream()
            .map(entry -> {
                try {
                    return new LinkResponse(
                        entry.getValue().id(),
                        URI.create(entry.getKey()).toURL()
                    );
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            })
            .toArray(LinkResponse[]::new),
            links.size());
    }

    @PostMapping
    public LinkResponse addLink(
        @RequestHeader("Tg-Chat-Id") long tgChatId,
        @RequestBody LinkRequest linkRequest) {
        chatRepository.addLink(tgChatId, linkRequest.link());

        try {
            return new LinkResponse(
                chatRepository.getLinks(tgChatId).get(linkRequest.link()).id(),
                URI.create(linkRequest.link()).toURL()
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping
    public LinkResponse removeLink(
        @RequestHeader("Tg-Chat-Id") long tgChatId,
        @RequestBody LinkRequest linkRequest) {
        chatRepository.removeLink(tgChatId, linkRequest.link());

        try {
            return new LinkResponse(
                chatRepository.getLinks(tgChatId).get(linkRequest.link()).id(),
                URI.create(linkRequest.link()).toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
