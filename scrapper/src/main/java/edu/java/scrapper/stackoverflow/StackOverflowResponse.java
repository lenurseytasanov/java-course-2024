package edu.java.scrapper.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.dto.UpdateResponse;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

public class StackOverflowResponse implements UpdateResponse {

    private OffsetDateTime updatedAt;

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("items")
    private void unpackNested(Object[] items) {
        var question = (Map<String, Object>) items[0];
        this.updatedAt = Instant.ofEpochSecond((Integer) question.get("last_activity_date"))
            .atOffset(ZoneOffset.ofTotalSeconds(0));
    }

}
