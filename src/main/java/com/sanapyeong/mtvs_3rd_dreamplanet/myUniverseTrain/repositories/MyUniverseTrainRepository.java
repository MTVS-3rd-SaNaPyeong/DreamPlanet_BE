package com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.repositories;

import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.dto.MyUniverseTrainFindResponseDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.entities.MyUniverseTrain;
import com.sanapyeong.mtvs_3rd_dreamplanet.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MyUniverseTrainRepository extends JpaRepository<MyUniverseTrain, Long> {

    @Query("SELECT m FROM MyUniverseTrain m WHERE m.userId = :userId")
    MyUniverseTrain findMyUniverseTrainByUserId(Long userId);

//    @Query("SELECT u FROM User u WHERE u.loginId = :loginId")
//    User findUserByLoginId(String loginId);

}
