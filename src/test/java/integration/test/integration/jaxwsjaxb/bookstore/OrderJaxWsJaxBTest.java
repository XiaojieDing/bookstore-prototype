package test.integration.jaxwsjaxb.bookstore;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static test.endtoend.bookstore.builder.OrderBuilder.ORDER_ID;
import static test.endtoend.bookstore.builder.OrderBuilder.anOrder;
import static test.integration.jaxwsjaxb.bookstore.Foo.HOST;

import javax.xml.ws.Endpoint;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import test.integration.jaxwsjaxb.bookstore.services.OrderTest;
import test.integration.jaxwsjaxb.bookstore.services.OrderTestImpl;
import bookstore.Order;

public class OrderJaxWsJaxBTest {
	private static final String NEW = " New";
	private static final String SERVICE_ENDPOINT = HOST + "order";

	private OrderTest orderService;

	@BeforeClass
	public static void publishOrderTestEndpoint() {
		Endpoint.publish(SERVICE_ENDPOINT, new OrderTestImpl());
	}

	@Before
	public void createOrderServiceClient() {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(OrderTest.class);
		factory.setAddress(SERVICE_ENDPOINT);
		orderService = (OrderTest) factory.create();
	}

	@Test
	public void checkIfOrderJaxBAnnotationsAreValid() {
		Order anOrder = anOrder().build();

		Order result = orderService.testOrder(anOrder);

		assertThat("Order Id", result.getId(), equalTo(ORDER_ID + NEW));
		assertThat("Order customer", result.getCustomer(), notNullValue());
	}
}
