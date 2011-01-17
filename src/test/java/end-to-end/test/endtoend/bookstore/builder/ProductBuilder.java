package test.endtoend.bookstore.builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import bookstore.Item;
import bookstore.Product;

public class ProductBuilder {
	public static final String PRODUCT_ID = "978-0321503626";
	public static final String PRODUCT_NAME = "Growing Object-Oriented Software, Guided by Tests";
	public static final String PROVIDED_BY_AUSTRIA_SUPPLIER_CONTINUOUS_DEL = "978-0321601919";
	public static final String PROVIDED_BY_AUSTRIA_SUPPLIER_USER_STORIES = "978-0321205681";
	public static final String PROVIDED_BY_AUSTRIA_SUPPLIER_BUT_NOT_AVAILABLE = "978-0201633610";
	public static final String PROVIDED_BY_GERMAN_SUPPLIER = "978-0321336385";

	private static final String UNKNOWN = "978-0201485677";
	private static final String NOT_AVAILABLE_IN_AUSTRIA = "not available";
	private static final String NOT_AVAILABLE_IN_GERMANY = "not available in germany";

	private String id = PRODUCT_ID;
	private String name = PRODUCT_NAME;
	private BigDecimal singleUnitPrice = new BigDecimal(1);
	private List<Item> items = new ArrayList<Item>();

	public static ProductBuilder aProduct() {
		return new ProductBuilder();
	}

	public static Product aProductProvidedByAustriaSupplier() {
		// @formatter:off
		return aProduct()
			   .withProductId(PROVIDED_BY_AUSTRIA_SUPPLIER_CONTINUOUS_DEL)
			   .withSingleUnitPrice(new BigDecimal(2)).build();
		// @formatter:on
	}

	public static Product aProductProvidedByGermanSupplier() {
		// @formatter:off
		return aProduct()
				.withProductId(PROVIDED_BY_GERMAN_SUPPLIER)
				.withSingleUnitPrice(new BigDecimal(3)).build();
		// @formatter:on
	}

	public static Product aProductWhichIsUnknown() {
		// @formatter:off
		return aProduct()
				.withProductId(UNKNOWN)
				.build();
		// @formatter:on
	}

	public static Product aProductProvidedByAustriaSupplierButNotAvailable() {
		// @formatter:off
		return aProduct()
				.withProductId(NOT_AVAILABLE_IN_AUSTRIA)
				.build();
		// @formatter:on
	}

	public static Product aProductProvidedByGermanySupplierButNotAvailable() {
		// @formatter:off
		return aProduct()
		.withProductId(NOT_AVAILABLE_IN_GERMANY)
		.build();
		// @formatter:on
	}

	public ProductBuilder withProductId(String id) {
		this.id = id;
		return this;
	}

	public ProductBuilder withSingleUnitPrice(BigDecimal singleUnitPrice) {
		this.singleUnitPrice = singleUnitPrice;
		return this;
	}

	public ProductBuilder ofName(String name) {
		this.name = name;
		return this;
	}

	public ProductBuilder forItem(Item item) {
		items.add(item);
		return this;
	}

	public Product build() {
		Product product = new Product(id, name, singleUnitPrice);
		for (Item each : items) {
			product.addItem(each);
		}
		return product;
	}

}
