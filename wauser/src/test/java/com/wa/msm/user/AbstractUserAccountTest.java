package com.wa.msm.user;

import com.wa.msm.user.entity.UserAccount;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Calendar;

public abstract class AbstractUserAccountTest {

/*    @Autowired
    protected TestEntityManager entityManager;*/

    protected static UserAccount userAccount;

    @BeforeClass
    public static void setUp(){
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

    }
}
