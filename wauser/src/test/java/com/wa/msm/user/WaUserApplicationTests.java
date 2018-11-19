package com.wa.msm.user;

import com.wa.msm.user.entity.UserAccount;
import com.wa.msm.user.proxy.MSImageProxy;
import com.wa.msm.user.repository.UserAccountRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class WaUserApplicationTests extends AbstractUserAccountTest {


	@Test
	public void whenFindById_thenReturnUserAccount(){
		persistJdd();
		Optional<UserAccount> userAccountDb = userAccountRepository.findById(userAccount.getId());
		Assert.assertTrue(userAccountDb.isPresent());
		Assert.assertEquals(userAccount.getId(), userAccountDb.get().getId());
	}


}
