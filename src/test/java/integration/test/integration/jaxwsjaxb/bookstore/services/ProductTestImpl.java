package test.integration.jaxwsjaxb.bookstore.services;

import java.math.BigDecimal;

import bookstore.Product;

public class ProductTestImpl implements ProductTest {

	@Override
	public Product testProduct(Product product) {
		// @formatter:off
		Product result = new Product(
			product.getId() + " New",
			product.getName() + " New",
			product.getSingleUnitPrice().add(new BigDecimal(1))
		);
		result.addItem(product.getItems().get(0));
		// @formatter:on
		return result;
	}

}
