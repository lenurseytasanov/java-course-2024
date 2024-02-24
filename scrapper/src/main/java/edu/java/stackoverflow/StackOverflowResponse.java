package edu.java.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

public class StackOverflowResponse {

    private OffsetDateTime updateAt;

    public OffsetDateTime getUpdateAt() {
        return updateAt;
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("items")
    private void unpackNested(Object[] items) {
        var question = (Map<String, Object>) items[0];
        this.updateAt = Instant.ofEpochSecond((Integer) question.get("last_activity_date"))
            .atOffset(ZoneOffset.ofTotalSeconds(0));
    }
}
