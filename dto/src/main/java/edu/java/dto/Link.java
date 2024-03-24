package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private OffsetDateTime updatedAt;

    private OffsetDateTime checkedAt;

    @JsonIgnore
    @ManyToMany(mappedBy = "links")
    private Set<Chat> chats = new HashSet<>();

    public Link(Long id, String url, OffsetDateTime updatedAt, OffsetDateTime checkedAt) {
        this.id = id;
        this.url = url;
        this.updatedAt = updatedAt;
        this.checkedAt = checkedAt;
    }

    public Link() {
    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public OffsetDateTime getCheckedAt() {
        return checkedAt;
    }

    public Set<Chat> getChats() {
        return chats;
    }
}
