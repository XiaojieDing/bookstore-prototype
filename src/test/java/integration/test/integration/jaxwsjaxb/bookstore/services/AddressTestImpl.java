package test.integration.jaxwsjaxb.bookstore.services;

import bookstore.Address;

public class AddressTestImpl implements AddressTest {

	@Override
	public Address testAddress(Address address) {
		// @formatter:off
		Address result = new Address(
			address.getId() + " New",
			address.getStreet() + " New",
			address.getCity() + " New",
			address.getHouse() + 1,
			address.getDoor() + 1,
			address.getZipCode() + " New",
			address.isShipping() ? false : true,
			address.isBilling() ? false : true,
			address.isOther() ? false : true

		);
		// @formatter:on
		return result;
	}
}
