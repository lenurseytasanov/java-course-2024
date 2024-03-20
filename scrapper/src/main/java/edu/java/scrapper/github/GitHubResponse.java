package edu.java.scrapper.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.dto.UpdateResponse;
import java.time.OffsetDateTime;

public record GitHubResponse(
    @JsonProperty("updated_at") OffsetDateTime updatedAt) implements UpdateResponse {
    @Override
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
