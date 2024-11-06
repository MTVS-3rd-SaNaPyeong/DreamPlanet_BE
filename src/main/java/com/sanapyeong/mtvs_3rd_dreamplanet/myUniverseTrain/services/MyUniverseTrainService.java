package com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.services;

import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.dto.BlockInventoryFindResponseDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.repositories.InventoryRepository;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.dto.MyUniverseTrainFindResponseDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.entities.MyUniverseTrain;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.repositories.MyUniverseTrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

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

    @Transactional
    public void createMyUniverseTrain(Long userId, String trainName) {

        MyUniverseTrain newMyUniverseTrain
                = new MyUniverseTrain(userId, trainName, createUniqueCode());

        myUniverseTrainRepository.save(newMyUniverseTrain);
    }

    public String createUniqueCode(){

            String uuidString = UUID.randomUUID().toString();
            byte[] uuidStringBytes = uuidString.getBytes(StandardCharsets.UTF_8);
            byte[] hashBytes;

            try{
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                hashBytes = messageDigest.digest(uuidStringBytes);
            }catch(NoSuchAlgorithmException e){
                throw new RuntimeException(e);
            }

            StringBuilder sb = new StringBuilder();
            for(int j  = 0; j < 4; j++){
                sb.append(String.format("%02x", hashBytes[j]));
            }

        return sb.toString();
    }
}
