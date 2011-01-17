package bookstore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import bookstore.services.util.DateLongAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Order {
	@XmlAttribute
	private String id;
	private Customer customer;

	@XmlJavaTypeAdapter(DateLongAdapter.class)
	private Date orderDate;

	@XmlElementWrapper
	@XmlElements(@XmlElement(name = "item"))
	private List<Item> items = new ArrayList<Item>();

	@SuppressWarnings("unused")
	private Order() {
		// Needed by Apache CXF.
	}

	public Order(String id, Date orderDate, Customer customer) {
		this.id = id;
		this.orderDate = orderDate;
		this.customer = customer;
	}

	public void addItem(Item item) {
		items.add(item);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public String getCustomerId() {
		return customer.getId();
	}
}
