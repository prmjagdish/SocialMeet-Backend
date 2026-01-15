package com.jagdish.SocialMeet.service.impl.chat;

import com.jagdish.SocialMeet.model.entity.Message;
import com.jagdish.SocialMeet.repository.ConversationRepository;
import com.jagdish.SocialMeet.repository.MessageRepository;
import com.jagdish.SocialMeet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepo;
    private final ConversationRepository conversationRepo;
    private final UserRepository userRepo;

    public Message sendMessage(
            Long conversationId,
            Long senderId,
            String content
    ) {
        Message m = new Message();
        m.setConversation(conversationRepo.findById(conversationId).get());
        m.setSender(userRepo.findById(senderId).get());
        m.setContent(content);
        m.setType("TEXT");
        return messageRepo.save(m);
    }

    public List<Message> getMessages(Long conversationId) {
        return messageRepo.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }

    public void markAsSeen(Long conversationId, Long userId) {
        List<Message> messages =
                messageRepo.findByConversationIdOrderByCreatedAtAsc(conversationId);

        messages.stream()
                .filter(m -> !m.isSeen() && !m.getSender().getId().equals(userId))
                .forEach(m -> {
                    m.setSeen(true);
                    messageRepo.save(m);
                });
    }
}

