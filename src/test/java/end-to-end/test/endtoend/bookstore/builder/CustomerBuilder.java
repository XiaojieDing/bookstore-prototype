package test.endtoend.bookstore.builder;

import static test.endtoend.bookstore.builder.AddressBuilder.aBillingAddressB;
import static test.endtoend.bookstore.builder.AddressBuilder.aShippingAddressA;
import static test.endtoend.bookstore.builder.AddressBuilder.anAddress;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import bookstore.Address;
import bookstore.Customer;
import bookstore.Order;

public class CustomerBuilder {
	public static final Address CUSTOMER_SHIPPING_ADDRESS = aShippingAddressA();
	public static final String CUSTOMER_ID = "279cedd6-ba3b-4a30-a63e-8e54f28f0037";
	public static final String CUSTOMER_NAME = "MiniMe";
	public static final String CUSTOMER_WITH_UNKNOWN_ADDRESS = "123decc2-bb2c-4a30-a63e-4v13e25r0046";

	private static final String UNKNOWN_ADDRESS = "Nordpol";

	private String id = CUSTOMER_ID;
	private String name = CUSTOMER_NAME;
	private BigDecimal openBalance = new BigDecimal(0);
	private List<Address> addresses = new ArrayList<Address>();
	private List<Order> orders = new ArrayList<Order>();

	public static CustomerBuilder aCustomer() {
		return new CustomerBuilder();
	}

	public static Customer aCustomerWithAddressesAndOpenBalanceOfFive() {
		//@formatter:off
		return aCustomer()
				.withOpenBalance(new BigDecimal(5))
				.withAddress(aShippingAddressA())
				.withAddress(aBillingAddressB()).build();
		//@formatter:on
	}

	public static Customer aCustomerWithUnknownShippingAddress() {
		//@formatter:off
		return aCustomer()
				.withId(CUSTOMER_WITH_UNKNOWN_ADDRESS)
				.withAddress(anAddress().withAddressId(UNKNOWN_ADDRESS).shippable().build())
				.build();
		//@formatter:on
	}

	public CustomerBuilder withId(String id) {
		this.id = id;
		return this;
	}

	public CustomerBuilder withName(String name) {
		this.name = name;
		return this;
	}

	public CustomerBuilder withOpenBalance(BigDecimal openBalance) {
		this.openBalance = openBalance;
		return this;
	}

	public CustomerBuilder withAddress(Address address) {
		addresses.add(address);
		return this;
	}

	public CustomerBuilder withOrder(Order order) {
		orders.add(order);
		return this;
	}

	public Customer build() {
		Customer customer = new Customer(id, name);
		customer.setOpenBalance(openBalance);
		for (Address each : addresses) {
			customer.addAddress(each);
		}
		for (Order each : orders) {
			customer.addOrder(each);
		}
		return customer;
	}

}
