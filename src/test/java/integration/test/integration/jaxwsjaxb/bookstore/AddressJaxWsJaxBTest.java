package test.integration.jaxwsjaxb.bookstore;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static test.endtoend.bookstore.builder.AddressBuilder.ADDRESS_ID;
import static test.endtoend.bookstore.builder.AddressBuilder.CITY;
import static test.endtoend.bookstore.builder.AddressBuilder.DOOR_NR;
import static test.endtoend.bookstore.builder.AddressBuilder.HOUSE_NR;
import static test.endtoend.bookstore.builder.AddressBuilder.STREET;
import static test.endtoend.bookstore.builder.AddressBuilder.ZIP_CODE;
import static test.endtoend.bookstore.builder.AddressBuilder.anAddress;
import static test.integration.jaxwsjaxb.bookstore.Foo.HOST;

import javax.xml.ws.Endpoint;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import test.integration.jaxwsjaxb.bookstore.services.AddressTest;
import test.integration.jaxwsjaxb.bookstore.services.AddressTestImpl;
import bookstore.Address;

public class AddressJaxWsJaxBTest {

	private static final String NEW = " New";
	private static final String SERVICE_ENDPOINT = HOST + "address";

	@BeforeClass
	public static void publishAddressTestEndpoint() {
		Endpoint.publish(SERVICE_ENDPOINT, new AddressTestImpl());
	}

	private AddressTest addressService;

	@Before
	public void createAddressServiceClient() {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(AddressTest.class);
		factory.setAddress(SERVICE_ENDPOINT);
		addressService = (AddressTest) factory.create();
	}

	@Test
	public void checkIfAddressJaxBAnnotationsAreValid() {
		// @formatter:off
		Address anAddress = anAddress().build();
		// @formatter:on

		Address result = addressService.testAddress(anAddress);

		assertThat("Address Id", result.getId(), equalTo(ADDRESS_ID + NEW));
		assertThat("Address street", result.getStreet(), equalTo(STREET + NEW));
		assertThat("Address city", result.getCity(), equalTo(CITY + NEW));
		assertThat("Address house", result.getHouse(), equalTo(HOUSE_NR + 1));
		assertThat("Address door", result.getDoor(), equalTo(DOOR_NR + 1));
		assertThat("Address zipCode", result.getZipCode(), equalTo(ZIP_CODE + NEW));
		assertThat("Address shippable", result.isShipping(), equalTo(true));
		assertThat("Address billing", result.isBilling(), equalTo(true));
		assertThat("Address other", result.isOther(), equalTo(true));

	}

}
