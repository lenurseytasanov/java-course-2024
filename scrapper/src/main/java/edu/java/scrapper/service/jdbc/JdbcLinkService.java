package edu.java.scrapper.service.jdbc;

import edu.java.dto.Link;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.TrackingRepository;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import java.time.Duration;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;

public class JdbcLinkService implements LinkService {

    private final LinkRepository linkRepository;
    private final TrackingRepository trackingRepository;

    @Autowired
    private ApplicationConfig config;

    @Autowired
    public JdbcLinkService(LinkRepository linkRepository, TrackingRepository trackingRepository) {
        this.linkRepository = linkRepository;
        this.trackingRepository = trackingRepository;
    }

    @Override
    public Link add(long tgChatId, URI url) {
        Link link = linkRepository.addLink(new Link(null, url.toString(), null, null));
        trackingRepository.addTracking(tgChatId, link.getId());
        return link;
    }

    @Override
    public Link remove(long tgChatId, URI url) {
        Link link = linkRepository.getLink(url.toString());
        trackingRepository.removeLink(tgChatId, link.getId());
        linkRepository.removeLink(url.toString());
        return link;
    }

    @Override
    public Collection<Link> listAll(long tgChatId) {
        return trackingRepository.findAllLinks(tgChatId);
    }

    @Override
    public Collection<Link> listAllByCheck(Duration forceCheckDelay) {
        return linkRepository.findByLastCheck(forceCheckDelay);
    }

    @Override
    public void updateCheckTime(long linkId) {
        linkRepository.updateCheckTime(linkId);
    }

    @Override
    public void updateStateTime(long linkId) {
        linkRepository.updateTime(linkId);
    }
}
