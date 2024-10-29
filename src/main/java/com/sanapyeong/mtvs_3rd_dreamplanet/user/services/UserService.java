package com.sanapyeong.mtvs_3rd_dreamplanet.user.services;

import com.sanapyeong.mtvs_3rd_dreamplanet.user.entities.User;
import com.sanapyeong.mtvs_3rd_dreamplanet.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByLoginId(String loginId) {
        return userRepository.findUserByLoginId(loginId);
    }

    public User findUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
    }

    public void registerUser(User user) {
        userRepository.save(user);
    }
}
