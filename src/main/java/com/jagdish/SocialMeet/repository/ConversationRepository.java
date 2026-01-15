package com.jagdish.SocialMeet.repository;

import com.jagdish.SocialMeet.model.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("""
      SELECT c FROM Conversation c
      JOIN c.participants p1
      JOIN c.participants p2
      WHERE c.type = 'PRIVATE'
      AND p1.id = :user1
      AND p2.id = :user2
    """)
    Optional<Conversation> findPrivateChat(Long user1, Long user2);

    List<Conversation> findAllByParticipants_Id(Long userId);
}

