package com.wa.msm.comment;

import com.wa.msm.comment.entity.Comment;
import com.wa.msm.comment.repository.CommentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class WaCommentApplicationTests {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private CommentRepository commentRepository;

	@Test
	public void contextLoads() {
	}

	@Test
	public void whenFindById_thenReturnComment(){
		Comment comment = new Comment();

		comment.setContent("Hello world !");
		comment.setReported(true);
		comment.setUserId(1L);
		comment.setAdventureId(1L);

		entityManager.persist(comment);
		entityManager.flush();

		Optional<Comment> commentDb = commentRepository.findById(comment.getId());

		Assertions.assertTrue(commentDb.isPresent());
		Assertions.assertEquals(comment.getId(), commentDb.get().getId());
		Assertions.assertEquals(comment.getContent(), commentDb.get().getContent());
		Assertions.assertEquals(comment.getReported(), commentDb.get().getReported());
		Assertions.assertEquals(comment.getUserId(), commentDb.get().getUserId());
		Assertions.assertEquals(comment.getAdventureId(), commentDb.get().getAdventureId());
	}

}
