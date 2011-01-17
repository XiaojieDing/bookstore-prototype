package test.integration.jaxwsjaxb.bookstore;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static test.endtoend.bookstore.builder.AddressBuilder.anAddress;
import static test.endtoend.bookstore.builder.CustomerBuilder.CUSTOMER_ID;
import static test.endtoend.bookstore.builder.CustomerBuilder.CUSTOMER_NAME;
import static test.endtoend.bookstore.builder.CustomerBuilder.aCustomer;
import static test.endtoend.bookstore.builder.OrderBuilder.anOrder;
import static test.integration.jaxwsjaxb.bookstore.Foo.HOST;

import javax.xml.ws.Endpoint;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import test.integration.jaxwsjaxb.bookstore.services.CustomerTest;
import test.integration.jaxwsjaxb.bookstore.services.CustomerTestImpl;
import bookstore.Customer;

public class CustomerJaxWSJaxBTest {

	private static final String NEW = " New";

	private static final String SERVICE_ENDPOINT = HOST + "customer";

	private CustomerTest customerService;

	@BeforeClass
	public static void publishCustomerTestEndpoint() {
		Endpoint.publish(SERVICE_ENDPOINT, new CustomerTestImpl());
	}

	@Before
	public void createCustomerServiceClient() {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(CustomerTest.class);
		factory.setAddress(SERVICE_ENDPOINT);
		customerService = (CustomerTest) factory.create();
	}

	@Test
	public void checkIfCustomerJaxBAnnotationsAreValid() {
		// @formatter:off
		Customer aCustomer = aCustomer()
			.withAddress(anAddress().build())
			.withOrder(anOrder().build())
			.build();
		// @formatter:on

		Customer result = customerService.testCustomer(aCustomer);

		assertThat("Customer Id", result.getId(), equalTo(CUSTOMER_ID + NEW));
		assertThat("Customer name", result.getName(), equalTo(CUSTOMER_NAME + NEW));
		assertThat("Customer address", result.getAddresses().get(0), notNullValue());
		assertThat("Customer order", result.getOrders().get(0), notNullValue());
	}
}
