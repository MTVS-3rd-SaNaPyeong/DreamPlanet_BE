package com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.services;

import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.dto.BlockInventoryFindResponseDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.repositories.InventoryRepository;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.dto.MyUniverseTrainFindResponseDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.entities.MyUniverseTrain;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.repositories.MyUniverseTrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyUniverseTrainService {

    private final MyUniverseTrainRepository myUniverseTrainRepository;
    private final InventoryRepository inventoryRepository;

    @Autowired
    public MyUniverseTrainService(
            MyUniverseTrainRepository myUniverseTrainRepository,
            InventoryRepository inventoryRepository
    ){
        this.myUniverseTrainRepository = myUniverseTrainRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public MyUniverseTrainFindResponseDTO findMyUniverseTrainByUserId(Long userId) {

        MyUniverseTrain myUniverseTrain
                = myUniverseTrainRepository.findMyUniverseTrainByUserId(userId);

        List<BlockInventoryFindResponseDTO> blockTrainInfo
                = inventoryRepository.findBlockInventoryByUserId(userId)
                .stream()
                .map(BlockInventoryFindResponseDTO::new)
                .toList();

        MyUniverseTrainFindResponseDTO findResult
                = new MyUniverseTrainFindResponseDTO(
                        myUniverseTrain.getPlanetStatus(),
                        myUniverseTrain.getPlanetOrder(),
                        blockTrainInfo
        );

        return findResult;
    }
}
