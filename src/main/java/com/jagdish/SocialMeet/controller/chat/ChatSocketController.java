package com.jagdish.SocialMeet.controller.chat;

import com.jagdish.SocialMeet.model.dto.chat.ChatSendDto;
import com.jagdish.SocialMeet.model.dto.chat.ConversationDto;
import com.jagdish.SocialMeet.model.dto.chat.MessageDto;
import com.jagdish.SocialMeet.model.entity.Message;
import com.jagdish.SocialMeet.service.impl.chat.ConversationService;
import com.jagdish.SocialMeet.service.impl.chat.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


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
    public void send(ChatSendDto dto) {

        System.out.println("📨 Message received in backend:");
        System.out.println("Sender: " + dto.getSenderId());
        System.out.println("Receiver: " + dto.getReceiverId());
        System.out.println("Content: " + dto.getContent());

        ConversationDto conversation =
                conversationService.getOrCreatePrivate(
                        dto.getSenderId(),
                        dto.getReceiverId()
                );

        Message saved =
                messageService.sendMessage(
                        conversation.getId(),
                        dto.getSenderId(),
                        dto.getContent()
                );

        System.out.println("✅ Message saved with ID: " + saved.getId());

        MessageDto response = new MessageDto(
                saved.getId(),
                saved.getContent(),
                saved.getSender().getId(),
                saved.isSeen(),
                saved.getCreatedAt()
        );

        System.out.println("📢 Broadcasting to /topic/chat/" + conversation.getId());

        template.convertAndSend(
                "/topic/chat/" + conversation.getId(),
                response
        );
    }

}

