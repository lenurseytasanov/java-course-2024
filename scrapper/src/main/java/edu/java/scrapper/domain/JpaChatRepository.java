package edu.java.scrapper.domain;

import edu.java.dto.Chat;
import org.springframework.data.repository.CrudRepository;

public interface JpaChatRepository extends CrudRepository<Chat, Long> {
}
