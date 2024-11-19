package com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.repositories;

import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.entities.PostingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PostingInfoRepository extends JpaRepository<PostingInfo, Long> {


    @Query("SELECT p " +
            "FROM PostingInfo p " +
            "WHERE p.inventoryId = :inventoryId " +
                "AND p.myUniverseTrainId = :myUniverseTrainId")
    PostingInfo findByInventoryIdAndMyUniverseTrainId(Long inventoryId, Long myUniverseTrainId);
}
