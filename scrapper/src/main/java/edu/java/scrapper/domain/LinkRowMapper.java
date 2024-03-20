package edu.java.scrapper.domain;

import edu.java.dto.Link;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import org.springframework.jdbc.core.RowMapper;

public class LinkRowMapper implements RowMapper<Link> {

    @Override
    public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Link(
            rs.getLong("id"),
            rs.getString("url"),
            rs.getTimestamp("updated_at").toInstant().atOffset(ZoneOffset.UTC),
            rs.getTimestamp("checked_at").toInstant().atOffset(ZoneOffset.UTC)
        );
    }
}
