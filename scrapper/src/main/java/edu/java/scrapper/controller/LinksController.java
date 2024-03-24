package edu.java.scrapper.controller;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.Link;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
public class LinksController {

    private final LinkService linkService;

    @Autowired
    public LinksController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<ListLinksResponse> getLinks(@RequestHeader("Tg-Chat-Id") long tgChatId) {
        Link[] links = linkService.listAll(tgChatId).toArray(new Link[0]);
        return ResponseEntity.ok(new ListLinksResponse(links, links.length));
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Link> addLink(
        @RequestHeader("Tg-Chat-Id") long tgChatId,
        @RequestBody AddLinkRequest addLinkRequest
    ) {
        URI uri = URI.create(addLinkRequest.link());
        Link link = linkService.add(tgChatId, uri);
        return ResponseEntity.ok(link);
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity<Link> removeLink(
        @RequestHeader("Tg-Chat-Id") long tgChatId,
        @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        URI uri = URI.create(removeLinkRequest.link());
        Link link = linkService.remove(tgChatId, uri);
        return ResponseEntity.ok(link);
    }
}
