package edu.java.repository;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ChatRepository {

    private long count = 0;

    private final Map<Long, Map<String, LinkData>> data;

    public ChatRepository() {
        this.data = new HashMap<>();
    }

    public Map<Long, Map<String, LinkData>> getData() {
        return this.data;
    }

    public void addChat(long tgChatId) {
        this.data.put(tgChatId, new HashMap<>());
    }

    public void removeChat(long tgChatId) {
        this.data.remove(tgChatId);
    }

    public Map<String, LinkData> getLinks(long tgChatId) {
        return this.data.get(tgChatId);
    }

    public void addLink(long tgChatId, String url) {
        this.data.get(tgChatId).put(url, new LinkData(++count, OffsetDateTime.now()));
    }

    public void removeLink(long tgChatId, String url) {
        this.data.get(tgChatId).remove(url);
    }
}
