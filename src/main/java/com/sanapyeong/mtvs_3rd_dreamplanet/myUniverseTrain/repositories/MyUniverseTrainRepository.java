package com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.repositories;

import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.dto.MyUniverseTrainFindResponseDTO;
import com.sanapyeong.mtvs_3rd_dreamplanet.myUniverseTrain.entities.MyUniverseTrain;
import com.sanapyeong.mtvs_3rd_dreamplanet.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface MyUniverseTrainRepository extends JpaRepository<MyUniverseTrain, Long> {

//    @Query("SELECT m FROM MyUniverseTrain m WHERE m.userId = :userId")
//    List<MyUniverseTrain> findMyUniverseTrainsByUserId(Long userId);

    @Query("SELECT m FROM MyUniverseTrain m WHERE m.userId = :userId")
    List<MyUniverseTrain> findMyUniverseTrainsTemp(Long userId);

    @Query("SELECT m.id, m.userId, m.trainName, m.uniqueCode " +
            " FROM MyUniverseTrain m " +
            "WHERE m.userId = :userId")
    List<Object[]> findMyUniverseTrainsByUserId(Long userId);

    @Query("SELECT m.id, m.userId, m.trainName, m.uniqueCode " +
            "FROM MyUniverseTrain m " +
            "WHERE m.trainName like %:searchWord% OR m.uniqueCode like %:searchWord%")
    List<Object[]> findMyUniverseTrainsBySearchWord(String searchWord);

//    @Query("SELECT u FROM User u WHERE u.loginId = :loginId")
//    User findUserByLoginId(String loginId);

}
