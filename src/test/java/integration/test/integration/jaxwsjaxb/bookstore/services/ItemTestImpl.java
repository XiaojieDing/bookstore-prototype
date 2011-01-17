package test.integration.jaxwsjaxb.bookstore.services;

import bookstore.Item;

public class ItemTestImpl implements ItemTest {

	@Override
	public Item testItem(Item item) {
		// @formatter:off
		Item result = new Item(
			item.getQuantity() + 1,
			item.getOrder(),
			item.getProduct()
		);
		// @formatter:on
		return result;
	}

}
