package edu.java.scrapper.service;

import edu.java.dto.Link;
import java.net.URI;
import java.util.Collection;

public interface LinkService {

    Link add(long tgChatId, URI url);

    Link remove(long tgChatId, URI url);

    Collection<Link> listAll(long tgChatId);

    Collection<Link> listAllByCheck();

    void updateCheckTime(long linkId);

    void updateStateTime(long linkId);
}
