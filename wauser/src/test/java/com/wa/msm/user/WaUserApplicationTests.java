package com.wa.msm.user;

import com.wa.msm.user.entity.UserAccount;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class WaUserApplicationTests extends AbstractUserAccountTest {


	@Test
	public void whenFindById_thenReturnUserAccount(){
		persistJdd();
		Optional<UserAccount> userAccountDb = userAccountRepository.findById(userAccount.getId());
		Assertions.assertTrue(userAccountDb.isPresent());
		Assertions.assertEquals(userAccount.getId(), userAccountDb.get().getId());
	}


}
