package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.TrackingRepository;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.jdbc.JdbcChatService;
import edu.java.scrapper.service.jdbc.JdbcLinkService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {

    @Bean
    public LinkService linkService(
        LinkRepository linkRepository,
        TrackingRepository trackingRepository
    ) {
        return new JdbcLinkService(linkRepository, trackingRepository);
    }

    @Bean ChatService chatService(
        ChatRepository chatRepository,
        TrackingRepository trackingRepository
    ) {
        return new JdbcChatService(chatRepository, trackingRepository);
    }
}
