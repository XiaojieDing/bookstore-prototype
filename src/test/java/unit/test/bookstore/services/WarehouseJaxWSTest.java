package test.bookstore.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static test.endtoend.bookstore.builder.ProductBuilder.aProduct;

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
import bookstore.ProductAvailability;
import bookstore.Warehouse;
import bookstore.services.WarehouseJaxWS;

public class WarehouseJaxWSTest {
	private static final BigDecimal TOTAL_PRICE_4 = new BigDecimal(4);
	private static final BigDecimal TOTAL_PRICE_2 = new BigDecimal(2);
	private static final BigDecimal SINGLE_UNIT_PRICE = new BigDecimal(2);
	private static final boolean AVAILABLE = true;
	private static final boolean NOT_AVAILABLE = false;
	private static final int AMOUNT_1 = 1;
	private static final int AMOUNT_2 = 2;
	private static final int NO_PRODUCTS = 0;
	private static final int ANY_NR_HIGHER_THAN_ZERO = 1;

	@Rule
	public JUnitRuleMockery context = new JUnitRuleMockery();

	@Mock
	private BookstoreLibrary library;
	@Mock
	private InformationReporter reporter;

	private Warehouse warehouseService;

	@Before
	public void createWarehouse() {
		warehouseService = new WarehouseJaxWS(library, reporter);
	}

	@Test
	public void checkIfProductIsAvailableInWarehouse() {
		final Product aProduct = aProduct().build();

		// @formatter:off
		context.checking(new Expectations() {{
			oneOf(library).countProducts(aProduct.getId()); will(returnValue(AMOUNT_1));
		}});
		// @formatter:on

		ProductAvailability result = warehouseService.checkAvailability(aProduct, AMOUNT_1);

		assertThat("Product available in warehouse", result.isAvailable(), is(AVAILABLE));
	}

	@Test
	public void notEnoughProductsAvailableInWarehouse() {
		final Product aProduct = aProduct().build();

		// @formatter:off
		context.checking(new Expectations() {{
			oneOf(library).countProducts(aProduct.getId()); will(returnValue(NO_PRODUCTS));
		}});
		// @formatter:on

		ProductAvailability result = warehouseService.checkAvailability(aProduct, ANY_NR_HIGHER_THAN_ZERO);

		assertThat("Product not available in warehouse", result.isAvailable(), is(NOT_AVAILABLE));
	}

	@Test
	public void orderExactlyOneProduct() {
		final Product aProduct = aProduct().withSingleUnitPrice(SINGLE_UNIT_PRICE).build();

		// @formatter:off
		context.checking(new Expectations() {{
			ignoring(reporter);
			oneOf(library).deleteProduct(aProduct);
		}});
		// @formatter:on

		BigDecimal totalPrice = warehouseService.order(aProduct, AMOUNT_1);

		assertThat("Total price", totalPrice, is(equalTo(TOTAL_PRICE_2)));
	}

	@Test
	public void orderExactlyTwoProducts() {
		final Product aProduct = aProduct().withSingleUnitPrice(SINGLE_UNIT_PRICE).build();

		// @formatter:off
		context.checking(new Expectations() {{
			ignoring(reporter);
			allowing(library).deleteProduct(aProduct);
		}});
		// @formatter:on

		BigDecimal totalPrice = warehouseService.order(aProduct, AMOUNT_2);

		assertThat("Total price", totalPrice, is(equalTo(TOTAL_PRICE_4)));
	}

	@Test
	public void reportOrderProcessing() {
		final Product aProduct = aProduct().build();

		// @formatter:off
		context.checking(new Expectations() {{
			ignoring(library);
			oneOf(reporter).notifyOrderProcessingOf(with(aProduct),
													with(AMOUNT_1),
													with(any(BigDecimal.class)));
		}});
		// @formatter:on

		warehouseService.order(aProduct, AMOUNT_1);
	}
}
