package bookstore;

import java.math.BigDecimal;

public interface BookstoreLibrary {

	void addCustomer(Customer customer);

	void deleteCustomer(String id);

	Customer getCustomer(String id);

	void updateCustomer(Customer customer);

	void updateAccount(Customer customer, BigDecimal balance);

	void deleteProduct(Product aProduct);

	int countProducts(String id);

	String getSupplierAddressFor(String productId);

	void getFromAustriaSupplier(String id);

	void getFromGermanSupplier(String id);

	boolean isAvailableInAustria(Product aProduct, int amount);

	boolean isAvailableInGermany(Product aProduct, int amount);

	boolean isValid(String address);

}
