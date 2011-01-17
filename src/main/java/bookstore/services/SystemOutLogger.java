package bookstore.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Logger;

import bookstore.Address;
import bookstore.Customer;
import bookstore.InformationReporter;
import bookstore.Item;
import bookstore.Order;
import bookstore.Product;

public class SystemOutLogger implements InformationReporter {
	private static final String WAREHOUSE = "[Warehouse] ";
	private static final String SHIPPING_SERVICE = "[ShippingService] ";
	private static final String SUPPLIER_AUSTRIA = "[Supplier (Austria)] ";
	private static final String SUPPLIER_GERMANY = "[Supplier (Germany)] ";
	private static final String SUPPLIER_REGISTRY = "[SupplierRegistry] ";
	private static final String CUSTOMER_MANAGEMENT_JAXRS = "[CustomerManagement (Jax-RS)] ";
	private static final String BOOKSTORE = "[Bookstore] ";
	private static final String NEW_LINE = "\n";
	private static final String SEPARATOR = "      ";

	private Logger logger;

	public SystemOutLogger(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void notifyShippingEvent(Item[] items, Address anAddress) {
		StringBuffer logEntry = buildShippingLogEntry(items, anAddress);
		logger.info(logEntry.toString());
	}

	private StringBuffer buildShippingLogEntry(Item[] items, Address anAddress) {
		StringBuffer logEntry = new StringBuffer();
		appendTimeStamp(logEntry, SHIPPING_SERVICE);
		logEntry.append(SHIPPING_SERVICE);
		logEntry.append("Sending item" + (items.length > 1 ? "s " : " "));
		appendItems(logEntry, items);
		logEntry.append(NEW_LINE + SEPARATOR);
		logEntry.append(SHIPPING_SERVICE);
		logEntry.append(anAddress);
		logEntry.append(NEW_LINE);
		return logEntry;
	}

	private void appendTimeStamp(StringBuffer logEntry, String prefix) {
		logEntry.append(prefix);
		logEntry.append(new Date());
		logEntry.append(NEW_LINE + SEPARATOR);
	}

	private void appendItems(StringBuffer logEntry, Item[] items) {
		for (int i = 0; i < items.length; i++) {
			logEntry.append("\"" + items[i].getProductName() + "\"");
			logEntry.append(i == items.length - 1 ? "" : ", ");
		}
		logEntry.append(" to");
	}

	@Override
	public void notifyNewOrderRequest(Order anOrder) {
		StringBuffer logEntry = new StringBuffer();
		appendTimeStamp(logEntry, BOOKSTORE);
		logEntry.append(BOOKSTORE + "Received new order request from customer:");
		logEntry.append(NEW_LINE + SEPARATOR);
		logEntry.append(BOOKSTORE + anOrder.getCustomer());
		logEntry.append(NEW_LINE);

		logger.info(logEntry.toString());
	}

	@Override
	public void notifyGetCustomerRequest(String customerId, Customer aCustomer) {
		StringBuffer logEntry = new StringBuffer();
		appendTimeStamp(logEntry, CUSTOMER_MANAGEMENT_JAXRS);
		logEntry.append(CUSTOMER_MANAGEMENT_JAXRS + "Get customer for id: " + customerId);
		logEntry.append(NEW_LINE + SEPARATOR);
		logEntry.append(CUSTOMER_MANAGEMENT_JAXRS + "Found customer: " + aCustomer);
		logEntry.append(NEW_LINE + SEPARATOR);

		logger.info(logEntry.toString());
	}

	@Override
	public void notifyOrderProcessingOf(Product aProduct, int amount, BigDecimal totalPrice) {
		StringBuffer logEntry = new StringBuffer();
		appendTimeStamp(logEntry, WAREHOUSE);
		logEntry.append(WAREHOUSE + "Orders product with " + aProduct + "; " + amount + " time" + (amount > 1 ? "s" : ""));
		logEntry.append(NEW_LINE + SEPARATOR);
		logEntry.append(WAREHOUSE + "Total price: " + totalPrice);
		logEntry.append(NEW_LINE + SEPARATOR);

		logger.info(logEntry.toString());

	}

	@Override
	public void notifyGetSupplierRequest(Product aProduct, String address) {
		StringBuffer logEntry = new StringBuffer();
		appendTimeStamp(logEntry, SUPPLIER_REGISTRY);
		logEntry.append(SUPPLIER_REGISTRY + "Received an supplier-address request for: " + aProduct);
		logEntry.append(NEW_LINE + SEPARATOR);
		logEntry.append(SUPPLIER_REGISTRY + "Found supplier-address: \"" + address + "\"");
		logEntry.append(NEW_LINE + SEPARATOR);

		logger.info(logEntry.toString());
	}

	@Override
	public void notifyOrderRequestFromAustriaSupplier(Product aProduct, int amount, BigDecimal totalPrice) {
		StringBuffer logEntry = new StringBuffer();
		appendTimeStamp(logEntry, SUPPLIER_AUSTRIA);
		logEntry.append(SUPPLIER_AUSTRIA + "Received an order-request for: " + aProduct + "; of amount: " + amount);
		logEntry.append(NEW_LINE + SEPARATOR);
		logEntry.append(SUPPLIER_AUSTRIA + "Total-price of order: \"" + totalPrice + "\"");
		logEntry.append(NEW_LINE + SEPARATOR);

		logger.info(logEntry.toString());
	}

	@Override
	public void notifyOrderRequestFromGermanSupplier(Product aProduct, int amount, BigDecimal totalPrice) {
		StringBuffer logEntry = new StringBuffer();
		appendTimeStamp(logEntry, SUPPLIER_GERMANY);
		logEntry.append(SUPPLIER_GERMANY + "Received an order-request for: " + aProduct + "; of amount: " + amount);
		logEntry.append(NEW_LINE + SEPARATOR);
		logEntry.append(SUPPLIER_GERMANY + "Total-price of order: \"" + totalPrice + "\"");
		logEntry.append(NEW_LINE + SEPARATOR);

		logger.info(logEntry.toString());
	}

	@Override
	public void notifyUpdateOfCustomersAccount(Customer aCustomer, BigDecimal openBalance) {
		StringBuffer logEntry = new StringBuffer();
		appendTimeStamp(logEntry, CUSTOMER_MANAGEMENT_JAXRS);
		logEntry.append(CUSTOMER_MANAGEMENT_JAXRS + "Account-update for customer: " + aCustomer);
		logEntry.append(NEW_LINE + SEPARATOR);
		logEntry.append(CUSTOMER_MANAGEMENT_JAXRS + "Current open balance: \"" + aCustomer.getOpenBalance() + "\"");
		logEntry.append(NEW_LINE + SEPARATOR);
		logEntry.append(CUSTOMER_MANAGEMENT_JAXRS + "New open balance: \"" + openBalance + "\"");
		logEntry.append(NEW_LINE + SEPARATOR);

		logger.info(logEntry.toString());
	}

	@Override
	public void notifyThatCustomerReceivesANotificationMessage(Customer aCustomer, String message) {
		StringBuffer logEntry = new StringBuffer();
		appendTimeStamp(logEntry, CUSTOMER_MANAGEMENT_JAXRS);
		logEntry.append(CUSTOMER_MANAGEMENT_JAXRS + "Notification message for customer: " + aCustomer);
		logEntry.append(NEW_LINE + SEPARATOR);
		logEntry.append(CUSTOMER_MANAGEMENT_JAXRS + "Received message: \"" + message + "\"");
		logEntry.append(NEW_LINE + SEPARATOR);

		logger.info(logEntry.toString());
	}
}
