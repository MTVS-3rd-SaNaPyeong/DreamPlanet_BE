package com.sanapyeong.mtvs_3rd_dreamplanet.likes.services;

import com.amazonaws.services.kms.model.NotFoundException;
import com.sanapyeong.mtvs_3rd_dreamplanet.inventory.entities.PostingInfo;
import com.sanapyeong.mtvs_3rd_dreamplanet.inventory.repositories.PostingInfoRepository;
import com.sanapyeong.mtvs_3rd_dreamplanet.likes.entities.Likes;
import com.sanapyeong.mtvs_3rd_dreamplanet.likes.repositories.LikesRepository;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikesService {

    private final LikesRepository likesRepository;
    private final PostingInfoRepository postingInfoRepository;

    @Autowired
    public LikesService(
            LikesRepository likesRepository,
            PostingInfoRepository postingInfoRepository
    ){
        this.likesRepository = likesRepository;
        this.postingInfoRepository = postingInfoRepository;
    }

    @Transactional
    public void createLikes(
            Long userId,
            Long postingInfoId
    ) {

        // user 존재 여부 확인 완료

        // postingInfo id를 통한 존재 여부 확인 필요
        // 해당 유저가 해당 postingInfo에 대해 좋아요를 눌렀는지 확인 필요

        Optional<PostingInfo> OptionalPostingInfo = postingInfoRepository.findById(postingInfoId);

        if (OptionalPostingInfo.isPresent()) {

            PostingInfo postingInfo = OptionalPostingInfo.get();

            Optional<Likes> OptionalLikes = likesRepository.findByUserIdAndPostingInfoId(userId, postingInfoId);

            if (OptionalLikes.isPresent()) {
                throw new IllegalArgumentException("이미 좋아요를 눌렀습니다.");
            }else{
                Likes likes = new Likes(userId, postingInfoId);
                likesRepository.save(likes);
            }

        } else{
            throw new NotFoundException("존재하지 않는 PostingInfo 입니다.");
        }
    }
}
