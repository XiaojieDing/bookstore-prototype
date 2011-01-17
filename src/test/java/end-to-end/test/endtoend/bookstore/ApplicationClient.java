package test.endtoend.bookstore;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import bookstore.Bookstore;
import bookstore.BookstoreLibrary;
import bookstore.Customer;
import bookstore.Order;

public class ApplicationClient {

	private Customer customer;
	private BookstoreLibrary repository;

	public ApplicationClient(BookstoreLibrary repository) {
		this.repository = repository;
	}

	public void orders(Order order) {
		this.customer = order.getCustomer();
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(Bookstore.class);
		factory.setAddress("http://localhost:9000/bookstore");
		Bookstore bookstore = (Bookstore) factory.create();
		bookstore.requestOrder(order);
	}

	public void hasReceivedUpdateForOpenBalance(BigDecimal newBalance) {
		assertThat("Customer's open balance", customer().getOpenBalance(), is(equalTo(newBalance)));
	}

	private Customer customer() {
		Customer aCustomer = repository.getCustomer(customer.getId());
		return aCustomer;
	}

	public void hasReceivedNotiyfication(String message) {
		assertThat("Customer notifcation", customer().getMessage(), is(equalTo(message)));
	}
}
