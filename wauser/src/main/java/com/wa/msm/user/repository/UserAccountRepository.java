package com.wa.msm.user.repository;

import com.wa.msm.user.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findUserAccountByEmail(String email);
    Integer countUserAccountByPseudo(String pseudo);
    Integer countUserAccountByEmail(String email);
}
