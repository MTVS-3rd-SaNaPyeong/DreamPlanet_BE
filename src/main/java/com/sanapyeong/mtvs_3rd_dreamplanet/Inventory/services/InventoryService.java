package com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.services;

import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.dto.SaveInventoryDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.entities.Inventory;
import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(
            InventoryRepository inventoryRepository
    ) {
        this.inventoryRepository = inventoryRepository;
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

    public void findBlockInventoryByUserId(Long userId) {

        List<Inventory> inventoryList = inventoryRepository.findBlockInventoryByUserID(userId);
    }
}
