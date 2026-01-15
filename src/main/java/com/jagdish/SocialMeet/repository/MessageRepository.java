package com.jagdish.SocialMeet.repository;

import com.jagdish.SocialMeet.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByConversationIdOrderByCreatedAtAsc(Long conversationId);

    long countByConversationIdAndSeenFalseAndSender_IdNot(
            Long conversationId, Long userId
    );
}

