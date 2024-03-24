package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.JpaChatRepository;
import edu.java.scrapper.domain.JpaLinkRepository;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.jpa.JpaChatService;
import edu.java.scrapper.service.jpa.JpaLinkService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {

    @Bean
    public LinkService linkService(
        JpaLinkRepository linkRepository,
        JpaChatRepository tgChatRepository
    ) {
        return new JpaLinkService(tgChatRepository, linkRepository);
    }

    @Bean
    public ChatService chatService(
        JpaLinkRepository linkRepository,
        JpaChatRepository tgChatRepository
    ) {
        return new JpaChatService(tgChatRepository, linkRepository);
    }
}
