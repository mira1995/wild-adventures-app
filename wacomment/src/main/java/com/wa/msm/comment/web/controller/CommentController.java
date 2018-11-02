package com.wa.msm.comment.web.controller;

import com.sun.deploy.util.StringUtils;
import com.wa.msm.comment.entity.Comment;
import com.wa.msm.comment.proxy.MSAdventureProxy;
import com.wa.msm.comment.proxy.MSUserAccountProxy;
import com.wa.msm.comment.repository.CommentRepository;
import com.wa.msm.comment.web.exception.CommentNotFoundException;
import com.wa.msm.comment.web.exception.CommentNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;

@RestController
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MSAdventureProxy msAdventureProxy;

    @Autowired
    MSUserAccountProxy msUserAccountProxy;

    @GetMapping(value = "/{adventureId}")
    public List<Comment> commentList(@PathVariable Long adventureId) {
        // Vérifier que l'aventure existe
        msAdventureProxy.getAdventure(adventureId);

        List<Comment> comments = new ArrayList<>(0);
        commentRepository.findByAdventureId(adventureId).iterator().forEachRemaining(comments::add);
        if (comments.isEmpty())
            throw new CommentNotFoundException("Il n'existe aucun commentaires pour cette aventure à l'id " + adventureId + ".");
        // Supprimer si parentId pas null
        else comments.removeIf(comment -> comment.getParentId() != null);
        return comments;
    }

    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
        // Vérifier que l'aventure et l'utilisateur existent
        msAdventureProxy.getAdventure(comment.getAdventureId());
        msUserAccountProxy.getUserById(comment.getUserId());

        validateComment(comment);
        return new ResponseEntity<>(commentRepository.save(comment), HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<Comment> updateComment(@RequestBody Comment comment) {
        if (comment.getId() == null || !commentRepository.findById(comment.getId()).isPresent())
            throw new CommentNotFoundException("Le commentaire envoyé n'existe pas.");
        else {
            // Vérifier que l'aventure ainsi que l'utilisateur existent
            msAdventureProxy.getAdventure(comment.getAdventureId());
            msUserAccountProxy.getUserById(comment.getUserId());
            comment.getComments().forEach(item -> {
                msAdventureProxy.getAdventure(item.getAdventureId());
                msUserAccountProxy.getUserById(item.getUserId());
            });
        }
        validateComment(comment);
        return new ResponseEntity<>(commentRepository.save(comment), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        Optional<Comment> commentToDelete = commentRepository.findById(id);
        if (!commentToDelete.isPresent()) throw new CommentNotFoundException("Le commentaire correspondant à l'id " + id + " n'existe pas.");
        else commentRepository.deleteById(commentToDelete.get().getId());
        return new ResponseEntity<>("Le commentaire pour id " + id + " a bien été supprimé.", HttpStatus.GONE);
    }

    @DeleteMapping(value = "/adventure/{adventureId}")
    public ResponseEntity<String> deleteCommentByAdventureId(@PathVariable Long adventureId) {
        commentRepository.deleteAllByAdventureId(adventureId);
        return new ResponseEntity<>("Les commentaires pour adventureId " + adventureId + " ont bien été supprimés.", HttpStatus.GONE);
    }

    private void validateComment(Comment comment) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Comment>> constraintViolations = validator.validate(comment);

        if(constraintViolations.size() > 0) {
            List<String> violationMessages = new ArrayList<>();
            constraintViolations.iterator().forEachRemaining(constraintViolation ->
                    violationMessages.add(constraintViolation.getPropertyPath() + " : " + constraintViolation.getMessage()));

            throw new CommentNotValidException("Le commentaire n'est pas valide. " + StringUtils.join(violationMessages, " ; "));
        }
    }
}
