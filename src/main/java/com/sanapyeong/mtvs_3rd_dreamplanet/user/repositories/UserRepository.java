package com.sanapyeong.mtvs_3rd_dreamplanet.user.repositories;

import com.sanapyeong.mtvs_3rd_dreamplanet.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
