package com.sanapyeong.mtvs_3rd_dreamplanet.likes.services;

import com.sanapyeong.mtvs_3rd_dreamplanet.likes.repositories.LikesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikesService {

    private final LikesRepository likesRepository;

    @Autowired
    public LikesService(
            LikesRepository likesRepository
    ){
        this.likesRepository = likesRepository;
    }


    public void createLikes(Long userId, Long postingInfoId) {
    }
}
