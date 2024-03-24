package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Chat {

    @Id
    private Long id;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "tracking",
        joinColumns = @JoinColumn(name = "chat_id"),
        inverseJoinColumns = @JoinColumn(name = "link_id")
    )
    private Set<Link> links = new HashSet<>();

    public Chat(Long id) {
        this.id = id;
    }

    public Chat() {
    }

    public Long getId() {
        return id;
    }

    public Set<Link> getLinks() {
        return links;
    }
}
