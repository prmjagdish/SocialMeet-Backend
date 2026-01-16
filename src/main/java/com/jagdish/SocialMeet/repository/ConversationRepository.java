package com.jagdish.SocialMeet.repository;

import com.jagdish.SocialMeet.model.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("""
                SELECT c FROM Conversation c
                JOIN c.participants p
                WHERE c.type = 'PRIVATE'
                AND p.id IN (:user1, :user2)
                GROUP BY c
                HAVING COUNT(p) = 2
            """)
    Optional<Conversation> findPrivateChat(Long user1, Long user2);

    @Query("""
                SELECT DISTINCT c
                FROM Conversation c
                JOIN c.participants p
                JOIN FETCH c.participants
                WHERE p.id = :userId
            """)
    List<Conversation> findAllByParticipantsWithUsers(Long userId);
}

