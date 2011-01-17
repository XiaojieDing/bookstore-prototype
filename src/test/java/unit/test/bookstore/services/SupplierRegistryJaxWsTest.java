package test.bookstore.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static test.endtoend.bookstore.builder.ProductBuilder.PRODUCT_ID;
import static test.endtoend.bookstore.builder.ProductBuilder.PROVIDED_BY_AUSTRIA_SUPPLIER_CONTINUOUS_DEL;
import static test.endtoend.bookstore.builder.ProductBuilder.aProduct;
import static test.endtoend.bookstore.builder.ProductBuilder.aProductProvidedByAustriaSupplier;
import static test.endtoend.bookstore.builder.ProductBuilder.aProductWhichIsUnknown;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.xmlsoap.schemas.ws._2004._08.addressing.EndpointReferenceType;

import bookstore.BookstoreLibrary;
import bookstore.InformationReporter;
import bookstore.Product;
import bookstore.SupplierRegistry;
import bookstore.UnknownProductFault;
import bookstore.services.SupplierRegistryJaxWs;

public class SupplierRegistryJaxWsTest {
	private static final String EMPTY_ADDRESS = "";
	private static final String ADDRESS = "http://localhist:9000/supplieraustria";
	@Rule
	public JUnitRuleMockery context = new JUnitRuleMockery();
	@Mock
	private BookstoreLibrary library;
	@Mock
	private InformationReporter reporter;

	private SupplierRegistry registry;

	@Before
	public void createSupplier() {
		registry = new SupplierRegistryJaxWs(library, reporter);
	}

	@Test
	public void provideAddressOfSupplierForAGivenProduct() {
		final Product aProduct = aProduct().withProductId(PRODUCT_ID).build();

		//@formatter:off
		context.checking(new Expectations() {{
			ignoring(reporter);
			oneOf(library).getSupplierAddressFor(PRODUCT_ID); will(returnValue(ADDRESS));
		}});
		//@formatter:on

		EndpointReferenceType endpoint = registry.getAddressFromSupplierFor(aProduct);
		assertThat("Supplier's address", endpoint.getAddress().getValue(), is(equalTo(ADDRESS)));
	}

	@Test(expected = UnknownProductFault.class)
	public void reportErrorBecauseGivenProductIsUnknown() {
		final Product aProduct = aProductWhichIsUnknown();

		//@formatter:off
		context.checking(new Expectations() {{
			ignoring(reporter);
			oneOf(library).getSupplierAddressFor(aProduct.getId()); will(returnValue(EMPTY_ADDRESS));
		}});
		//@formatter:on

		registry.getAddressFromSupplierFor(aProduct);
	}

	@Test
	public void reportInformationAboutGetSupplierRequest() {
		final Product aProduct = aProductProvidedByAustriaSupplier();

		//@formatter:off
		context.checking(new Expectations() {{
			oneOf(library).getSupplierAddressFor(PROVIDED_BY_AUSTRIA_SUPPLIER_CONTINUOUS_DEL); will(returnValue(ADDRESS));
			oneOf(reporter).notifyGetSupplierRequest(aProduct, ADDRESS);
		}});
		//@formatter:on

		registry.getAddressFromSupplierFor(aProduct);
	}
}
