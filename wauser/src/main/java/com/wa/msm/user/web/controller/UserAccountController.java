package com.wa.msm.user.web.controller;

import com.sun.deploy.util.StringUtils;
import com.wa.msm.user.bean.UserAccountImageBean;
import com.wa.msm.user.entity.UserAccount;
import com.wa.msm.user.proxy.MSImageProxy;
import com.wa.msm.user.repository.UserAccountRepository;
import com.wa.msm.user.web.exception.UserAccountImageNotFoundException;
import com.wa.msm.user.web.exception.UserAccountNotFoundException;
import com.wa.msm.user.web.exception.UserAccountNotValidException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;

@Api(description = "API pour les opérations CRUD sur les comptes utilisateur")
@RestController
@RequestMapping(value = "/api")
public class UserAccountController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private MSImageProxy msImageProxy;

    @ApiOperation(value = "Récupère un compte utilisateur par son id")
    @GetMapping(value = "/{userId}")
    public Optional<UserAccount> getUserById(@PathVariable Long userId){
        log.info("Début méthode : getUserById()");
        Optional<UserAccount> userAccount = userAccountRepository.findById(userId);
        if(!userAccount.isPresent()) {
            log.error("L'utilisateur d'id "+ userId +" n'existe pas.");
            throw new UserAccountNotFoundException("L'utilisateur d'id "+ userId +" n'existe pas.");
        }
        checkIfImageExist(userAccount.get());
        log.info("Récupération de l'utilisateur d'id : "+ userId);
        return userAccount;
    }

    @ApiOperation(value = "Récupère un compte utilisateur par son email")
    @PostMapping(value = "/email")
    public Optional<UserAccount> getUserByEmail(@RequestBody String email) {
        log.info("Début méthode : getUserByEmail()");
        Optional<UserAccount> userAccount = userAccountRepository.findUserAccountByEmail(email);
        if (!userAccount.isPresent()) {
            log.error("L'utilisateur lié à l'identifiant " + email+ " n'existe pas.");
            throw new UserAccountNotFoundException("L'utilisateur lié à l'identifiant " + email+ " n'existe pas.");
        }
        checkIfImageExist(userAccount.get());
        log.info("Récupération de l'utilisateur d'email : "+ email);
        return userAccount;
    }

    @ApiOperation(value = "Créé un compte utilisateur en fonction des informations fournies par l'utilisateur")
    @PostMapping
    public ResponseEntity<UserAccount> createUserAccount(@RequestBody UserAccount userAccount){
        //Si l'image du compte n'est pas vide la sauvegarder
/*        if(userAccount.getProfileImageId() != null && userAccount.getProfileImage() == null) throw new UserAccountNotValidException("Pour lier une image à un utilisateur les informations de l'image doivent être fournie");*/
        //Si l'image n'existe pas la sauvegarder
        /*else if (userAccount.getProfileImage()!= null && userAccount.getProfileImageId() == null) {
            Long idImage = msImageProxy.createUserAccountImage(userAccount.getProfileImage()).getId();
            userAccount.setProfileImageId(idImage);
        }

        else if (userAccount.getProfileImageId()!=null && userAccount.getProfileImage()!= null && userAccount.getProfileImage().getId()== null){
            Long idImage = msImageProxy.createUserAccountImage(userAccount.getProfileImage()).getId();
            userAccount.setProfileImageId(idImage);
        }*/

        log.info("Début méthode : createUserAccount()");

        //Vérifie si le pseudo n'existe pas déjà en base
        Integer countUserWithPseudo = userAccountRepository.countUserAccountByPseudo(userAccount.getPseudo());
        if(countUserWithPseudo >0) {
            log.error("Le pseudo : "+userAccount.getPseudo()+" est déjà utilisé par un autre utilisateur");
            throw new UserAccountNotValidException("Le pseudo : "+userAccount.getPseudo()+" est déjà utilisé par un autre utilisateur");
        }

        //Vérifie si l'email n'existe pas déjà en base
        Integer countUserWithEmail = userAccountRepository.countUserAccountByEmail(userAccount.getEmail());
        if(countUserWithEmail >0) {
            log.error("L'email : "+userAccount.getEmail()+" est déjà utilisé par un autre utilisateur");
            throw new UserAccountNotValidException("L'email : "+userAccount.getEmail()+" est déjà utilisé par un autre utilisateur");
        }

        if(userAccount.getProfileImageId() != null){
            Optional<UserAccountImageBean> userAccountImageBean = msImageProxy.findById(userAccount.getProfileImageId());
            if(!userAccountImageBean.isPresent()) {
                log.error("L'image correspondant à l'utilisateur n'a pas été trouvée");
                throw new UserAccountImageNotFoundException("L'image correspondant à l'utilisateur n'a pas été trouvée");
            }
            /*else userAccount.get().setProfileImage(userAccountImageBean.get());*/
        }

        userAccount.setActive(true);
        userAccount.setRole("USER");

        //Valide tous les autres champs du compte
        checkIfImageExist(userAccount);
        validateUserAccount(userAccount);
        log.info("Création de l'utilisateur");
        return new ResponseEntity<>(userAccountRepository.save(userAccount), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Met à jour un compte utilisateur en fonction des informations fournies par l'utilisateur")
    @PatchMapping
    public ResponseEntity<UserAccount> updateUserAccount(@RequestBody UserAccount userAccount) {

        log.info("Début méthode : updateUserAccount()");

        if(userAccount.getId() != null){

            //Vérifie que le compte existe
            Optional<UserAccount> dbUserAccount = userAccountRepository.findById(userAccount.getId());
            if(!dbUserAccount.isPresent()) {
                log.error("L'utilisateur fourni n'existe pas");
                throw new UserAccountNotFoundException("L'utilisateur fourni n'existe pas");
            }

            //Vérifie si le pseudo a été modifié puis si ce dernier n'existe pas déjà en base
            Integer countUserWithPseudo = userAccountRepository.countUserAccountByPseudo(userAccount.getPseudo());
            if((!userAccount.getPseudo().equals(dbUserAccount.get().getPseudo())) && countUserWithPseudo >0) {
                log.error("Le pseudo : "+userAccount.getPseudo()+" est déjà utilisé par un autre utilisateur");
                throw new UserAccountNotValidException("Le pseudo : "+userAccount.getPseudo()+" est déjà utilisé par un autre utilisateur");
            }

            //Vérifie si l'email a été modifié puis si ce dernier n'existe pas déjà en base
            Integer countUserWithEmail = userAccountRepository.countUserAccountByEmail(userAccount.getEmail());
            if((!userAccount.getEmail().equals(dbUserAccount.get().getEmail())) && countUserWithEmail >0) {
                log.error("L'email : "+userAccount.getEmail()+" est déjà utilisé par un autre utilisateur");
                throw new UserAccountNotValidException("L'email : "+userAccount.getEmail()+" est déjà utilisé par un autre utilisateur");
            }

            //Si l'image du compte n'est pas vide vérifier si cette dernière existe en base ou non
            /* if (userAccount.getProfileImageId()!=null && userAccount.getProfileImage()!= null && userAccount.getProfileImageId().longValue()==userAccount.getProfileImage().getId().longValue()){
                msImageProxy.updateUserAccountImage(userAccount.getProfileImage());
            }
            //Si l'image n'existe pas la sauvegarder
            else if (userAccount.getProfileImageId()!=null && userAccount.getProfileImage()!= null && userAccount.getProfileImage().getId()== null){
                Long idImage = msImageProxy.createUserAccountImage(userAccount.getProfileImage()).getId();
                userAccount.setProfileImageId(idImage);
            }*/

            //Valide tous les autres champs du compte
            checkIfImageExist(userAccount);
            validateUserAccount(userAccount);
        } else {
            log.error("L'utilisateur fourni n'a pas encore été ajouté");
            throw new UserAccountNotFoundException("L'utilisateur fourni n'a pas encore été ajouté");
        }

        log.info("Mise à jour du compte utilisateur");
        return new ResponseEntity<>(userAccountRepository.save(userAccount), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Supprime un compte utilisateur en fonction l'id fournie ")
    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<String> deleteUserAccount(@PathVariable Long userId) {
        log.info("Début méthode : deleteUserAccount()");
        Optional<UserAccount> userAccount = userAccountRepository.findById(userId);
        if(!userAccount.isPresent()) {
            log.error("L'utilisateur d'id "+ userId +" n'existe pas.");
            throw new UserAccountNotFoundException("L'utilisateur d'id "+ userId +" n'existe pas.");
        }
        /*if(userAccount.get().getProfileImageId()!=null) msImageProxy.deleteUserAccountImageById(userAccount.get().getProfileImageId());*/
        userAccountRepository.deleteById(userId);
        log.info("Suppression du compte utilisateur d'id : "+ userId);
        return new ResponseEntity<>("Le compte utilisateur  d'id " + userId + " a bien été supprimé.", HttpStatus.GONE);
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

    private void checkIfImageExist(UserAccount userAccount){
        if(userAccount.getProfileImageId() != null) {
            Optional<UserAccountImageBean> userAccountImageBean = msImageProxy.findById(userAccount.getProfileImageId());
            if (!userAccountImageBean.isPresent())
                throw new UserAccountImageNotFoundException("L'image correspondant à l'utilisateur n'a pas été trouvée");
            /*else userAccount.get().setProfileImage(userAccountImageBean.get());*/
        }
    }
}
