package test.integration.jaxwsjaxb.bookstore.services;
import java.util.Date;

import bookstore.Order;

public class OrderTestImpl implements OrderTest {

	@Override
	public Order testOrder(Order order) {
		// @formatter:off
		Order result = new Order(
			order.getId() + " New",
			new Date(),
			order.getCustomer()
		);
		// @formatter:on
		return result;
	}

}
