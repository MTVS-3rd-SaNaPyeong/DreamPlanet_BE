package com.sanapyeong.mtvs_3rd_dreamplanet.likes.repositories;

import com.sanapyeong.mtvs_3rd_dreamplanet.likes.entities.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Query("SELECT l " +
            "FROM Likes l " +
            "WHERE l.userId = :userId AND l.postingInfoId = :postingInfoId")
    Optional<Likes> findByUserIdAndPostingInfoId(Long userId, Long postingInfoId);

    @Query("SELECT l " +
            "FROM Likes l " +
            "WHERE l.postingInfoId = :postingInfoId")
    List<Likes> findByPostingInfoId(Long postingInfoId);
}
