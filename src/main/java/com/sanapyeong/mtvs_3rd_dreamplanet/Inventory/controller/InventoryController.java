package com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.controller;

import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.dto.BlockInventoryFindResponseDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.dto.PostedLocationUpdateRequestDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.dto.SaveInventoryDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.entities.Inventory;
import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.repositories.InventoryRepository;
import com.sanapyeong.mtvs_3rd_dreamplanet.Inventory.services.InventoryService;
import com.sanapyeong.mtvs_3rd_dreamplanet.ResponseMessage;
import com.sanapyeong.mtvs_3rd_dreamplanet.component.UserTokenStorage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Tag(name="Inventory Controller", description = "Inventory Controller")
@RestController
@RequestMapping("/v1/dev")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class InventoryController {

    private final InventoryService inventoryService;
    private final UserTokenStorage userTokenStorage;
    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryController(
            InventoryService inventoryService,
            UserTokenStorage userTokenStorage,
            InventoryRepository inventoryRepository) {
        this.inventoryService = inventoryService;
        this.userTokenStorage = userTokenStorage;
        this.inventoryRepository = inventoryRepository;
    }

    @GetMapping("/inventories/block-inventory")
    @Operation(summary = "유저의 블록 행성 인벤토리 조회", description = "유저의 블록 행성 인벤토리 조회 API")
    public ResponseEntity<?> findBlockInventoryByUserToken(
            HttpServletRequest request
    ){
        // Response Message 기본 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        // 헤더에서 토큰 추출
        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authorizationHeader);

        // User Token Storage에서 해당 토큰에 맞는 유저 식별
        Long userId = userTokenStorage.getToken(authorizationHeader);
        // 만약 입력된 토큰에 해당하는 유저가 없다면
        if (userId == null) {
            ResponseMessage responseMessage = new ResponseMessage(401, "사용자 없음", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.UNAUTHORIZED);
        }

        List<BlockInventoryFindResponseDTO> blockInventoryList = findBlockInventoryByUserId(userId);

        responseMap.put("blockInventory", blockInventoryList);

        ResponseMessage responseMessage = new ResponseMessage(200, "조회 성공", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    // user id로 블록 행성 인벤토리 조회
    // 나만의 우주열차 입장 시, 게시 작품 정보 전달용
    //@GetMapping("/inventories")
    public List<BlockInventoryFindResponseDTO> findBlockInventoryByUserId(
            Long userId
    ){

        List<BlockInventoryFindResponseDTO> blockInventoryList;

        try {
            blockInventoryList= inventoryService.findBlockInventoryByUserId(userId);
        } catch (Exception e) {
            return null;
        }

        // playedPlanetId, postedLocation, completedWork
        return blockInventoryList;
    }

    @PostMapping("/inventories")
    @Operation(summary = "행성 플레이 작품 저장", description = "행성 플레이 후, 작품 저장 API")
    public ResponseEntity<?> saveInventory(
            @RequestBody SaveInventoryDTO storedStatusInfo,
            HttpServletRequest request
    ){
        // Response Message 기본 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        // 헤더에서 토큰 추출
        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authorizationHeader);

        // User Token Storage에서 해당 토큰에 맞는 유저 식별
        Long userId = userTokenStorage.getToken(authorizationHeader);
        // 만약 입력된 토큰에 해당하는 유저가 없다면
        if (userId == null) {
            ResponseMessage responseMessage = new ResponseMessage(401, "사용자 없음", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.UNAUTHORIZED);
        }

        try {
            // 인벤토리에 저장
            inventoryService.saveInventory(userId, storedStatusInfo);

            // 정상적으로 저장되었을 경우
            ResponseMessage responseMessage = new ResponseMessage(201, "인벤토리 저장 성공", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.CREATED);
        } catch (Exception e) {
            // 이미 인벤토리에 작품이 저장되어 있는 경우
            ResponseMessage responseMessage = new ResponseMessage(409, "Error: 이미 저장된 작품입니다.", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/inventories")
    public ResponseEntity<?> updatePostedLocation(
            @RequestBody PostedLocationUpdateRequestDTO postedLocationInfo,
            HttpServletRequest request
    ){
        // Response Message 기본 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        // 헤더에서 토큰 추출
        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authorizationHeader);

        // User Token Storage에서 해당 토큰에 맞는 유저 식별
        Long userId = userTokenStorage.getToken(authorizationHeader);
        // 만약 입력된 토큰에 해당하는 유저가 없다면
        if (userId == null) {
            ResponseMessage responseMessage = new ResponseMessage(401, "사용자 없음", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.UNAUTHORIZED);
        }

        // 변경 전 작품 ID와 변경하려는 작품 ID가 같은 경우
        if(Objects.equals(postedLocationInfo.getPrevId(), postedLocationInfo.getNextId())){
            ResponseMessage responseMessage = new ResponseMessage(200, "변경 내용 없음", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
        }else if(postedLocationInfo.getPrevId() == 0L){
            //변경하려는 작품의 위치가 비어있는 경우

            Inventory inventory =
                    inventoryService.findInventoryById(postedLocationInfo.getNextId());

            inventoryService.updatePostedLocation(inventory.getId(), postedLocationInfo.getPostedLocation());

        }else{
            // 변경하려는 작품의 위치가 차있고, 이전 작품과 이후 작품이 다른 경우

            Inventory prevInventory =
                    inventoryService.findInventoryById(postedLocationInfo.getPrevId());

            Inventory nextInventory =
                    inventoryService.findInventoryById(postedLocationInfo.getNextId());

            inventoryService.updatePostedLocation(prevInventory.getId(), 0L);

            inventoryService.updatePostedLocation(nextInventory.getId(), postedLocationInfo.getPostedLocation());
        }

        ResponseMessage responseMessage = new ResponseMessage(200, "변경 완료", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }
}
