package edu.java.scrapper.service.jpa;

import edu.java.dto.Chat;
import edu.java.dto.Link;
import edu.java.scrapper.domain.JpaChatRepository;
import edu.java.scrapper.domain.JpaLinkRepository;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;

public class JpaLinkService implements LinkService {

    private final JpaChatRepository jpaChatRepository;

    private final JpaLinkRepository jpaLinkRepository;

    @Autowired
    public JpaLinkService(JpaChatRepository jpaChatRepository, JpaLinkRepository jpaLinkRepository) {
        this.jpaChatRepository = jpaChatRepository;
        this.jpaLinkRepository = jpaLinkRepository;
    }

    @Override
    public Link add(long tgChatId, URI url) {
        Chat chat = jpaChatRepository.findById(tgChatId).orElseThrow();
        Link link = jpaLinkRepository.save(new Link(null, url.toString(), OffsetDateTime.now(), OffsetDateTime.now()));
        link.getChats().add(chat);
        chat.getLinks().add(link);
        jpaLinkRepository.save(link);
        jpaChatRepository.save(chat);
        return link;
    }

    @Override
    public Link remove(long tgChatId, URI url) {
        Link link = StreamSupport.stream(jpaLinkRepository.findAll().spliterator(), false)
            .filter(link1 -> link1.getUrl().equals(url.toString()))
            .findFirst().orElseThrow();
        jpaLinkRepository.deleteById(link.getId());
        return link;
    }

    @Override
    public Collection<Link> listAll(long tgChatId) {
        return jpaChatRepository.findById(tgChatId).orElseThrow()
            .getLinks().stream()
            .toList();
    }

    @Override
    public Collection<Link> listAllByCheck(Duration forceCheckDelay) {
        OffsetDateTime timeFilter = OffsetDateTime.now().minus(forceCheckDelay);
        return StreamSupport.stream(jpaLinkRepository.findAll().spliterator(), false)
            .filter(link1 -> link1.getCheckedAt().isBefore(timeFilter))
            .toList();
    }

    @Override
    public void updateCheckTime(long linkId) {
        Link link = jpaLinkRepository.findById(linkId).orElseThrow();
        jpaLinkRepository.save(new Link(linkId, link.getUrl(), link.getUpdatedAt(), OffsetDateTime.now()));
    }

    @Override
    public void updateStateTime(long linkId) {
        Link link = jpaLinkRepository.findById(linkId).orElseThrow();
        jpaLinkRepository.save(new Link(linkId, link.getUrl(), OffsetDateTime.now(), link.getCheckedAt()));
    }
}
