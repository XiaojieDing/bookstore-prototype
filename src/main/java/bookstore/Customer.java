package bookstore;

import static java.text.MessageFormat.format;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Customer {
	@XmlAttribute
	private String id;
	private String name;
	private BigDecimal openBalance = new BigDecimal(0);

	@XmlElementWrapper
	@XmlElements(@XmlElement(name = "address"))
	private List<Address> addresses = new ArrayList<Address>();

	@XmlElementWrapper
	@XmlElements(@XmlElement(name = "order"))
	private List<Order> orders = new ArrayList<Order>();
	private String message;

	@SuppressWarnings("unused")
	private Customer() {
		// Needed by Apache CXF.
	}

	public Customer(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public void addAddress(Address address) {
		addresses.add(address);
	}

	public void addOrder(Order order) {
		orders.add(order);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public BigDecimal getOpenBalance() {
		return openBalance;
	}

	public void setOpenBalance(BigDecimal openBalance) {
		this.openBalance = openBalance;
	}

	public Address getShippingAddress() {
		for (Address each : addresses) {
			if (each.isShipping()) {
				return each;
			}
		}
		throw new RuntimeException("No Shipping Address found for customer: " + this);
	}

	public void notify(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return format("Name: {0}, ID: {1}, Open-Balance: {2}", name, id, openBalance);
	}
}
