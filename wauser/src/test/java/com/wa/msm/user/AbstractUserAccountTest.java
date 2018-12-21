package com.wa.msm.user;

import com.wa.msm.user.entity.UserAccount;
import com.wa.msm.user.repository.UserAccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;

public abstract class AbstractUserAccountTest {

/*    @Autowired
    protected TestEntityManager entityManager;*/

    protected static UserAccount userAccount;

    @Autowired
    protected UserAccountRepository userAccountRepository;

    @BeforeEach
    public void setUp(){
        userAccount = new UserAccount();
        userAccount.setProfileImageId(1L);
        userAccount.setActive(true);
        userAccount.setAddress("1 rue du Maronnier");
        Calendar cal = Calendar.getInstance();
        cal.set(2000, 0, 1);
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
        userAccount.setRole("USER");

    }

    @AfterEach
    public void afterTest(){
        userAccountRepository.delete(userAccount);
    }

    protected void persistJdd(){
        userAccountRepository.save(userAccount);
    }
}
