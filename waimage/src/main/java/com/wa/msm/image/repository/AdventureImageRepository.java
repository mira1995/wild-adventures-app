package com.wa.msm.image.repository;

import com.wa.msm.image.entity.AdventureImage;
import com.wa.msm.image.entity.AdventureImageKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdventureImageRepository extends JpaRepository<AdventureImage, AdventureImageKey> {

    List<AdventureImage> findAdventureImagesByAdventureId(Long adventureId);

}
