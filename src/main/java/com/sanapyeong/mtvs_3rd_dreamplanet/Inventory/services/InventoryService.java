package com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.services;

import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.dto.BlockInventoryFindResponseDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.dto.SaveInventoryDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.entities.Inventory;
import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.repositories.InventoryRepository;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.entities.PlayedBlockPlanet;
import com.sanapyeong.mtvs_3rd_dreamplanet.playedBlockPlanet.repositories.PlayedBlockPlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @Transactional
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

    public Inventory findInventoryById(Long nextId) {

        return inventoryRepository.findById(nextId).orElseThrow();
    }

    @Transactional
    public void updatePostedLocation(Long id, Long postedLocation) {

        Optional<Inventory> optionalInventory = inventoryRepository.findById(id);

        // 엔티티가 존재하는지 확인
        if (optionalInventory.isPresent()) {
            Inventory inventory = optionalInventory.get();

            // postedLocation 정보 업데이트
            if (postedLocation != null) {
                inventory.setPostedLocation(postedLocation);
            }

            // 자동으로 변경된 내용이 영속성 컨텍스트에 반영됨 (Transactional 덕분)
        } else {
            throw new IllegalArgumentException("ID에 해당하는 인벤토리가 존재하지 않습니다.");
        }
    }
}
