package com.wa.msm.image;

import com.wa.msm.image.entity.Image;
import com.wa.msm.image.entity.ImageType;
import com.wa.msm.image.repository.ImageRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class WaImageApplicationTests {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ImageRepository imageRepository;

	@Test
	public void contextLoads() {
	}

	@Test
	public void whenFindById_thenReturnImage(){
		ImageType imageType = new ImageType();
		imageType.setCode("ADV");
		imageType.setName("adventure");
		entityManager.persist(imageType);

		Image image = new Image();
		image.setAlt("toto");
		image.setDescription("titi");
		image.setUri("tata.jpeg");
		image.setType(imageType);
		entityManager.persist(image);
		entityManager.flush();

		Optional<Image> imageDb = imageRepository.findById(image.getId());

		Assertions.assertTrue(imageDb.isPresent());
		Assertions.assertEquals(image.getId(), imageDb.get().getId());
		Assertions.assertEquals(image.getAlt(), imageDb.get().getAlt());
		Assertions.assertEquals(image.getDescription(), imageDb.get().getDescription());
		Assertions.assertEquals(image.getUri(), imageDb.get().getUri());
		Assertions.assertEquals(image.getType().getCode(), imageDb.get().getType().getCode());
		Assertions.assertEquals(image.getType().getName(), imageDb.get().getType().getName());

	}

}
