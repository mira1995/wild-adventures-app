package com.wa.msm.user.repository;

import com.wa.msm.user.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Integer countUserAccountByPseudo(String pseudo);
    Integer countUserAccountByEmail(String email);
}
