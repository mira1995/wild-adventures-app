package com.wa.msm.comment.web.controller;

import com.wa.msm.comment.entity.Comment;
import com.wa.msm.comment.repository.CommentRepository;
import com.wa.msm.comment.web.exception.CommentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

    @GetMapping(value = "/comments/{adventureId}")
    public List<Comment> commentList(@PathVariable Long adventureId) {
        List<Comment> comments = new ArrayList<>(0);
        commentRepository.findByAdventureId(adventureId).iterator().forEachRemaining(comments::add);
        if (comments.isEmpty()) throw new CommentNotFoundException("Il n'existe aucun commentaires pour cette aventure à l'id " + adventureId + ".");
        return comments;
    }

    @PostMapping(value = "/comment")
    public Comment addComment(@RequestBody Comment comment) {
        // TODO : Vérifier que l'aventure existe
        // TODO : Vérifier que l'utilisateur existe
        return commentRepository.save(comment);
    }

    @PatchMapping(value = "/comment")
    public Comment updateComment(@RequestBody Comment comment) {
        // TODO : Vérifier que l'aventure existe
        // TODO : Vérifier que l'utilisateur existe
        if (comment == null) throw new CommentNotFoundException("Le commentaire envoyé n'existe pas.");
        return commentRepository.save(comment);
    }

    @DeleteMapping(value = "/comment/{id}")
    public String deleteComment(@PathVariable Long id) {
        Optional<Comment> commentToDelete = commentRepository.findById(id);
        if (!commentToDelete.isPresent()) throw new CommentNotFoundException("Le commentaire correspondant à l'id " + id + " n'existe pas.");
        else commentRepository.deleteById(commentToDelete.get().getId());
        return "Le commentaire pour id " + id + " a bien été supprimé.";
    }
}
