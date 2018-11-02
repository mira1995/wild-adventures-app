package com.wa.msm.category;

import com.wa.msm.category.entity.Category;
import com.wa.msm.category.repository.CategoryRepository;
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
public class WaCategoryApplicationTests {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private CategoryRepository categoryRepository;


	@Test
	public void contextLoads() {
	}

	@Test
	public void whenFindById_thenReturnCategory(){
		Category category = new Category();
		category.setTitle("La super catégorie");
		category.setDescription("Description de la super catégorie");
		entityManager.persist(category);
		entityManager.flush();

		Optional<Category> categoryDb = categoryRepository.findById(category.getId());

		Assert.assertTrue(categoryDb.isPresent());
		Assert.assertEquals(category.getId(), categoryDb.get().getId());
		Assert.assertEquals(category.getTitle(), categoryDb.get().getTitle());
		Assert.assertEquals(category.getDescription(), categoryDb.get().getDescription());

	}

}
