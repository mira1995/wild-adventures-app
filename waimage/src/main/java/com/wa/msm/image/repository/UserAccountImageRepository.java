package com.wa.msm.image.repository;

import com.wa.msm.image.entity.UserAccountImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountImageRepository extends JpaRepository<UserAccountImage, Long> {
}
