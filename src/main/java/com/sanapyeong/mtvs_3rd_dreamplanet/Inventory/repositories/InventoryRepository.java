package com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.repositories;


import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.entities.Inventory;
import com.sanapyeong.mtvs_3rd_dreamplanet.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    //List<Inventory> findBlockInventoryByUserID(Long userId);



    @Query("SELECT u FROM User u WHERE u.loginId = :loginId")
    User findUserByLoginId(String loginId);
}
