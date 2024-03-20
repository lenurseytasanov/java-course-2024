package edu.java.scrapper.domain;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ChatRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ChatRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void addChat(long id) {
        jdbcTemplate.update("INSERT INTO Chat (id) VALUES (?)", id);
    }

    @Transactional
    public void removeChat(long id) {
        jdbcTemplate.update("DELETE FROM Chat WHERE id = ?", id);
    }

    @Transactional
    public List<Long> findAll() {
        return jdbcTemplate.queryForList("SELECT id FROM Chat", Long.class);
    }
}
