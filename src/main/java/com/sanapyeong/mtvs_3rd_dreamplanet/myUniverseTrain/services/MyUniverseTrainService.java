package com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.dto.BlockInventoryFindResponseDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.repositories.InventoryRepository;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.dto.MyUniverseTrainFindResponseDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.entities.MyUniverseTrain;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.repositories.MyUniverseTrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    public MyUniverseTrainFindResponseDTO findMyUniverseTrainByUserId(Long userId) throws IOException {

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

    @Transactional
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

    @Transactional
    public void modifyPlanetOrder(
            Long userId,
            String planetOrder
    ) {

        MyUniverseTrain myUniverseTrain = myUniverseTrainRepository.findMyUniverseTrainByUserId(userId);

        ObjectMapper objectMapper = new ObjectMapper();

        if(myUniverseTrain != null){

            if(planetOrder != null){

                List<Integer> planetOrderList;

                try{
                    planetOrderList = objectMapper.readValue(planetOrder, new TypeReference<List<Integer>>(){});
                }
                catch (Exception e){
                    throw new IllegalArgumentException("올바르지 않은 planet order 형식입니다");
                }

                myUniverseTrain.setPlanetOrder(planetOrderList);
            }

        }else{
            throw new IllegalArgumentException("해당 유저의 우주 열차가 존재하지 않습니다.");
        }
    }

    @Transactional
    public void modifyPlanetStatus(
            Long userId,
            String planetStatus) {

        MyUniverseTrain myUniverseTrain = myUniverseTrainRepository.findMyUniverseTrainByUserId(userId);

        ObjectMapper objectMapper = new ObjectMapper();

        if(myUniverseTrain != null){

            if(planetStatus != null){

                List<Boolean> planetStatusList;

                try{
                    planetStatusList = objectMapper.readValue(planetStatus, new TypeReference<List<Boolean>>(){});
                }
                catch (Exception e){
                    System.out.println("올바르지 않은 planet status 형식입니다");
                    throw new IllegalArgumentException("올바르지 않은 planet status 형식입니다");
                }

                myUniverseTrain.setPlanetStatus(planetStatusList);
            }

        }else{
            System.out.println("해당 유저의 우주 열차가 존재하지 않습니다.");
            throw new IllegalArgumentException("해당 유저의 우주 열차가 존재하지 않습니다.");
        }
    }
}
