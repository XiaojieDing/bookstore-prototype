package test.bookstore.services;

import static bookstore.services.ShippingServiceJaxWs.SHIPPING_ADDRESS_UNKNOWN;
import static org.hamcrest.Matchers.equalTo;
import static test.endtoend.bookstore.builder.CustomerBuilder.aCustomerWithAddressesAndOpenBalanceOfFive;
import static test.endtoend.bookstore.builder.CustomerBuilder.aCustomerWithUnknownShippingAddress;
import static test.endtoend.bookstore.builder.ItemBuilder.anItem;
import static test.endtoend.bookstore.builder.ItemBuilder.anItemOfOneProduct;
import static test.endtoend.bookstore.builder.OrderBuilder.anOrder;
import static test.endtoend.bookstore.builder.OrderBuilder.anOrderWithOneItem;
import static test.endtoend.bookstore.builder.ProductBuilder.aProduct;
import static test.endtoend.bookstore.builder.ProductBuilder.aProductWhichIsUnknown;

import java.math.BigDecimal;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import bookstore.Customer;
import bookstore.CustomerManagement;
import bookstore.InformationReporter;
import bookstore.Item;
import bookstore.NotificationMessage;
import bookstore.Order;
import bookstore.Product;
import bookstore.ProductAvailability;
import bookstore.ShippingService;
import bookstore.Supplier;
import bookstore.UnknownAddressFault;
import bookstore.UnknownProductFault;
import bookstore.Warehouse;
import bookstore.services.BookstoreJaxWS;

public class BookstoreJaxWSTest {
	private static final NotificationMessage ERROR_MESSAGE = new NotificationMessage("Product not available");
	private static final String NOT_AVAILABLE_IN_WAREHOUSE = "xyz";
	private static final BigDecimal NEW_BALANCE_OF_3 = new BigDecimal(3);
	private static final BigDecimal NEW_BALENCE_OF_4 = new BigDecimal(4);
	private static final BigDecimal SINGLE_UNIT_PRICE = new BigDecimal(1);
	private static final BigDecimal TOTAL_PRICE = new BigDecimal(1);
	private static final int NOT_IMPORTANT = 0;
	private static final int ESTIMATED_TIME_DELIVERY = 1;
	private static final boolean IS_AVAILABLE = true;
	private static final boolean NOT_AVAILABLE = false;

	private final Customer aCustomer = aCustomerWithAddressesAndOpenBalanceOfFive();
	private final Product aProduct = aProduct().withSingleUnitPrice(SINGLE_UNIT_PRICE).build();

	// @formatter:off

	@Rule
	public JUnitRuleMockery context = new JUnitRuleMockery();
	@Mock
	private CustomerManagement customerService;
	@Mock
	private Warehouse warehouse;
	@Mock
	private ShippingService shippingService;
	@Mock
	private Supplier supplier;
	@Mock
	private InformationReporter reporter;

	private BookstoreJaxWS bookstoreService;

	@Before
	public void createBookStoreService() {
		bookstoreService = new BookstoreJaxWS(customerService, warehouse, shippingService, supplier, reporter);
	}

	@Test
	public void orderExactlyOneProductFormWarehouse() {
		final Item anItem = anItem().ofQuantity(1)
									.ofProduct(aProduct).build();

		final Order anOrder = anOrder().fromCustomer(aCustomer)
									   .withItem(anItem).build();

		allowingCustomerServiceQuerying(aCustomer);
		allowingWarehouseOrdering(aProduct, 1);
		expectShippingServiceToShip(anItem);
		sendNotificationAndUpdateCustomerWith(NEW_BALENCE_OF_4);

		bookstoreService.requestOrder(anOrder);
	}

	private void allowingCustomerServiceQuerying(final Customer aCustomer) {
		context.checking(new Expectations() {{
			ignoring(reporter);
			allowing(customerService).getCustomer(aCustomer.getId()); will(returnValue(aCustomer));
		}});
	}

	private void allowingWarehouseOrdering(final Product aProduct, final int quantity) {
		context.checking(new Expectations() {{
			allowing(warehouse).checkAvailability(aProduct, quantity);	will(returnValue(availableInWarehouse()));
			allowing(warehouse).order(aProduct, quantity); will(returnValue(totalPriceOf(aProduct, quantity)));
		}

		private BigDecimal totalPriceOf(final Product aProduct, final int quantity) {
			return aProduct.getSingleUnitPrice().multiply(new BigDecimal(quantity));
		}});
	}

	private ProductAvailability availableInWarehouse() {
		return new ProductAvailability(IS_AVAILABLE, ESTIMATED_TIME_DELIVERY);
	};

	private void expectShippingServiceToShip(final Item... items) {
		context.checking(new Expectations() {{
			oneOf(shippingService).shipItems(items, aCustomer.getShippingAddress());
		}});
	}

	@Test
	public void orderExactlyTwoProductsFormWarehouse() {
		final Item anItem = anItem().ofQuantity(2)
									.ofProduct(aProduct).build();

		final Order anOrder = anOrder().fromCustomer(aCustomer)
									   .withItem(anItem).build();

		allowingCustomerServiceQuerying(aCustomer);
		allowingWarehouseOrdering(aProduct, 2);
		expectShippingServiceToShip(anItem);
		sendNotificationAndUpdateCustomerWith(NEW_BALANCE_OF_3);

		bookstoreService.requestOrder(anOrder);
	}

	private void sendNotificationAndUpdateCustomerWith(final BigDecimal balance) {
		context.checking(new Expectations() {{
			oneOf(customerService).updateAccount(aCustomer.getId(), balance);
			allowing(customerService).notify(with(aCustomer.getId()), with(any(NotificationMessage.class)));
		}});
	}

	@Test
	public void ordersOneProductNotAvailableInWarehouseButProvidedBySupplier() {
		final Product aProduct = aProduct().withProductId(NOT_AVAILABLE_IN_WAREHOUSE)
										   .withSingleUnitPrice(SINGLE_UNIT_PRICE).build();

		final Item anItem = anItem().ofQuantity(1)
		 							.ofProduct(aProduct).build();

		final Order anOrder = anOrder().fromCustomer(aCustomer)
									   .withItem(anItem).build();

		allowingCustomerServiceQuerying(aCustomer);
		expectingNotToBeAvailableInWarehouse(aProduct);

		context.checking(new Expectations() {{
			ignoring(customerService);
			ignoring(shippingService);

			oneOf(supplier).order(aProduct, 1); will(returnValue(TOTAL_PRICE));
		}});

		bookstoreService.requestOrder(anOrder);
	}

	private void expectingNotToBeAvailableInWarehouse(final Product aProduct) {
		context.checking(new Expectations() {{
			allowing(warehouse).checkAvailability(aProduct, 1); will(returnValue(notAvailableInWarehouse()));
		}});
	}

	private ProductAvailability notAvailableInWarehouse() {
		return new ProductAvailability(NOT_AVAILABLE, NOT_IMPORTANT);
	};

	@Test
	public void notifysCustomerThatProductWasUnknown() {
		final Product aProduct = aProductWhichIsUnknown();
		final Order anOrder = anOrder().withItem(anItemOfOneProduct(aProduct))
									   .fromCustomer(aCustomer).build();

		allowingCustomerServiceQuerying(aCustomer);
		expectingNotToBeAvailableInWarehouse(aProduct);
		expectingNotToBeAvailableAtSupplier(aProduct);

		context.checking(new Expectations() {{
			allowing(customerService).notify(with(aCustomer.getId()), with(productWasUnknownNotification()));
		}});

		bookstoreService.requestOrder(anOrder);
	}

	private void expectingNotToBeAvailableAtSupplier(final Product aProduct) {
		context.checking(new Expectations() {{
			oneOf(supplier).order(aProduct, 1); will(throwException(new UnknownProductFault(ERROR_MESSAGE.getMessage())));
		}});
	}

	protected Matcher<NotificationMessage> productWasUnknownNotification() {
		return new FeatureMatcher<NotificationMessage, String>(equalTo("Product not available"), "Notification", "order error") {
			@Override
			protected String featureValueOf(NotificationMessage actual) {
				return actual.getMessage();
			}
		};
	}

	@Test
	public void notifysCustomerThatShippingAddressWasUnknown() {
		final Customer aCustomer = aCustomerWithUnknownShippingAddress();
		final Product aProduct = aProduct().build();
		final Item anItem = anItemOfOneProduct(aProduct);
		final Order anOrder = anOrder().withItem(anItem)
									   .fromCustomer(aCustomer).build();

		allowingCustomerServiceQuerying(aCustomer);
		allowingWarehouseOrdering(aProduct, 1);

		context.checking(new Expectations() {{
			oneOf(shippingService).shipItems(new Item[] { anItem }, aCustomer.getShippingAddress());
			will(throwException(new UnknownAddressFault(SHIPPING_ADDRESS_UNKNOWN)));

			allowing(customerService).notify(with(aCustomer.getId()), with(shippingAddressUnknownNotification()));
		}});

		bookstoreService.requestOrder(anOrder);
	}

	protected Matcher<NotificationMessage> shippingAddressUnknownNotification() {
		return new FeatureMatcher<NotificationMessage, String>(equalTo(SHIPPING_ADDRESS_UNKNOWN), "Shipping Notification", "shipping error") {
			@Override
			protected String featureValueOf(NotificationMessage actual) {
				return actual.getMessage();
			}
		};
	}

	@Test
	public void reportsNewReceivedOrderRequest() {
		final Customer aCustomer = aCustomerWithUnknownShippingAddress();
		final Order anOrder = anOrderWithOneItem();

		context.checking(new Expectations() {{
			allowing(customerService).getCustomer(anOrder.getCustomerId()); will(returnValue(aCustomer));
			ignoring(customerService);
			ignoring(warehouse);
			ignoring(shippingService);

			oneOf(reporter).notifyNewOrderRequest(anOrder);
		}});

		bookstoreService.requestOrder(anOrder);
	}
	// @formatter:on
}
