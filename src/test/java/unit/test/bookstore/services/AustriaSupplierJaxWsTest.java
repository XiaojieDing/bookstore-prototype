package test.bookstore.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static test.endtoend.bookstore.builder.ProductBuilder.aProductProvidedByAustriaSupplier;
import static test.endtoend.bookstore.builder.ProductBuilder.aProductProvidedByAustriaSupplierButNotAvailable;

import java.math.BigDecimal;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import bookstore.BookstoreLibrary;
import bookstore.InformationReporter;
import bookstore.Product;
import bookstore.UnknownProductFault;
import bookstore.services.AustriaSupplierJaxWs;

public class AustriaSupplierJaxWsTest {
	private static final int AMOUNT_OF_ONE = 1;
	private static final int AMOUNT_OF_TWO = 2;

	@Rule
	public JUnitRuleMockery context = new JUnitRuleMockery();
	@Mock
	private BookstoreLibrary library;
	@Mock
	private InformationReporter reporter;

	private AustriaSupplierJaxWs supplier;

	@Before
	public void createAustriaSupplier() {
		supplier = new AustriaSupplierJaxWs(library, reporter);
	}

	@Test(expected = UnknownProductFault.class)
	public void reportErrorIfProductIsNotAvailableAnymore() {
		final Product aProduct = aProductProvidedByAustriaSupplierButNotAvailable();

		//@formatter:off
		context.checking(new Expectations() {{
			ignoring(reporter);
			oneOf(library).isAvailableInAustria(aProduct, AMOUNT_OF_ONE); will(returnValue(false));
		}});
		//@formatter:on

		supplier.order(aProduct, AMOUNT_OF_ONE);
	}

	@Test
	public void calculateTotalPriceOfOneOrderedProduct() {
		final Product aProduct = aProductProvidedByAustriaSupplier();

		//@formatter:off
		context.checking(new Expectations() {{
			ignoring(reporter);
			oneOf(library).isAvailableInAustria(aProduct, AMOUNT_OF_ONE); will(returnValue(true));
			oneOf(library).getFromAustriaSupplier(aProduct.getId());
		}});
		//@formatter:on

		BigDecimal totalPrice = supplier.order(aProduct, AMOUNT_OF_ONE);
		assertThat("Total price of prodcuct", totalPrice, is(equalTo(new BigDecimal(2))));
	}

	@Test
	public void calculateTotalPriceOfTwpOrderedProduct() {
		final Product aProduct = aProductProvidedByAustriaSupplier();

		//@formatter:off
		context.checking(new Expectations() {{
			ignoring(reporter);
			oneOf(library).isAvailableInAustria(aProduct, AMOUNT_OF_TWO); will(returnValue(true));

			oneOf(library).getFromAustriaSupplier(aProduct.getId());
			oneOf(library).getFromAustriaSupplier(aProduct.getId());
		}});
		//@formatter:on

		BigDecimal totalPrice = supplier.order(aProduct, AMOUNT_OF_TWO);
		assertThat("Total price of prodcuct", totalPrice, is(equalTo(new BigDecimal(4))));
	}

	@Test
	public void notifyInformationAboutOrderReqeust() {
		final Product aProduct = aProductProvidedByAustriaSupplier();

		//@formatter:off
		context.checking(new Expectations() {{
			oneOf(library).isAvailableInAustria(aProduct, AMOUNT_OF_ONE); will(returnValue(true));
			oneOf(library).getFromAustriaSupplier(aProduct.getId());

			oneOf(reporter).notifyOrderRequestFromAustriaSupplier(with(aProduct), with(AMOUNT_OF_ONE), with(any(BigDecimal.class)));
		}});
		//@formatter:on

		supplier.order(aProduct, AMOUNT_OF_ONE);
	}
}
