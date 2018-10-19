package com.wa.msm.comment;

import com.wa.msm.comment.entity.Comment;
import com.wa.msm.comment.repository.CommentRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
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

		Assert.assertTrue(commentDb.isPresent());
		Assert.assertEquals(comment.getId(), commentDb.get().getId());
		Assert.assertEquals(comment.getContent(), commentDb.get().getContent());
		Assert.assertEquals(comment.getReported(), commentDb.get().getReported());
		Assert.assertEquals(comment.getUserId(), commentDb.get().getUserId());
		Assert.assertEquals(comment.getAdventureId(), commentDb.get().getAdventureId());
	}

}
