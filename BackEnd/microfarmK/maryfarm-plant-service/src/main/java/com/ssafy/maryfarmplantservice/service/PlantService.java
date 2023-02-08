package com.ssafy.maryfarmplantservice.service;

import com.ssafy.maryfarmplantservice.domain.plant.Plant;
import com.ssafy.maryfarmplantservice.repository.PlantRepository;
import com.ssafy.maryfarmplantservice.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PlantService {
    private final PlantRepository plantRepository;
    private final TagRepository tagRepository;
    @Transactional
    public Plant savePlant(String userId, String title, String name) {
        Plant plant = Plant.of(userId,title,name);
        Plant savePlant = plantRepository.save(plant);
        return savePlant;
    }

    public List<Plant> searchPlantsByUserId(String userId) {
        return plantRepository.findByUserId(userId);
    }

    public Plant findPlant(String plantId) {
        Optional<Plant> plant = plantRepository.findById(plantId);
        return plant.get();
    }

    public List<Plant> searchPlantByMonth(String userId, Integer year, Integer month) {
        return plantRepository.findPlantByMonth(userId,year,month);
    }

    @Transactional
    public Plant doHarvest(String plantId) {
        Optional<Plant> plant = plantRepository.findById(plantId);
        plant.get().setHarvestTime(LocalDateTime.now());
        plant.get().setActive(false);
        return plant.get();
    }
}
