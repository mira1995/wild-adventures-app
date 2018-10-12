package com.wa.msm.comment.repository;

import com.wa.msm.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Iterable<Comment> findByAdventureId(Long id);
    void deleteAllByAdventureId(Long adventureId);
}
