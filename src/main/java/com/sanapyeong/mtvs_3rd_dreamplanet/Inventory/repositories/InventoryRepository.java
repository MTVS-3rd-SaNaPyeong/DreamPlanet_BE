package com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.repositories;


import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.dto.BlockInventoryFindResponseDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.entities.Inventory;
import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.enums.PlanetType;
import com.sanapyeong.mtvs_3rd_dreamplanet.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // PlanetType BLOCK_TYPE = PlanetType.BLOCK;

    @Query("SELECT i.playedPlanetId, i.postedLocation, b.completedWork " +
            "FROM Inventory i LEFT JOIN PlayedBlockPlanet b on i.playedPlanetId = b.id " +
            "WHERE i.userId = :userId AND i.planetType = com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.enums.PlanetType.BLOCK AND i.storedStatus = true")
    List<Object[]> findBlockInventoryByUserId(Long userId);

}
