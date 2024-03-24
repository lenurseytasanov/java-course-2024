package edu.java.scrapper;

import edu.java.dto.Chat;
import edu.java.dto.Link;
import edu.java.scrapper.domain.JpaChatRepository;
import edu.java.scrapper.domain.JpaLinkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JpaTest extends IntegrationTest {

    @Autowired
    private JpaLinkRepository jpaLinkRepository;

    @Autowired
    private JpaChatRepository jpaChatRepository;

    @Test
    @Transactional
    @Rollback
    void chatRepositoryTest() {
        // Arrange
        long chatId = 100;

        // Act
        jpaChatRepository.save(new Chat(chatId));
        boolean contains1 = jpaChatRepository.existsById(chatId);
        jpaChatRepository.deleteById(chatId);
        boolean contains2 = jpaChatRepository.existsById(chatId);

        // Assert
        assertTrue(contains1);
        assertFalse(contains2);
    }

    @Test
    @Transactional
    @Rollback
    void linkRepositoryTest() {
        // Arrange
        String url = "url";
        Link link = new Link(null, url, OffsetDateTime.now(), OffsetDateTime.now());

        // Act
        Link saved = jpaLinkRepository.save(link);
        boolean contains1 = jpaLinkRepository.existsById(saved.getId());
        jpaLinkRepository.deleteById(saved.getId());
        boolean contains2 = jpaLinkRepository.existsById(saved.getId());

        // Assert
        assertTrue(contains1);
        assertFalse(contains2);
        assertEquals(link.getUrl(), saved.getUrl());
    }
}
