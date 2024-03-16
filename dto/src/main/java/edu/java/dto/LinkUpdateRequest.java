package edu.java.dto;

public record LinkUpdateRequest(long id, String url, String description, long[] tgChatIds) {
}
