package com.wa.msm.image;

import com.wa.msm.image.entity.Image;
import com.wa.msm.image.entity.ImageType;
import com.wa.msm.image.repository.ImageRepository;
import com.wa.msm.image.util.enumeration.ImageTypeEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
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

		Assert.assertTrue(imageDb.isPresent());
		Assert.assertEquals(image.getId(), imageDb.get().getId());
		Assert.assertEquals(image.getAlt(), imageDb.get().getAlt());
		Assert.assertEquals(image.getDescription(), imageDb.get().getDescription());
		Assert.assertEquals(image.getUri(), imageDb.get().getUri());
		Assert.assertEquals(image.getType().getCode(), imageDb.get().getType().getCode());
		Assert.assertEquals(image.getType().getName(), imageDb.get().getType().getName());

	}

}
