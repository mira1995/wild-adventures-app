package com.wa.msm.category;

import com.wa.msm.category.entity.Category;
import com.wa.msm.category.repository.CategoryRepository;
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

		Assertions.assertTrue(categoryDb.isPresent());
		Assertions.assertEquals(category.getId(), categoryDb.get().getId());
		Assertions.assertEquals(category.getTitle(), categoryDb.get().getTitle());
		Assertions.assertEquals(category.getDescription(), categoryDb.get().getDescription());

	}

}
