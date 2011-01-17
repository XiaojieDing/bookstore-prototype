package bookstore;

import java.math.BigDecimal;
import java.text.MessageFormat;
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
public class Product {
	@XmlAttribute
	private String id;
	private String name;
	private BigDecimal singleUnitPrice;

	@XmlElementWrapper
	@XmlElements(@XmlElement(name = "item"))
	private List<Item> items = new ArrayList<Item>();

	@SuppressWarnings("unused")
	private Product() {
		// Needed by Apache CXF.
	}

	public Product(String id, String name, BigDecimal singleUnitPrice) {
		this.id = id;
		this.name = name;
		this.singleUnitPrice = singleUnitPrice;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getSingleUnitPrice() {
		return singleUnitPrice;
	}

	public void setSingleUnitPrice(BigDecimal singleUnitPrice) {
		this.singleUnitPrice = singleUnitPrice;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return MessageFormat.format("Product-id: \"{0}\", name: \"{1}\", single unit price: {2}", id, name, singleUnitPrice);
	}
}
