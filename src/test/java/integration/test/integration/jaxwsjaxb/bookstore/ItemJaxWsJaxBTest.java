package test.integration.jaxwsjaxb.bookstore;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static test.endtoend.bookstore.builder.ItemBuilder.anItem;
import static test.integration.jaxwsjaxb.bookstore.Foo.HOST;

import javax.xml.ws.Endpoint;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import test.integration.jaxwsjaxb.bookstore.services.ItemTest;
import test.integration.jaxwsjaxb.bookstore.services.ItemTestImpl;
import bookstore.Item;

public class ItemJaxWsJaxBTest {

	private static final String SERVICE_ENDPOINT = HOST + "item";

	private ItemTest itemService;

	@BeforeClass
	public static void publishItemTestEndpoint() {
		Endpoint.publish(SERVICE_ENDPOINT, new ItemTestImpl());
	}

	@Before
	public void createItemServiceClient() {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(ItemTest.class);
		factory.setAddress(SERVICE_ENDPOINT);
		itemService = (ItemTest) factory.create();
	}

	@Test
	public void checkIfItemJaxBAnnotationsAreValid() {
		// @formatter:off
		Item anItem = anItem().build();
		// @formatter:on

		Item result = itemService.testItem(anItem);

		assertThat("Item Id", result.getQuantity(), equalTo(2));
		assertThat("Item product", result.getProduct(), notNullValue());
		assertThat("Item order", result.getOrder(), nullValue());
	}
}
