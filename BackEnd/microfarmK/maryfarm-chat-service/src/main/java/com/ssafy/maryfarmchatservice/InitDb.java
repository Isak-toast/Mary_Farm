package com.ssafy.maryfarmchatservice;

import com.ssafy.maryfarmchatservice.domain.chat.Message;
import com.ssafy.maryfarmchatservice.domain.chat.Room;
import com.ssafy.maryfarmchatservice.repository.MessageRepository;
import com.ssafy.maryfarmchatservice.repository.RoomRepository;
import com.ssafy.maryfarmchatservice.service.MessageService;
import com.ssafy.maryfarmchatservice.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final RoomRepository roomRepository;
        private final MessageRepository messageRepository;
        public void dbInit() {
            Room room = Room.of("123456", null,null,"1234567",null,null);
            roomRepository.save(room);
            Message message = Message.of(room, "123456", "baek",null, "안냥안냥", LocalDateTime.now());
            messageRepository.save(message);
        }
    }
}
