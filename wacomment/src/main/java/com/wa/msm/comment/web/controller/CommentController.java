package com.wa.msm.comment.web.controller;

import com.wa.msm.comment.entity.Comment;
import com.wa.msm.comment.proxy.MSAdventureProxy;
import com.wa.msm.comment.proxy.MSUserAccountProxy;
import com.wa.msm.comment.repository.CommentRepository;
import com.wa.msm.comment.web.exception.CommentNotFoundException;
import com.wa.msm.comment.web.exception.CommentNotValidException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;

@Api(description = "API pour les opérations CRUD sur les commentaires")
@RestController
public class CommentController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MSAdventureProxy msAdventureProxy;

    @Autowired
    MSUserAccountProxy msUserAccountProxy;

    @ApiOperation(value = "Récupère la liste des commentaires liés à une aventure, s'il y en existe au moins un.")
    @GetMapping(value = "/getAll/{adventureId}")
    public List<Comment> commentList(@PathVariable Long adventureId) {
        log.info("Tentative de récupération de la liste des commentaires liés à une aventure");

        log.info("Vérifier si la catégorie existe");
        msAdventureProxy.getAdventure(adventureId);

        List<Comment> comments = new ArrayList<>(0);
        commentRepository.findByAdventureId(adventureId).iterator().forEachRemaining(comments::add);
        if (comments.isEmpty()) {
            String message = "Il n'existe aucun commentaires pour cette aventure à l'id " + adventureId + ".";
            log.error(message);
            throw new CommentNotFoundException(message);
        }
        else {
            log.info("Supprimer si parentId pas null");
            comments.removeIf(comment -> comment.getParentId() != null);
        }

        log.info("Liste des commentaires récupérée");
        return comments;
    }

    @ApiOperation(value = "Avec ce truc, tu peux ajouter un commentaire si t'es connecté. Cool hein ?")
    @PostMapping(value = "/add")
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
        log.info("Tentative de création du commentaire");

        log.info("Vérifier que l'aventure et l'utilisateur existent");
        if (!msAdventureProxy.getAdventure(comment.getAdventureId()).isPresent()) {
            String message = "L'aventure de ce commentaire n'existe pas";
            log.error(message);
            throw new CommentNotValidException(message);
        }
        if (!msUserAccountProxy.getUserById(comment.getUserId()).isPresent()) {
            String message = "L'utilisateur ayant écrit ce comentaire n'existe pas";
            log.error(message);
            throw new CommentNotValidException(message);
        }

        validateComment(comment);

        log.info("Commentaire créé");
        return new ResponseEntity<>(commentRepository.save(comment), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Si tu veux mettre à jour un comentaire en étant connecté, c'est ici.")
    @PatchMapping(value = "/update")
    public ResponseEntity<Comment> updateComment(@RequestBody Comment comment) {
        log.info("Tentative de mise à jour du commentaire");

        if (comment.getId() == null || !commentRepository.findById(comment.getId()).isPresent()) {
            String message = "Le commentaire envoyé n'existe pas";
            log.error(message);
            throw new CommentNotFoundException(message);
        }
        else {
            log.info("Vérifier que l'aventure ainsi que l'utilisateur existent");
            if(!msAdventureProxy.getAdventure(comment.getAdventureId()).isPresent()) {
                String message = "L'aventure de ce commentaire n'existe pas.";
                log.error(message);
                throw new CommentNotValidException(message);
            }
            if(!msUserAccountProxy.getUserById(comment.getUserId()).isPresent()) {
                String message = "L'utilisateur ayant écrit ce comentaire n'existe pas.";
                log.error(message);
                throw new CommentNotValidException(message);
            }
            comment.getComments().forEach(item -> {
                if(!msAdventureProxy.getAdventure(item.getAdventureId()).isPresent()) {
                    String message = "L'aventure de ce commentaire n'existe pas.";
                    log.error(message);
                    throw new CommentNotValidException(message);
                }
                if(!msUserAccountProxy.getUserById(item.getUserId()).isPresent()) {
                    String message = "L'utilisateur ayant écrit ce comentaire n'existe pas.";
                    log.error(message);
                    throw new CommentNotValidException(message);
                }
            });
        }
        validateComment(comment);

        log.info("Commentaire mis à jour");
        return new ResponseEntity<>(commentRepository.save(comment), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Si tu veux supprimer l'existence d'un commentaire, c'est à tes risques et périls")
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        log.info("Tentative de suppression du commentaire");

        Optional<Comment> commentToDelete = commentRepository.findById(id);
        //FIX ME Ajouter règle de gestion utilisateur doit avoir écrit commentaire ou est Admin.
        if (!commentToDelete.isPresent()) {
            String message = "Le commentaire correspondant à l'id " + id + " n'existe pas";
            log.error(message);
            throw new CommentNotFoundException(message);
        }
        else commentRepository.deleteById(commentToDelete.get().getId());

        String message = "Le commentaire pour id " + id + " a bien été supprimé.";
        log.info(message);
        return new ResponseEntity<>(message, HttpStatus.GONE);
    }

    @ApiOperation(value = "Si tu veux supprimer les commentaires d'une aventure, c'est à tes risques et périls. En tant qu'admin tu le sais, non ?")
    @DeleteMapping(value = "/admin/adventure/{adventureId}")
    public ResponseEntity<String> deleteCommentByAdventureId(@PathVariable Long adventureId) {
        log.info("Tentative de suppression des commentaires liés à une aventure");

        commentRepository.deleteAllByAdventureId(adventureId);

        String message = "Les commentaires pour adventureId " + adventureId + " ont bien été supprimés.";
        log.info(message);
        return new ResponseEntity<>(message, HttpStatus.GONE);
    }

    private void validateComment(Comment comment) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Comment>> constraintViolations = validator.validate(comment);

        if(constraintViolations.size() > 0) {
            List<String> violationMessages = new ArrayList<>();
            constraintViolations.iterator().forEachRemaining(constraintViolation ->
                    violationMessages.add(constraintViolation.getPropertyPath() + " : " + constraintViolation.getMessage()));

            String message = "Le commentaire n'est pas valide. " + StringUtils.join(violationMessages, " ; ");
            log.error(message);
            throw new CommentNotValidException(message);
        }

        log.info("Le commentaire est valide");
    }
}
