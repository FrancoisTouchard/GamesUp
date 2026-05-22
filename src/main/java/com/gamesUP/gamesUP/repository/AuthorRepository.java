package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {
    Optional<Author> findByName(String name);
    boolean existsByName(String name);
}
