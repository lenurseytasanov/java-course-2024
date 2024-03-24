package edu.java.scrapper.domain;

import edu.java.dto.Link;
import org.springframework.data.repository.CrudRepository;

public interface JpaLinkRepository extends CrudRepository<Link, Long> {
}
