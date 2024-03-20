package edu.java.scrapper;

import edu.java.dto.Link;
import edu.java.scrapper.domain.ChatRepository;
import edu.java.scrapper.domain.LinkRepository;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class JdbcLinkTest extends IntegrationTest {
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private ChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    void chatRepositoryTest() {
        // Arrange
        long chatId = 100;
        long chatCount = 1;

        // Act
        chatRepository.addChat(chatId);
        List<Long> data0 = chatRepository.findAll();
        chatRepository.removeChat(chatId);
        List<Long> data1 = chatRepository.findAll();

        // Assert
        assertEquals(chatCount, data0.size());
        assertEquals(chatId, data0.getFirst().longValue());
        assertEquals(0, data1.size());
    }

    @Test
    @Transactional
    @Rollback
    void linkRepositoryTest() {
        // Arrange
        String url = "url";
        Link link = new Link(0L, url, OffsetDateTime.now(), OffsetDateTime.now());

        // Act
        linkRepository.addLink(link);
        List<Link> data0 = linkRepository.findAll();
        linkRepository.removeLink(url);
        List<Link> data1 = linkRepository.findAll();

        // Assert
        assertEquals(url, data0.getFirst().url());
        assertEquals(0, data1.size());
    }
}
