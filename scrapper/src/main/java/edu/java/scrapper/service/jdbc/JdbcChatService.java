package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.TrackingRepository;
import edu.java.scrapper.service.ChatService;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class JdbcChatService implements ChatService {

    private final ChatRepository chatRepository;
    private final TrackingRepository trackingRepository;

    @Autowired
    public JdbcChatService(ChatRepository chatRepository, TrackingRepository trackingRepository) {
        this.chatRepository = chatRepository;
        this.trackingRepository = trackingRepository;
    }

    @Override
    public void register(long id) {
        chatRepository.addChat(id);
    }

    @Override
    public void get(long id) {
        List<Long> chatIds = chatRepository.findAll();
        chatIds.indexOf(id);
    }

    @Override
    public void unregister(long id) {
        trackingRepository.findAllLinks(id).forEach(link -> trackingRepository.removeLink(id, link.getId()));
        chatRepository.removeChat(id);
    }

    @Override
    public Collection<Long> listAllChatIds(long linkId) {
        return trackingRepository.findAllChats(linkId);
    }
}
