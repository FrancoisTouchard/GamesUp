package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GenreRepository extends JpaRepository<Genre, UUID> {
    Optional<Genre> findByName(String name);

    boolean existsByName(String name);

}
