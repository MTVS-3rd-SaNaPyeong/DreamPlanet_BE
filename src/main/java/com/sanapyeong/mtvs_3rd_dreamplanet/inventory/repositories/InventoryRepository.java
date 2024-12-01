package com.sanapyeong.mtvs_3rd_dreamplanet.inventory.repositories;


import com.sanapyeong.mtvs_3rd_dreamplanet.inventory.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // PlanetType BLOCK_TYPE = PlanetType.BLOCK;

    @Query("SELECT i.id, i.playedPlanetId, p.postedLocation, b.completedWork " +
            "FROM Inventory i " +
                "LEFT JOIN PlayedBlockPlanet b on i.playedPlanetId = b.id " +
                "LEFT JOIN PostingInfo p on i.id = p.inventoryId AND p.myUniverseTrainId = :myUniverseTrainId " +
            "WHERE i.userId = :userId " +
                "AND i.planetType = com.sanapyeong.mtvs_3rd_dreamplanet.inventory.enums.PlanetType.BLOCK " +
                "AND i.storedStatus = true")
    List<Object[]> findBlockInventoryByUserIdAndMyUniverseTrainId(Long userId, Long myUniverseTrainId);

    @Query("SELECT i FROM Inventory i WHERE i.userId = :userId")
    List<Inventory> findInventoriesByUserId(Long userId);
}
