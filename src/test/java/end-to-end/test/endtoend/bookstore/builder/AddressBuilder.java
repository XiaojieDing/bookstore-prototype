package test.endtoend.bookstore.builder;

import bookstore.Address;

public class AddressBuilder {

	public static final String ADDRESS_ID = "home";
	public static final String SHIPPING_ID_A = "addressA";
	public static final String SHIPPING_ID_B = "addressB";
	public static final String ZIP_CODE = "1070";
	public static final int DOOR_NR = 1;
	public static final int HOUSE_NR = 1;
	public static final String STREET = "Mikrastrasse";
	public static final String CITY = "Vienna";

	private String id = ADDRESS_ID;
	private String street = STREET;
	private String city = CITY;
	private int houseNr = HOUSE_NR;
	private int door = DOOR_NR;
	private String zipCode = ZIP_CODE;
	private boolean isOther;
	private boolean isBilling;
	private boolean isShipping;

	public static AddressBuilder anAddress() {
		return new AddressBuilder();
	}

	public static Address anAddressAtUniversity() {
		// @formatter:off
		// @formatter:off
		return anAddress()
				.onStreet("Argentinierstrasse")
				.withHouseNr(8)
				.withDoorNr(1)
				.withZipCode("1040")
				.inCity(CITY).build();
		// @formatter:on
	}

	public static Address aShippingAddressA() {
		return anAddress().withAddressId(SHIPPING_ID_A).shippable().build();
	}

	public static Address aBillingAddressB() {
		return anAddress().withAddressId(SHIPPING_ID_B).asBillingAddress().build();
	}

	public AddressBuilder withAddressId(String id) {
		this.id = id;
		return this;
	}

	public AddressBuilder onStreet(String street) {
		this.street = street;
		return this;
	}

	public AddressBuilder withHouseNr(int houseNr) {
		this.houseNr = houseNr;
		return this;
	}

	public AddressBuilder withDoorNr(int doorNr) {
		this.door = doorNr;
		return this;
	}

	public AddressBuilder withZipCode(String zipCode) {
		this.zipCode = zipCode;
		return this;
	}

	public AddressBuilder inCity(String city) {
		this.city = city;
		return this;
	}

	public AddressBuilder shippable() {
		this.isShipping = true;
		return this;
	}

	public AddressBuilder asBillingAddress() {
		this.isBilling = true;
		return this;
	}

	public AddressBuilder asOther() {
		this.isOther = true;
		return this;
	}

	public Address build() {
		return new Address(id, street, city, houseNr, door, zipCode, isShipping, isBilling, isOther);
	}
}
