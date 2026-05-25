package com.gamesUP.gamesUP.repository;

import com.gamesUP.gamesUP.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

    Optional<AppUser> findByName(String name);
}
