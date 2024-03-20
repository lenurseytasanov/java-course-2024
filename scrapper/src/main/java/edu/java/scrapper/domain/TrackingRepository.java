package edu.java.scrapper.domain;

import edu.java.dto.Link;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class TrackingRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TrackingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void addTracking(long chatId, long linkId) {
        jdbcTemplate.update("INSERT INTO Tracking (chat_id, link_id) VALUES (?, ?)", chatId, linkId);
    }

    @Transactional
    public void removeLink(long chatId, long linkId) {
        jdbcTemplate.update("DELETE FROM Tracking WHERE chat_id = ? AND link_id = ?", chatId, linkId);
    }

    @Transactional
    public List<Link> findAllLinks(long chatId) {
        String query = "SELECT * FROM Link JOIN Tracking ON Link.id = Tracking.link_id WHERE chat_id = ?";
        return jdbcTemplate.query(query, new LinkRowMapper(), chatId);
    }

    @Transactional
    public List<Long> findAllChats(long linkId) {
        String query = "SELECT chat_id FROM Tracking WHERE link_id = ?";
        return jdbcTemplate.query(query, new SingleColumnRowMapper<>(), linkId);
    }
}
