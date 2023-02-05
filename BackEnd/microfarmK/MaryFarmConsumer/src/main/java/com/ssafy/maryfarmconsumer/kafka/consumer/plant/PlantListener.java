package com.ssafy.maryfarmconsumer.kafka.consumer.plant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.maryfarmconsumer.kafka.constants.KafkaConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class PlantListener {

    @KafkaListener(
            topics = "plant",
            groupId = KafkaConstants.GROUP_ID
    )
    public void PlantListen(String message) {
        log.info("Kafka Message: ->" + message);

        Map<Object, Object> plant = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            plant = mapper.readValue(message, new TypeReference<Map<Object, Object>>() {});
            log.info(plant.toString());
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

    }
    @KafkaListener(
            topics = "diary",
            groupId = KafkaConstants.GROUP_ID
    )
    public void DiaryListen(String message) {
        log.info("Kafka Message: ->" + message);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(message, new TypeReference<Map<Object, Object>>() {});
            log.info(map.toString());
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

//        CatalogEntity entity = repository.findByProductId((String)map.get("productId"));
//        if (entity != null) {
//            entity.setStock(entity.getStock() - (Integer)map.get("qty"));
//            repository.save(entity);
//        }
    }
}
