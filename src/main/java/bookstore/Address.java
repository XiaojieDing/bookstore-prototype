package bookstore;

import java.text.MessageFormat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Address {
	@XmlAttribute
	private String id;
	private String street;
	private String city;
	private String zipCode;
	private int house;
	private int door;
	private boolean isShipping;
	private boolean isBilling;
	private boolean isOther;

	@SuppressWarnings("unused")
	private Address() {
		// Needed by Apache CXF.
	}

	public Address(String id, String street, String city, int house, int door, String zipCode, boolean isShipping, boolean isBilling, boolean isOther) {
		this.id = id;
		this.street = street;
		this.city = city;
		this.house = house;
		this.door = door;
		this.zipCode = zipCode;
		this.isShipping = isShipping;
		this.isBilling = isBilling;
		this.isOther = isOther;
	}

	public void shippable() {
		this.isShipping = true;
	}

	public void notShippable() {
		this.isShipping = false;
	}

	public void isBillingAddress() {
		this.isBilling = true;
	}

	public void noBillingAddress() {
		this.isBilling = false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public int getHouse() {
		return house;
	}

	public void setHouse(int house) {
		this.house = house;
	}

	public int getDoor() {
		return door;
	}

	public void setDoor(int door) {
		this.door = door;
	}

	public boolean isShipping() {
		return isShipping;
	}

	public void setShipping(boolean isShipping) {
		this.isShipping = isShipping;
	}

	public boolean isBilling() {
		return isBilling;
	}

	public void setBilling(boolean isBilling) {
		this.isBilling = isBilling;
	}

	public boolean isOther() {
		return isOther;
	}

	public void setOther(boolean isOther) {
		this.isOther = isOther;
	}

	@Override
	public String toString() {
		return MessageFormat.format("{0} {1}/{2}, {3} {4}", street, house, door, zipCode, city);
	}

}
