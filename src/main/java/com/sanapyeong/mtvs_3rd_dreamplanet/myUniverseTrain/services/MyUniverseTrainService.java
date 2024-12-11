package com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.services;

import com.amazonaws.services.kms.model.NotFoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanapyeong.mtvs_3rd_dreamplanet.inventory.dto.BlockInventoryFindResponseDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.inventory.entities.PostingInfo;
import com.sanapyeong.mtvs_3rd_dreamplanet.inventory.repositories.InventoryRepository;
import com.sanapyeong.mtvs_3rd_dreamplanet.inventory.repositories.PostingInfoRepository;
import com.sanapyeong.mtvs_3rd_dreamplanet.likes.entities.Likes;
import com.sanapyeong.mtvs_3rd_dreamplanet.likes.repositories.LikesRepository;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.dto.MyUniverseTrainFindResponseDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.dto.MyUniverseTrainSummaryFindResponseDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.entities.MyUniverseTrain;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.enums.TrainColor;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.repositories.MyUniverseTrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class MyUniverseTrainService {

    private final MyUniverseTrainRepository myUniverseTrainRepository;
    private final InventoryRepository inventoryRepository;
    private final PostingInfoRepository postingInfoRepository;
    private final LikesRepository likesRepository;

    @Autowired
    public MyUniverseTrainService(
            MyUniverseTrainRepository myUniverseTrainRepository,
            InventoryRepository inventoryRepository,
            PostingInfoRepository postingInfoRepository,
            LikesRepository likesRepository
    ){
        this.myUniverseTrainRepository = myUniverseTrainRepository;
        this.inventoryRepository = inventoryRepository;
        this.postingInfoRepository = postingInfoRepository;
        this.likesRepository = likesRepository;
    }

    public List<MyUniverseTrainSummaryFindResponseDTO> findMyUniverseTrainsByUserId(Long userId) throws IOException {

//        List<MyUniverseTrain> myUniverseTrainList
//                = myUniverseTrainRepository.findMyUniverseTrainsByUserId(userId);
//
//        List<MyUniverseTrainFindResponseDTO> findResultList = new ArrayList<>();
//
//        for(MyUniverseTrain myUniverseTrain : myUniverseTrainList){
//
//            List<BlockInventoryFindResponseDTO> blockTrainInfo
//                    = inventoryRepository.findBlockInventoryByUserIdAndMyUniverseTrainId(userId, myUniverseTrain.getId())
//                    .stream()
//                    .map(BlockInventoryFindResponseDTO::new)
//                    .toList();
//
//            MyUniverseTrainFindResponseDTO findResult
//                    = new MyUniverseTrainFindResponseDTO(
//                    myUniverseTrain.getId(),
//                    myUniverseTrain.getTrainName(),
//                    myUniverseTrain.getUniqueCode(),
//                    myUniverseTrain.getPlanetStatus(),
//                    myUniverseTrain.getPlanetOrder(),
//                    blockTrainInfo);
//
//            findResultList.add(findResult);
//        }
//
//        return findResultList;

        List<MyUniverseTrainSummaryFindResponseDTO> foundList = null;

        foundList = myUniverseTrainRepository.findMyUniverseTrainsByUserId(userId)
                .stream()
                .map(MyUniverseTrainSummaryFindResponseDTO::new)
                .toList();

        return foundList;
    }

    public MyUniverseTrainFindResponseDTO findById(Long id) throws IOException{

        Optional<MyUniverseTrain> optionalMyUniverseTrain
                = myUniverseTrainRepository.findById(id);

        MyUniverseTrain myUniverseTrain = optionalMyUniverseTrain.get();

        List<BlockInventoryFindResponseDTO> blockTrainInfo
                = inventoryRepository.findBlockInventoryByUserIdAndMyUniverseTrainId(myUniverseTrain.getUserId(), myUniverseTrain.getId())
                .stream()
                .map(BlockInventoryFindResponseDTO::new)
                .toList();

        MyUniverseTrainFindResponseDTO findResult
                = new MyUniverseTrainFindResponseDTO(
                myUniverseTrain.getId(),
                myUniverseTrain.getTrainColor(),
                myUniverseTrain.getTrainName(),
                myUniverseTrain.getUniqueCode(),
                myUniverseTrain.getPlanetStatus(),
                myUniverseTrain.getPlanetOrder(),
                blockTrainInfo
        );

        return findResult;
    }

    @Transactional
    public Long createMyUniverseTrain(Long userId, String trainName) {

        MyUniverseTrain newMyUniverseTrain
                = new MyUniverseTrain(userId, trainName, createUniqueCode());

        return myUniverseTrainRepository.save(newMyUniverseTrain).getId();
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
            String planetOrder,
            Long myUniverseTrainId
    ) {

        Optional<MyUniverseTrain> optionalMyUniverseTrain = myUniverseTrainRepository.findById(myUniverseTrainId);

        MyUniverseTrain myUniverseTrain = optionalMyUniverseTrain.get();

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
            String planetStatus,
            Long myUniverseTrainId
    ) {

        Optional<MyUniverseTrain> optionalMyUniverseTrain = myUniverseTrainRepository.findById(myUniverseTrainId);

        MyUniverseTrain myUniverseTrain = optionalMyUniverseTrain.get();

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

    @Transactional
    public void modifyMyUniverseTrainColor(Long myUniverseTrainId, Long userId, TrainColor trainColor) {

        // myUniverseTrainId로 해당 객체 검색
        Optional<MyUniverseTrain> myUniverseTrainList = myUniverseTrainRepository.findById(myUniverseTrainId);

        // 존재하는 열차인지 화인
        if(myUniverseTrainList.isPresent()){

            MyUniverseTrain myUniverseTrain = myUniverseTrainList.get();

            // 삭제하고자 하는 유저와 열차의 소유주가 동일한지 확인
            if(Objects.equals(myUniverseTrain.getUserId(), userId)){

                myUniverseTrain.setTrainColor(trainColor);

            } else{
                System.out.println("열차의 소유주와 수정하고자 하는 유저가 일치하지 않습니다");
                throw new IllegalArgumentException("열차의 소유주와 수정하고자 하는 유저가 일치하지 않습니다");
            }
        } else{ // 만약 존재하지 않는 열차 번호라면
            System.out.println("존재하지 않는 열차입니다");
            throw new NotFoundException("존재하지 않는 열차입니다");
        }
    }

    public List<MyUniverseTrainSummaryFindResponseDTO> findMyUniverseTrainsBySearchWord(String searchWord) {

        List<MyUniverseTrainSummaryFindResponseDTO> foundList = null;

        foundList = myUniverseTrainRepository.findMyUniverseTrainsBySearchWord(searchWord)
                .stream()
                .map(MyUniverseTrainSummaryFindResponseDTO::new)
                .toList();

        return foundList;
    }


    public List<MyUniverseTrainSummaryFindResponseDTO> findRandomMyUniverseTrains(Long userId) {

        List<MyUniverseTrainSummaryFindResponseDTO> foundList = null;

        foundList = myUniverseTrainRepository.findRandomMyUniverseTrains(userId)
                .stream()
                .map(MyUniverseTrainSummaryFindResponseDTO::new)
                .toList();

        return foundList;

    }

    @Transactional
    public void deleteMyUniverseTrain(Long myUniverseTrainId, Long userId) {

        // myUniverseTrainId로 해당 객체 검색
        Optional<MyUniverseTrain> myUniverseTrainList = myUniverseTrainRepository.findById(myUniverseTrainId);

        // 존재하는 열차인지 화인
        if(myUniverseTrainList.isPresent()){

            MyUniverseTrain myUniverseTrain = myUniverseTrainList.get();

            // 삭제하고자 하는 유저와 열차의 소유주가 동일한지 확인
            if(Objects.equals(myUniverseTrain.getUserId(), userId)){

                // Posting Info 조회 후 삭제
                List<PostingInfo> postingInfoList
                        = postingInfoRepository.findByMyUniverseTrainId(myUniverseTrainId);

                for(PostingInfo postingInfo : postingInfoList){

                    // 각 postingInfo에 대한 likes 내역 삭제
                    List<Likes> likesList = likesRepository.findByPostingInfoId(postingInfo.getId());

                    likesRepository.deleteAll(likesList);

                    postingInfoRepository.deleteById(postingInfo.getId());
                }

                // My Universe Train 삭제
                myUniverseTrainRepository.deleteById(myUniverseTrainId);

            } else{
                System.out.println("열차의 소유주와 삭제하고자 하는 유저가 일치하지 않습니다");
                throw new IllegalArgumentException("열차의 소유주와 삭제하고자 하는 유저가 일치하지 않습니다");
            }
        } else{ // 만약 존재하지 않는 열차 번호라면
            System.out.println("존재하지 않는 열차입니다");
            throw new NotFoundException("존재하지 않는 열차입니다");
        }
    }

}
