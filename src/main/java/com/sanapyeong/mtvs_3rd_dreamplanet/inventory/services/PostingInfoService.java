package com.sanapyeong.mtvs_3rd_dreamplanet.inventory.services;

import com.sanapyeong.mtvs_3rd_dreamplanet.inventory.entities.Inventory;
import com.sanapyeong.mtvs_3rd_dreamplanet.inventory.entities.PostingInfo;
import com.sanapyeong.mtvs_3rd_dreamplanet.inventory.repositories.InventoryRepository;
import com.sanapyeong.mtvs_3rd_dreamplanet.inventory.repositories.PostingInfoRepository;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.entities.MyUniverseTrain;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.repositories.MyUniverseTrainRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostingInfoService {

    private final PostingInfoRepository postingInfoRepository;
    private final MyUniverseTrainRepository myUniverseTrainRepository;
    private final InventoryRepository inventoryRepository;

    public PostingInfoService(
            PostingInfoRepository postingInfoRepository,
            MyUniverseTrainRepository myUniverseTrainRepository, InventoryRepository inventoryRepository) {
        this.postingInfoRepository = postingInfoRepository;
        this.myUniverseTrainRepository = myUniverseTrainRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public void savePosingInfoByInventory(Long userId, Long inventoryId) {

        List<MyUniverseTrain> myUniverseTrainList = myUniverseTrainRepository.findMyUniverseTrainsTemp(userId);

        for(MyUniverseTrain myUniverseTrain : myUniverseTrainList) {
            PostingInfo postingInfo = new PostingInfo(inventoryId, myUniverseTrain.getId());

            postingInfoRepository.save(postingInfo);
        }
    }

    @Transactional
    public void savePostingInfoByMyUniverseTrain(Long userId, Long myUniverseTrainId) {

        List<Inventory> inventoryList = inventoryRepository.findInventoriesByUserId(userId);

        for(Inventory inventory : inventoryList){
            PostingInfo postingInfo = new PostingInfo(inventory.getId(), myUniverseTrainId);

            postingInfoRepository.save(postingInfo);
        }
    }

    @Transactional
    public void updatePostingInfo(
            Long inventoryId,
            Long myUniverseTrainId,
            Long postedLocation
    ){

        PostingInfo postingInfo
                = postingInfoRepository.findByInventoryIdAndMyUniverseTrainId(inventoryId, myUniverseTrainId);

        postingInfo.setPostedLocation(postedLocation);
    }

    @Transactional
    public void increaseLikesAmt(
            Long postingInfoId
    ){

        Optional<PostingInfo> OptionalPostingInfo = postingInfoRepository.findById(postingInfoId);

        if(OptionalPostingInfo.isPresent()){
            PostingInfo postingInfo = OptionalPostingInfo.get();

            postingInfo.setLikesAmt(postingInfo.getLikesAmt() + 1);
        }
    }
}
