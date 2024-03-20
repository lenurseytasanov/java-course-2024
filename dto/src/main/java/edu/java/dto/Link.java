package edu.java.dto;

import java.time.OffsetDateTime;

public record Link(Long id, String url, OffsetDateTime updatedAt, OffsetDateTime checkedAt) {
}
