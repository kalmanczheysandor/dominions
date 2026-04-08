package hu.kalmancheysandor.applications.dominions.servers.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    public String sendToAll(String message) {
        System.out.println("Üzenet mindenkihez: " + message);
        return message;
    }


    @MessageMapping("/private-message")
    @SendToUser("/queue/private")
    public String processMessageFromClient(@Payload String message, Principal principal) throws Exception {
        System.out.println("Üzenet önállóan: " + message);
        return message;
    }


    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }


//    // Egy másik felhasználónak küldött üzenet
//    @MessageMapping("/direct-message")  // Üzenet küldése a "/app/direct-message" URL-en
//    @SendToUser("/queue/private")  // Az üzenet a konkrét felhasználónak megy, aki kapja
//    public String sendToOtherUser(String message) {
//        System.out.println("Üzenet másnak: " + message);
//        return message;
//    }

    @MessageMapping("/direct-message")
    public void sendToOtherUser(String message) {
//        String recipient = privateMessage.getRecipient();
//        String recipientSessionId = userSessionRegistry.getSessionIdForUser(recipient);

        System.out.println("Üzenet másnak: " + message);
        messagingTemplate.convertAndSendToUser("test@test.com", "/queue/private", message);

    }

}
