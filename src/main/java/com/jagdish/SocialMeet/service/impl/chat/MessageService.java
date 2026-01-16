package com.jagdish.SocialMeet.service.impl.chat;

import com.jagdish.SocialMeet.model.dto.chat.MessageDto;
import com.jagdish.SocialMeet.model.entity.Conversation;
import com.jagdish.SocialMeet.model.entity.Message;
import com.jagdish.SocialMeet.model.entity.User;
import com.jagdish.SocialMeet.repository.ConversationRepository;
import com.jagdish.SocialMeet.repository.MessageRepository;
import com.jagdish.SocialMeet.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final MessageRepository messageRepo;
    private final ConversationRepository conversationRepo;
    private final UserRepository userRepo;

    public Message sendMessage(Long conversationId, Long senderId, String content) {

        Conversation conversation = conversationRepo.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        User sender = userRepo.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Message m = new Message();
        m.setConversation(conversation);
        m.setSender(sender);
        m.setContent(content);
        m.setType("TEXT");
        m.setSeen(false);

        Message saved = messageRepo.save(m);

        // maintain bidirectional consistency
        conversation.getMessages().add(saved);`

        return saved;
    }

    public List<MessageDto> getMessages(Long conversationId) {
        return messageRepo.findByConversationIdOrderByCreatedAtAsc(conversationId)
                .stream()
                .map(m -> new MessageDto(
                        m.getId(),
                        m.getContent(),
                        m.getSender().getId(),
                        m.isSeen(),
                        m.getCreatedAt()
                ))
                .toList();
    }
}


