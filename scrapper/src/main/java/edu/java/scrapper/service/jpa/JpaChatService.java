package edu.java.scrapper.service.jpa;

import edu.java.dto.Chat;
import edu.java.scrapper.domain.JpaChatRepository;
import edu.java.scrapper.domain.JpaLinkRepository;
import edu.java.scrapper.service.ChatService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;

public class JpaChatService implements ChatService {

    private final JpaChatRepository jpaChatRepository;

    private final JpaLinkRepository jpaLinkRepository;

    @Autowired
    public JpaChatService(JpaChatRepository jpaChatRepository, JpaLinkRepository jpaLinkRepository) {
        this.jpaChatRepository = jpaChatRepository;
        this.jpaLinkRepository = jpaLinkRepository;
    }

    @Override
    public void register(long id) {
        jpaChatRepository.save(new Chat(id));
    }

    @Override
    public void get(long id) {
        jpaChatRepository.findById(id).orElseThrow();
    }

    @Override
    public void unregister(long id) {
        jpaChatRepository.deleteById(id);
    }

    @Override
    public Collection<Long> listAllChatIds(long linkId) {
        return jpaLinkRepository.findById(linkId).orElseThrow()
            .getChats().stream()
            .map(Chat::getId)
            .toList();
    }
}
