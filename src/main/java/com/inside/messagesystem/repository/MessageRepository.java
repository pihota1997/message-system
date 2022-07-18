package com.inside.messagesystem.repository;

import com.inside.messagesystem.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    @Query(nativeQuery = true, value = "select * from message order by created_at desc limit 10")
    List<MessageEntity> findLast10Messages();
}
