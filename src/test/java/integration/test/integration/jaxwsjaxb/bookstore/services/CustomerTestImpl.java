package test.integration.jaxwsjaxb.bookstore.services;

import bookstore.Customer;

public class CustomerTestImpl implements CustomerTest {

	@Override
	public Customer testCustomer(Customer customer) {
		// @formatter:off
		Customer result = new Customer(
			customer.getId() + " New",
			customer.getName() + " New"
		);
		// @formatter:on
		result.addAddress(customer.getAddresses().get(0));
		result.addOrder(customer.getOrders().get(0));
		return result;
	}

}
