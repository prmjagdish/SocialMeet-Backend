package com.jagdish.SocialMeet.service.impl.chat;

import com.jagdish.SocialMeet.model.dto.chat.ConversationDto;
import com.jagdish.SocialMeet.model.dto.chat.UserChatDto;
import com.jagdish.SocialMeet.model.entity.Conversation;
import com.jagdish.SocialMeet.repository.ConversationRepository;
import com.jagdish.SocialMeet.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ConversationService {

    private final ConversationRepository conversationRepo;
    private final UserRepository userRepo;

    public ConversationDto getOrCreatePrivate(Long user1, Long user2) {

        Conversation conversation = conversationRepo.findPrivateChat(user1, user2)
                .orElseGet(() -> {
                    Conversation c = new Conversation();
                    c.setType("PRIVATE");
                    c.getParticipants().add(userRepo.findById(user1).orElseThrow());
                    c.getParticipants().add(userRepo.findById(user2).orElseThrow());
                    return conversationRepo.save(c);
                });

        return mapToDto(conversation);
    }

    public List<ConversationDto> getUserConversations(Long userId) {
        return conversationRepo.findAllByParticipantsWithUsers(userId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private ConversationDto mapToDto(Conversation c) {
        return new ConversationDto(
                c.getId(),
                c.getType(),
                c.getParticipants().stream()
                        .map(u -> new UserChatDto(
                                u.getId(),
                                u.getUsername(),
                                u.getAvatarUrl()
                        ))
                        .toList()
        );
    }
}
