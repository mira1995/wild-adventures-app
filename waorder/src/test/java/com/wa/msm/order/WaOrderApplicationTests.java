package com.wa.msm.order;

import com.wa.msm.order.entity.Order;
import com.wa.msm.order.repository.OrderRepository;
import com.wa.msm.order.util.enumeration.OrderStatusEnum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Calendar;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
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
		Assertions.assertTrue(orderDb.isPresent());
		Assertions.assertEquals(order.getId(), orderDb.get().getId());
		Assertions.assertEquals(order.getIsPaid(), orderDb.get().getIsPaid());
		Assertions.assertEquals(order.getOrderDate(), orderDb.get().getOrderDate());
		Assertions.assertEquals(order.getStatus(), orderDb.get().getStatus());

	}

}
