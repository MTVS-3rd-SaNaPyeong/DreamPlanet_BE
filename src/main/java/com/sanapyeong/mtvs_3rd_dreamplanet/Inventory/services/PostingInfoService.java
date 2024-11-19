package com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.services;

import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.entities.PostingInfo;
import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.repositories.PostingInfoRepository;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.entities.MyUniverseTrain;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.repositories.MyUniverseTrainRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostingInfoService {

    private final PostingInfoRepository postingInfoRepository;
    private final MyUniverseTrainRepository myUniverseTrainRepository;

    public PostingInfoService(
            PostingInfoRepository postingInfoRepository,
            MyUniverseTrainRepository myUniverseTrainRepository) {
        this.postingInfoRepository = postingInfoRepository;
        this.myUniverseTrainRepository = myUniverseTrainRepository;
    }

    @Transactional
    public void savePosingInfoByInventory(Long userId, Long inventoryId) {

        List<MyUniverseTrain> myUniverseTrainList = myUniverseTrainRepository.findMyUniverseTrainsByUserId(userId);

        for(MyUniverseTrain myUniverseTrain : myUniverseTrainList) {
            PostingInfo postingInfo = new PostingInfo(inventoryId, myUniverseTrain.getId());

            postingInfoRepository.save(postingInfo);
        }
    }

    @Transactional
    public void savePostingInfoByMyUniverseTrain(Long userId, Long myUniverseTrainId) {


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
}
