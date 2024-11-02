package com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.services;

import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.dto.BlockInventoryFindResponseDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.dto.SaveInventoryDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.entities.Inventory;
import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.repositories.InventoryRepository;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.entities.PlayedBlockPlanet;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.repositories.PlayedBlockPlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final PlayedBlockPlanetRepository playedBlockPlanetRepository;

    @Autowired
    public InventoryService(
            InventoryRepository inventoryRepository,
            PlayedBlockPlanetRepository playedBlockPlanetRepository
    ) {
        this.inventoryRepository = inventoryRepository;
        this.playedBlockPlanetRepository = playedBlockPlanetRepository;
    }


    public void saveInventory(Long userId, SaveInventoryDTO storedStatusInfo) {

        Inventory inventory = new Inventory(
                userId,
                storedStatusInfo.getPlanetType(),
                storedStatusInfo.getPlayedPlanetId(),
                storedStatusInfo.getStoredStatus()
        );

        inventoryRepository.save(inventory);
    }

    public List<BlockInventoryFindResponseDTO> findBlockInventoryByUserId(Long userId) {

        return inventoryRepository.findBlockInventoryByUserId(userId)
                .stream()
                .map(BlockInventoryFindResponseDTO::new)
                .toList();

    }
}
