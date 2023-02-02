package com.ssafy.maryfarmchatservice.util.chat.consumer;

import com.ssafy.maryfarmchatservice.domain.chat.Message;
import com.ssafy.maryfarmchatservice.repository.MessageRepository;
import com.ssafy.maryfarmchatservice.util.chat.constants.KafkaConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MessageListener {
    @Autowired
    SimpMessagingTemplate template;
    @Autowired
    MessageRepository messageRepository;

    @KafkaListener(
            topics = KafkaConstants.KAFKA_TOPIC,
            groupId = KafkaConstants.GROUP_ID
    )
    public void listen(Message message) {
        Message saveMessage = messageRepository.save(message);
        System.out.println("sending via kafka listener..");
        System.out.println("/topic/group/" + saveMessage.getRoomId());
        template.convertAndSend("/topic/group/" + saveMessage.getRoomId(), saveMessage);
    }
}
