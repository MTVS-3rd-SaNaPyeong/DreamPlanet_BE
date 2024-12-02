package com.sanapyeong.mtvs_3rd_dreamplanet.inventory.repositories;

import com.sanapyeong.mtvs_3rd_dreamplanet.inventory.entities.PostingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostingInfoRepository extends JpaRepository<PostingInfo, Long> {


    @Query("SELECT p " +
            "FROM PostingInfo p " +
            "WHERE p.inventoryId = :inventoryId " +
                "AND p.myUniverseTrainId = :myUniverseTrainId")
    PostingInfo findByInventoryIdAndMyUniverseTrainId(Long inventoryId, Long myUniverseTrainId);


    @Query("SELECT p " +
            "FROM PostingInfo p " +
            "WHERE p.myUniverseTrainId = :myUniverseTrainId")
    List<PostingInfo> findByMyUniverseTrainId(Long myUniverseTrainId);
}
