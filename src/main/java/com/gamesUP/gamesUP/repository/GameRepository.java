package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameRepository extends JpaRepository<Game, UUID> {

    boolean existsByName(String name);
    Optional<Game> findByName(String name);
    List<Game> findByPublisherId(UUID publisherId);
    List<Game> findByAuthorsId(UUID authorId);

}
