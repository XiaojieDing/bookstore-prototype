package bookstore;

import java.math.BigDecimal;

public interface InformationReporter {

	void notifyShippingEvent(Item[] items, Address anAddress);

	void notifyNewOrderRequest(Order anOrder);

	void notifyGetCustomerRequest(String customerId, Customer aCustomer);

	void notifyOrderProcessingOf(Product aProduct, int amount, BigDecimal totalPrice);

	void notifyGetSupplierRequest(Product aProduct, String address);

	void notifyOrderRequestFromAustriaSupplier(Product aProduct, int amount, BigDecimal totalPrice);

	void notifyOrderRequestFromGermanSupplier(Product aProduct, int amount, BigDecimal totalPrice);

	void notifyUpdateOfCustomersAccount(Customer aCustomer, BigDecimal openBalance);

	void notifyThatCustomerReceivesANotificationMessage(Customer aCustomer, String message);

}
