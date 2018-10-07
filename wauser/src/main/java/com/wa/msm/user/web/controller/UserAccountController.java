package com.wa.msm.user.web.controller;

import com.sun.deploy.util.StringUtils;
import com.wa.msm.user.entity.UserAccount;
import com.wa.msm.user.repository.UserAccountRepository;
import com.wa.msm.user.web.exceptions.UserAccountNotFoundException;
import com.wa.msm.user.web.exceptions.UserAccountNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;

@RestController
public class UserAccountController {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @GetMapping(value = "/user/{userId}")
    public Optional<UserAccount> getUserById(@PathVariable Long userId){
        Optional<UserAccount> userAccount = userAccountRepository.findById(userId);
        if(!userAccount.isPresent()) throw new UserAccountNotFoundException("L'utilisateur d'id "+ userId +" n'existe pas.");
        return userAccount;
    }

    @PostMapping(value="/user")
    public UserAccount createUserAccount(@RequestBody UserAccount userAccount){
        //TODO Si l'image du compte n'est pas vide vérifier si cette dernière existe en base ou non
        //TODO Si l'image n'existe pas la sauvegarder

        //Vérifie si le pseudo n'existe pas déjà en base
        Integer countUserWithPseudo = userAccountRepository.countUserAccountByPseudo(userAccount.getPseudo());
        if(countUserWithPseudo >0) throw new UserAccountNotValidException("Le pseudo : "+userAccount.getPseudo()+" est déjà utilisé par un autre utilisateur");

        //Vérifie si l'email n'existe pas déjà en base
        Integer countUserWithEmail = userAccountRepository.countUserAccountByEmail(userAccount.getEmail());
        if(countUserWithEmail >0) throw new UserAccountNotValidException("L'email : "+userAccount.getEmail()+" est déjà utilisé par un autre utilisateur");

        //Valide tous les autres champs du compte
        validateUserAccount(userAccount);

        return userAccountRepository.save(userAccount);
    }

    @PatchMapping(value = "/user")
    public UserAccount updateUserAccount(@RequestBody UserAccount userAccount) {

        if(userAccount.getId() != null){
            //Vérifie que le compte existe
            Optional<UserAccount> dbUserAccount = userAccountRepository.findById(userAccount.getId());
            if(!dbUserAccount.isPresent()) throw new UserAccountNotFoundException("L'utilisateur fourni n'existe pas");

            //Vérifie si le pseudo a été modifié puis si ce dernier n'existe pas déjà en base
            Integer countUserWithPseudo = userAccountRepository.countUserAccountByPseudo(userAccount.getPseudo());
            if((!userAccount.getPseudo().equals(dbUserAccount.get().getPseudo())) && countUserWithPseudo >0) throw new UserAccountNotValidException("Le pseudo : "+userAccount.getPseudo()+" est déjà utilisé par un autre utilisateur");

            //Vérifie si l'email a été modifié puis si ce dernier n'existe pas déjà en base
            Integer countUserWithEmail = userAccountRepository.countUserAccountByEmail(userAccount.getEmail());
            if((!userAccount.getEmail().equals(dbUserAccount.get().getEmail())) && countUserWithEmail >0) throw new UserAccountNotValidException("L'email : "+userAccount.getEmail()+" est déjà utilisé par un autre utilisateur");

            //Valide tous les autres champs du compte
            validateUserAccount(userAccount);
        }else throw new UserAccountNotFoundException("L'utilisateur fourni n'a pas encore été ajouté");
        //TODO Si l'image du compte n'est pas vide vérifier si cette dernière existe en base ou non
        //TODO Si l'image n'existe pas la sauvegarder

        return userAccountRepository.save(userAccount);
    }

    @DeleteMapping(value = "/user/{userId}")
    public String deleteUserAccount(@PathVariable Long userId){
        //TODO Gérer la suppression de l'image de profil si cette dernière existe
        Optional<UserAccount> userAccount = userAccountRepository.findById(userId);
        if(!userAccount.isPresent()) throw new UserAccountNotFoundException("L'utilisateur d'id "+ userId +" n'existe pas.");
        else userAccountRepository.deleteById(userId);
        return  "Le compte utilisateur  d'id " + userId + " a bien été supprimé.";
    }

    private void validateUserAccount(UserAccount userAccount){
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<UserAccount>> constraintViolations = validator.validate(userAccount);

        if(constraintViolations.size() > 0){
            List<String> violationMessages = new ArrayList<String>();
            constraintViolations.iterator().forEachRemaining(
                    constraintViolation -> violationMessages.add(constraintViolation.getPropertyPath()+ " : "+constraintViolation.getMessage())
            );

            throw new UserAccountNotValidException("Le compte utilisateur n'est pas valide : "+ StringUtils.join(violationMessages, "\n"));
        }
    }

}
