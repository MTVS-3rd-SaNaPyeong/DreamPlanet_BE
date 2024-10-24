package com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.repositories;


import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
}
