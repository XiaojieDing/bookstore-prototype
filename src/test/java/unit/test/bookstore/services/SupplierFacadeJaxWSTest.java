package test.bookstore.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static test.endtoend.bookstore.builder.ProductBuilder.aProduct;
import static test.endtoend.bookstore.builder.ProductBuilder.aProductWhichIsUnknown;

import java.math.BigDecimal;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.xmlsoap.schemas.ws._2004._08.addressing.AttributedURI;
import org.xmlsoap.schemas.ws._2004._08.addressing.EndpointReferenceType;

import bookstore.Product;
import bookstore.Supplier;
import bookstore.SupplierRegistry;
import bookstore.UnknownProductFault;
import bookstore.services.SupplierFacadeJaxWs;

public class SupplierFacadeJaxWSTest {
	private static final String ENDPOINT_ADDRESS = "address";
	private static final BigDecimal TOTAL_PRICE = new BigDecimal(4);
	private static final BigDecimal SINGLE_UNIT_PRICE = new BigDecimal(4);
	private static final int ANY_AMOUNT = 0;
	protected static final String ERROR_MESSAGE = "Prodcut not available";

	@Rule
	public JUnitRuleMockery context = new JUnitRuleMockery();
	@Mock
	private SupplierRegistry registry;

	private Supplier supplier;

	@Before
	public void createSupplier() {
		supplier = new SupplierFacadeJaxWs(registry) {
			@Override
			public Supplier createSupplierProxyFor(String address) {
				return aDummySupplier();
			}
		};
	}

	@Test
	public void orderOneProductFromSupplierReturnedByRegistry() {
		//@formatter:off
		final Product aProduct = aProduct()
								 .withProductId("xyz")
								 .withSingleUnitPrice(SINGLE_UNIT_PRICE).build();

		context.checking(new Expectations() {{
			oneOf(registry).getAddressFromSupplierFor(aProduct); will(returnValue(anEndpoint()));
		}});
		//@formatter:on

		BigDecimal totalPrice = supplier.order(aProduct, 1);
		assertThat("Total price", totalPrice, is((equalTo(TOTAL_PRICE))));
	}

	private EndpointReferenceType anEndpoint() {
		EndpointReferenceType anEndpoint = new EndpointReferenceType();
		anEndpoint.setAddress(anAddress());
		return anEndpoint;
	}

	private AttributedURI anAddress() {
		AttributedURI uri = new AttributedURI();
		uri.setValue(ENDPOINT_ADDRESS);
		return uri;
	}

	private Supplier aDummySupplier() {
		return new Supplier() {
			@Override
			public BigDecimal order(Product product, int amount) {
				return TOTAL_PRICE;
			}
		};
	}

	@Test(expected = UnknownProductFault.class)
	public void reportUnknownProductFaultIfProductIsNotProvidedByAnySupplier() {
		final Product aProduct = aProductWhichIsUnknown();

		//@formatter:off
		context.checking(new Expectations() {{
			oneOf(registry).getAddressFromSupplierFor(aProduct); will(throwException(new UnknownProductFault(ERROR_MESSAGE)));
		}});
		//@formatter:on

		supplier.order(aProduct, ANY_AMOUNT);
	}
}
