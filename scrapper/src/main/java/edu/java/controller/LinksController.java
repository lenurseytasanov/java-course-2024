package edu.java.controller;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.LinkResponse;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import java.net.MalformedURLException;
import java.net.URI;
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

    @GetMapping
    @ResponseBody
    public ResponseEntity<ListLinksResponse> getLinks(@RequestHeader("Tg-Chat-Id") long tgChatId) {
        return ResponseEntity.ok(new ListLinksResponse(new LinkResponse[0], 0));
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id") long tgChatId,
        @RequestBody AddLinkRequest addLinkRequest
    ) {
        try {
            return ResponseEntity.ok(new LinkResponse(0, URI.create("").toURL()));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping
    public ResponseEntity<LinkResponse> removeLink(
        @RequestHeader("Tg-Chat-Id") long tgChatId,
        @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        try {
            return ResponseEntity.ok(new LinkResponse(0, URI.create("").toURL()));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
