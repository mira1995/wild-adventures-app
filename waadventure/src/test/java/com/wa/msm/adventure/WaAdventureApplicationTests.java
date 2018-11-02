package com.wa.msm.adventure;

import com.wa.msm.adventure.entity.Adventure;
import com.wa.msm.adventure.repository.AdventureRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class WaAdventureApplicationTests {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private AdventureRepository adventureRepository;

	@Test
	public void contextLoads() {
	}

	@Test
	public void whenFindById_thenReturnAdventure(){
		//given
		Adventure adventure = new Adventure();
		adventure.setTitle("Jolie aventure");
		adventure.setDescription("Une jolie aventure");
		adventure.setLocation("Paris");
		adventure.setStatus("PAIEMENT_ATTENTE");
		entityManager.persist(adventure);
		entityManager.flush();

		//when
		Optional<Adventure> adventureDb = adventureRepository.findById(adventure.getId());

		//then
		Assert.assertTrue(adventureDb.isPresent());
		Assert.assertEquals(adventure.getTitle(), adventureDb.get().getTitle());
		Assert.assertEquals(adventure.getId(), adventureDb.get().getId());

	}
}
