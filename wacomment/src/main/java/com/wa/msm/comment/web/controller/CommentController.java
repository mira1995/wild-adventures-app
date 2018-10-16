package com.wa.msm.comment.web.controller;

import com.wa.msm.comment.entity.Comment;
import com.wa.msm.comment.proxy.MSAdventureProxy;
import com.wa.msm.comment.proxy.MSUserAccountProxy;
import com.wa.msm.comment.repository.CommentRepository;
import com.wa.msm.comment.web.exception.CommentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MSAdventureProxy msAdventureProxy;

    @Autowired
    MSUserAccountProxy msUserAccountProxy;

    @GetMapping(value = "/comments/{adventureId}")
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

    @PostMapping(value = "/comment")
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
        // Vérifier que l'aventure existe
        msAdventureProxy.getAdventure(comment.getAdventureId());

        // Vérifier que l'utilisateur existe
        msUserAccountProxy.getUserById(comment.getUserId());

        return new ResponseEntity<>(commentRepository.save(comment), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/comment")
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
        return new ResponseEntity<>(commentRepository.save(comment), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/comment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        Optional<Comment> commentToDelete = commentRepository.findById(id);
        if (!commentToDelete.isPresent()) throw new CommentNotFoundException("Le commentaire correspondant à l'id " + id + " n'existe pas.");
        else commentRepository.deleteById(commentToDelete.get().getId());
        return new ResponseEntity<>("Le commentaire pour id " + id + " a bien été supprimé.", HttpStatus.GONE);
    }

    @DeleteMapping(value = "/comment/adventure/{adventureId}")
    public ResponseEntity<String> deleteCommentByAdventureId(@PathVariable Long adventureId) {
        commentRepository.deleteAllByAdventureId(adventureId);
        return new ResponseEntity<>("Les commentaires pour adventureId " + adventureId + " ont bien été supprimés.", HttpStatus.GONE);
    }
}
