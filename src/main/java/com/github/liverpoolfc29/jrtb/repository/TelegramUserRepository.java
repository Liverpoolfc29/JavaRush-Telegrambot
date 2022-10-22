package com.github.liverpoolfc29.jrtb.repository;

import com.github.liverpoolfc29.jrtb.repository.entity.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * {@link Repository} for handling with {@link TelegramUser} entity.
 */

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, String> {

    // get all records from the tg_user table that have field active = true
    List<TelegramUser> findAllByActiveTrue();
}
