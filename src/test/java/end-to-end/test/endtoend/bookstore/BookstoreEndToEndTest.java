package test.endtoend.bookstore;

import static bookstore.services.BookstoreJaxWS.SUCCESS_MESSAGE;
import static bookstore.services.ShippingServiceJaxWs.SHIPPING_ADDRESS_UNKNOWN;
import static java.text.MessageFormat.format;
import static test.endtoend.bookstore.builder.CustomerBuilder.CUSTOMER_SHIPPING_ADDRESS;
import static test.endtoend.bookstore.builder.OrderBuilder.anOrderFromCustomerWithUnknownShippingAddress;
import static test.endtoend.bookstore.builder.OrderBuilder.anOrderOfAProductProvideByGermanSupplier;
import static test.endtoend.bookstore.builder.OrderBuilder.anOrderOfAProductProvidedByAustriaSupplier;
import static test.endtoend.bookstore.builder.OrderBuilder.anOrderOfAProductProvidedByAustriaSupplierButNotAvailableAnymore;
import static test.endtoend.bookstore.builder.OrderBuilder.anOrderOfAProductProvidedByGermanSupplierButNotAvailableAnymore;
import static test.endtoend.bookstore.builder.OrderBuilder.anOrderOfAProductWhichIsUnknown;
import static test.endtoend.bookstore.builder.OrderBuilder.anOrderWithOneItem;

import java.math.BigDecimal;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import bookstore.BookstoreLibrary;
import bookstore.services.SystemOutLogger;

public class BookstoreEndToEndTest {

	private static final BigDecimal NEW_BALANCE_OF_2 = new BigDecimal(2);
	private static final BigDecimal NEW_BALANCE_OF_3 = new BigDecimal(3);
	private static final BigDecimal NEW_BALANCE_OF_4 = new BigDecimal(4);
	private static final String PRODUCT_NOT_AVAILABLE = "Product not available";

	private final BookstoreLibrary library = new FakeBookStoreLibrary();
	private final FakeBookStoreServer bookstoreServer = new FakeBookStoreServer(library, new SystemOutLogger(Logger.getAnonymousLogger()));
	private final ApplicationClient customer = new ApplicationClient(library);

	
	public static void main(String[] args) {
		BookstoreEndToEndTest e = new BookstoreEndToEndTest();
		e.bookstoreServer.startSellingProducts();
	}
	
	// @formatter:off
	@Test public void
	customerWithBalanceOfFiveOrdersOneProductOfPriceOneWhichIsAvailableInWarehouse() {
		bookstoreServer.startSellingProducts();
		customer.orders(anOrderWithOneItem());
		bookstoreServer.hasReceivedNewOrderRequest();
		customer.hasReceivedUpdateForOpenBalance(NEW_BALANCE_OF_4);
		customer.hasReceivedNotiyfication(format(SUCCESS_MESSAGE, CUSTOMER_SHIPPING_ADDRESS));
	}

	@Test public void
	customerWithBalanceOfFiveOrdersOneProductOfPriceTwoWhichWillBeProvidedByAustriaSupplier() {
		bookstoreServer.startSellingProducts();
		customer.orders(anOrderOfAProductProvidedByAustriaSupplier());
		bookstoreServer.hasReceivedNewOrderRequest();
		customer.hasReceivedUpdateForOpenBalance(NEW_BALANCE_OF_3);
		customer.hasReceivedNotiyfication(format(SUCCESS_MESSAGE, CUSTOMER_SHIPPING_ADDRESS));
	}

	@Test public void
	customerWithBalanceOfFiveOrdersOneProductOfPriceThreeWhichWillBeProvidedByGermanSupplier() {
		bookstoreServer.startSellingProducts();
		customer.orders(anOrderOfAProductProvideByGermanSupplier());
		bookstoreServer.hasReceivedNewOrderRequest();
		customer.hasReceivedUpdateForOpenBalance(NEW_BALANCE_OF_2);
		customer.hasReceivedNotiyfication(format(SUCCESS_MESSAGE, CUSTOMER_SHIPPING_ADDRESS));
	}

	@Test public void
	customerOrdersProductWhichIsNotAvailableInWarehouseOrProvidedByASupplier() {
		bookstoreServer.startSellingProducts();
		customer.orders(anOrderOfAProductWhichIsUnknown());
		bookstoreServer.hasReceivedNewOrderRequest();
		customer.hasReceivedNotiyfication(PRODUCT_NOT_AVAILABLE);
	}

	@Test public void
	customerOrdersProductProvidedByAustriaSupplierButItIsNotAvailableAnymore() {
		bookstoreServer.startSellingProducts();
		customer.orders(anOrderOfAProductProvidedByAustriaSupplierButNotAvailableAnymore());
		bookstoreServer.hasReceivedNewOrderRequest();
		customer.hasReceivedNotiyfication(PRODUCT_NOT_AVAILABLE);
	}

	@Test public void
	customerOrdersProductProvidedByGermanSupplierButItIsNotAvailableAnymore() {
		bookstoreServer.startSellingProducts();
		customer.orders(anOrderOfAProductProvidedByGermanSupplierButNotAvailableAnymore());
		bookstoreServer.hasReceivedNewOrderRequest();
		customer.hasReceivedNotiyfication(PRODUCT_NOT_AVAILABLE);
	}

	@Test public void
	customerOrdersProductButCustomersShippingAddressIsUnknown() {
		bookstoreServer.startSellingProducts();
		customer.orders(anOrderFromCustomerWithUnknownShippingAddress());
		bookstoreServer.hasReceivedNewOrderRequest();
		customer.hasReceivedNotiyfication(SHIPPING_ADDRESS_UNKNOWN);
	}

	// @formatter:on

	@BeforeClass
	public static void setPropertyToMakeCXFLoggingConfigurable() {
		System.setProperty("java.util.logging.config.file", "src/test/resources/logging.properties");
	}

	@After
	public void stopSelling() {
		bookstoreServer.stopSellingProducts();
	}

}
