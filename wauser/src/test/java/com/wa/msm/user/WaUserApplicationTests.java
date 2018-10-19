package com.wa.msm.user;

import com.wa.msm.user.entity.UserAccount;
import com.wa.msm.user.repository.UserAccountRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class WaUserApplicationTests {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Test
	public void contextLoads() {
	}

	@Test
	public void whenFindById_thenReturnUserAccount(){
		UserAccount userAccount = new UserAccount();
		userAccount.setProfileImageId(1L);
		userAccount.setActive(true);
		userAccount.setAddress("1 rue du Maronnier");
		userAccount.setBirthDate(Calendar.getInstance());
		userAccount.setCity("Paris");
		userAccount.setCountry("France");
		userAccount.setEmail("test@gmail.com");
		userAccount.setFirstname("Toto");
		userAccount.setLastname("tata");
		userAccount.setPhoneNumber("0123456789");
		userAccount.setPassword("tititata");
		userAccount.setPseudo("test");
		userAccount.setPostalCode(75000);

		entityManager.persist(userAccount);
		entityManager.flush();

		Optional<UserAccount> userAccountDb = userAccountRepository.findById(userAccount.getId());
		Assert.assertTrue(userAccountDb.isPresent());
		Assert.assertEquals(userAccount.getId(), userAccountDb.get().getId());
	}

}
