package edu.java.scrapper.service;

import java.util.Collection;

public interface ChatService {

    void register(long id);

    void get(long id);

    void unregister(long id);

    Collection<Long> listAllChatIds(long linkId);
}
