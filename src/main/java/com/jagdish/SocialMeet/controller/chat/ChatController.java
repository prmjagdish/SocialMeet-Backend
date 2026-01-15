package com.jagdish.SocialMeet.controller.chat;

import com.jagdish.SocialMeet.model.dto.chat.ConversationDto;
import com.jagdish.SocialMeet.model.entity.Conversation;
import com.jagdish.SocialMeet.model.entity.Message;
import com.jagdish.SocialMeet.model.entity.User;
import com.jagdish.SocialMeet.service.impl.chat.ConversationService;
import com.jagdish.SocialMeet.service.impl.chat.MessageService;
import com.jagdish.SocialMeet.utilies.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ConversationService conversationService;
    private final MessageService messageService;
    private final AuthUtil authUtil;

    // Start or get private conversation
    @PostMapping("/start/{receiverId}")
    public ConversationDto startChat(@PathVariable Long receiverId) {
        User user = authUtil.getCurrentUser();
        return conversationService.getOrCreatePrivate(user.getId(), receiverId);
    }

    @GetMapping("/conversations")
    public List<ConversationDto> conversations() {
        User user = authUtil.getCurrentUser();
        return conversationService.getUserConversations(user.getId());
    }

    @GetMapping("/messages/{conversationId}")
    public List<Message> messages(@PathVariable Long conversationId) {

        User user = authUtil.getCurrentUser();

        messageService.markAsSeen(conversationId, user.getId());
        return messageService.getMessages(conversationId);
    }
}


