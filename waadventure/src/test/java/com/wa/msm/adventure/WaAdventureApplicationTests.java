package com.wa.msm.adventure;

import com.wa.msm.adventure.entity.Adventure;
import com.wa.msm.adventure.repository.AdventureRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
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
		Assertions.assertTrue(adventureDb.isPresent());
		Assertions.assertEquals(adventure.getTitle(), adventureDb.get().getTitle());
		Assertions.assertEquals(adventure.getId(), adventureDb.get().getId());

	}

	@Test
	public void whenFindLast5_thenReturnAdventures(){

		//given
		Adventure adventure = new Adventure();
		adventure.setTitle("Jolie aventure");
		adventure.setDescription("Une jolie aventure");
		adventure.setLocation("Paris");
		adventure.setStatus("PAIEMENT_ATTENTE");
		entityManager.persist(adventure);
		entityManager.flush();

		Adventure adventure1 = new Adventure();
		adventure1.setTitle("Jolie aventure");
		adventure1.setDescription("Une jolie aventure");
		adventure1.setLocation("Paris");
		adventure1.setStatus("PAIEMENT_ATTENTE");
		entityManager.persist(adventure1);
		entityManager.flush();

		//when
		Iterable<Adventure> adventuresDb = adventureRepository.findTop5ByOrderByIdDesc();

		//then
		adventuresDb.iterator().forEachRemaining(adventureDb -> Assertions.assertEquals(adventure.getTitle(), adventureDb.getTitle()));
	}
}
