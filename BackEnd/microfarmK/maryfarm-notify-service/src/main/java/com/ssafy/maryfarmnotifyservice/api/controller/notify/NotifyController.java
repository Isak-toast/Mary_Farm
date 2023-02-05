package com.ssafy.maryfarmnotifyservice.api.controller.notify;

import com.ssafy.maryfarmnotifyservice.api.dto.notify.request.CreateNotifyRequestDTO;
import com.ssafy.maryfarmnotifyservice.domain.notify.Notify;
import com.ssafy.maryfarmnotifyservice.kafka.dto.Status;
import com.ssafy.maryfarmnotifyservice.kafka.producer.notify.NotifyProducer;
import com.ssafy.maryfarmnotifyservice.service.NotifyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class NotifyController {
    private final NotifyService notifyService;
    private final NotifyProducer notifyProducer;

    @PostMapping("/notify/save")
    public ResponseEntity<?> saveNotify(@RequestBody CreateNotifyRequestDTO dto) {
        Notify saveNotify = notifyService.saveNotify(dto.getType(), dto.getContent(), dto.getUserId());
        return ResponseEntity.ok().build();
    }
}
