package test.bookstore.services;

import static bookstore.SupplierRegistry.AUSTRIA_SUPPLIER_ADDRESS;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static test.endtoend.bookstore.builder.ProductBuilder.PROVIDED_BY_AUSTRIA_SUPPLIER_USER_STORIES;
import static test.endtoend.bookstore.builder.ProductBuilder.aProduct;

import org.junit.Test;

import test.endtoend.bookstore.FakeBookStoreLibrary;
import bookstore.BookstoreLibrary;
import bookstore.Product;

public class BookstoreLibraryTest {

	private BookstoreLibrary library = new FakeBookStoreLibrary();

	@Test
	public void returnSupplierAddressForAGivenProductId() {
		Product aProduct = aProduct().withProductId(PROVIDED_BY_AUSTRIA_SUPPLIER_USER_STORIES).build();

		String address = library.getSupplierAddressFor(aProduct.getId());

		assertThat("Supplier's address", address, is(equalTo(AUSTRIA_SUPPLIER_ADDRESS)));
	}
}
