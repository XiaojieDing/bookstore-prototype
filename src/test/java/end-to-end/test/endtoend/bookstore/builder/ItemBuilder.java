package test.endtoend.bookstore.builder;

import static test.endtoend.bookstore.builder.OrderBuilder.anOrder;
import static test.endtoend.bookstore.builder.ProductBuilder.aProduct;
import bookstore.Item;
import bookstore.Order;
import bookstore.Product;

public class ItemBuilder {

	private int quantity = 1;
	private Order order = anOrder().build();
	private Product product = aProduct().build();

	public static ItemBuilder anItem() {
		return new ItemBuilder();
	}

	public static Item anItemOfOneProduct(Product aProduct) {
		//@formatter:off
		return anItem()
				.ofQuantity(1)
				.ofProduct(aProduct).build();
		//@formatter:on
	}

	public ItemBuilder ofQuantity(int quantity) {
		this.quantity = quantity;
		return this;
	}

	public ItemBuilder ofProduct(Product product) {
		this.product = product;
		return this;
	}

	public ItemBuilder forOrder(Order order) {
		this.order = order;
		return this;
	}

	public Item build() {
		Item item = new Item(quantity, order, product);
		return item;
	}

}
