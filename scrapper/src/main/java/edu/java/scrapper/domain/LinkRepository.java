package edu.java.scrapper.domain;

import edu.java.dto.Link;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class LinkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LinkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final int PARAMETER_INDEX = 3;
    private static final String SELECT_LINK_BY_URL = "SELECT * FROM Link WHERE url = ?";

    @Transactional
    public Link addLink(Link link) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                .prepareStatement("INSERT INTO Link (url, updated_at, checked_at) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            Timestamp timestamp = Timestamp.valueOf(
                OffsetDateTime.now().atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()
            );
            ps.setString(1, link.getUrl());
            ps.setTimestamp(2, timestamp);
            ps.setTimestamp(PARAMETER_INDEX, timestamp);
            return ps;
        }, keyHolder);

        long id = (long) keyHolder.getKeys().get("id");

        return jdbcTemplate.queryForObject("SELECT * FROM Link WHERE id = ?", new LinkRowMapper(), id);
    }

    @Transactional
    public Link removeLink(String url) {
        Link link = jdbcTemplate.queryForObject(SELECT_LINK_BY_URL, new LinkRowMapper(), url);

        jdbcTemplate.update("DELETE FROM Link WHERE url = ?", url);
        return link;
    }

    @Transactional
    public Link getLink(String url) {
        return jdbcTemplate.queryForObject(SELECT_LINK_BY_URL, new LinkRowMapper(), url);
    }

    @Transactional
    public List<Link> findAll() {
        return jdbcTemplate.query("SELECT * FROM Link", new LinkRowMapper());
    }

    @Transactional
    public List<Link> findByLastCheck(Duration timeFromLastUpdate) {
        String query = "SELECT * FROM Link JOIN Tracking ON Link.id = Tracking.link_id WHERE checked_at < ?";
        Timestamp timeFilter = Timestamp.valueOf(OffsetDateTime.now()
            .minus(timeFromLastUpdate)
            .toLocalDateTime()
        );
        return jdbcTemplate.query(query, new LinkRowMapper(), timeFilter);
    }

    @Transactional
    public void updateCheckTime(long id) {
        String query = "UPDATE Link SET checked_at = ? WHERE id = ?";
        jdbcTemplate.update(query, OffsetDateTime.now(), id);
    }

    @Transactional
    public void updateTime(long id) {
        String query = "UPDATE Link SET updated_at = ? WHERE id = ?";
        jdbcTemplate.update(query, OffsetDateTime.now(), id);
    }
}
