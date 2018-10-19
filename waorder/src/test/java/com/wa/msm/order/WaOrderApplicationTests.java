package com.wa.msm.order;

import com.wa.msm.order.entity.Order;
import com.wa.msm.order.repository.OrderRepository;
import com.wa.msm.order.util.enumeration.OrderStatusEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class WaOrderApplicationTests {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private OrderRepository orderRepository;

	@Test
	public void contextLoads() {
	}

	@Test
	public void whenFindById_thenReturnOrder(){

		Order order = new Order();
		order.setStatus(OrderStatusEnum.NOT_PAID);
		order.setOrderDate(Calendar.getInstance());
		order.setUserAccountId(1L);
		order.setIsPaid(true);

		entityManager.persist(order);
		entityManager.flush();

		Optional<Order> orderDb = orderRepository.findById(order.getId());
		Assert.assertTrue(orderDb.isPresent());
		Assert.assertEquals(order.getId(), orderDb.get().getId());
		Assert.assertEquals(order.getIsPaid(), orderDb.get().getIsPaid());
		Assert.assertEquals(order.getOrderDate(), orderDb.get().getOrderDate());
		Assert.assertEquals(order.getStatus(), orderDb.get().getStatus());

	}

}
