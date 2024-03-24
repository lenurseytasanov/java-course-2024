package edu.java.scrapper;

import edu.java.dto.Link;
import edu.java.dto.LinkUpdateRequest;
import edu.java.dto.UpdateResponse;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.github.GitHubClient;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.stackoverflow.StackOverflowClient;
import java.net.URI;
import java.util.Collection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class LinkUpdaterScheduler {

    @Autowired
    private LinkService linkService;
    @Autowired
    private ChatService chatService;

    @Autowired
    private GitHubClient gitHubClient;
    @Autowired
    private StackOverflowClient stackOverflowClient;

    @Autowired
    private BotClient botClient;

    @Autowired
    private ApplicationConfig config;

    private static final Logger LOGGER = LogManager.getLogger();

    @Scheduled(fixedDelayString = "#{ @scheduler.interval() }")
    public void update() {
        LOGGER.info("get update");

        Collection<Link> links = linkService.listAllByCheck(config.scheduler().forceCheckDelay());
        links.stream()
            .peek(link -> linkService.updateCheckTime(link.getId()))
            .filter(link -> parseLink(URI.create(link.getUrl())).getUpdatedAt().isAfter(link.getUpdatedAt()))
            .peek(link -> linkService.updateStateTime(link.getId()))
            .forEach(link -> botClient.sendUpdate(new LinkUpdateRequest(
                link.getId(), link.getUrl(), "source updated",
                chatService.listAllChatIds(link.getId()).stream().mapToLong(e -> e).toArray()
            )));
    }

    private UpdateResponse parseLink(URI uri) {
        if (uri.getHost().equals("stackoverflow.com")) {
            long id = Long.parseLong(uri.getPath().split("/")[2]);
            return stackOverflowClient.fetchUpdate(id);
        }
        if (uri.getHost().equals("github.com")) {
            String owner = uri.getPath().split("/")[1];
            String repo = uri.getPath().split("/")[2];
            return gitHubClient.fetchUpdate(owner, repo);
        }
        return null;
    }
}
