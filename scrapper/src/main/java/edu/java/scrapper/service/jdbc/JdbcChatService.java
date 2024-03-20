package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.TrackingRepository;
import edu.java.scrapper.service.ChatService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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
    public void unregister(long id) {
        chatRepository.addChat(id);
    }

    @Override
    public Collection<Long> listAllChatIds(long linkId) {
        return trackingRepository.findAllChats(linkId);
    }
}
