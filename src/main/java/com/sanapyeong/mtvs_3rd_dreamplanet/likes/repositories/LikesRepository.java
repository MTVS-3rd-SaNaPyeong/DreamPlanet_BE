package com.sanapyeong.mtvs_3rd_dreamplanet.likes.repositories;

import com.sanapyeong.mtvs_3rd_dreamplanet.likes.entities.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {
}
