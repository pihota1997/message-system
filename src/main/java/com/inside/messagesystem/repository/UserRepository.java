package com.inside.messagesystem.repository;

import com.inside.messagesystem.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    @Query("select user from UserEntity user left join fetch user.roles where user.name = :name")
    Optional<UserEntity> findByName(String name);
}
