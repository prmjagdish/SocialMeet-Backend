package com.jagdish.SocialMeet.controller.chat;

import com.jagdish.SocialMeet.model.dto.chat.ConversationDto;
import com.jagdish.SocialMeet.model.dto.chat.MessageDto;
import com.jagdish.SocialMeet.model.entity.Conversation;
import com.jagdish.SocialMeet.model.entity.Message;
import com.jagdish.SocialMeet.service.impl.chat.ConversationService;
import com.jagdish.SocialMeet.service.impl.chat.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final ConversationService conversationService;
    private final MessageService messageService;
    private final SimpMessagingTemplate template;

    /**
     * Client sends:
     * {
     *   "senderId": 1,
     *   "receiverId": 2,
     *   "content": "Hello"
     * }
     */
    @MessageMapping("/chat.send")
    public void send(Map<String, Object> payload) {

        Long senderId = Long.parseLong(payload.get("senderId").toString());
        Long receiverId = Long.parseLong(payload.get("receiverId").toString());
        String content = payload.get("content").toString();

        // 1️⃣ Get or create conversation
        ConversationDto conversation =
                conversationService.getOrCreatePrivate(senderId, receiverId);

        // 2️⃣ Save message
        Message saved =
                messageService.sendMessage(
                        conversation.getId(),
                        senderId,
                        content
                );

        // 3️⃣ Convert ENTITY → DTO (CRITICAL STEP)
        MessageDto response = new MessageDto(
                saved.getId(),
                saved.getContent(),
                saved.getSender().getId(),
                saved.isSeen(),
                saved.getCreatedAt()
        );

        // 4️⃣ Broadcast lightweight DTO
        template.convertAndSend(
                "/topic/chat/" + conversation.getId(),
                response
        );
    }
}

