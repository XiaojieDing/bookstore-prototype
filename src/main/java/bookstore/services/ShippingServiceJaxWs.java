package bookstore.services;

import java.util.UUID;

import javax.jws.WebService;

import bookstore.Address;
import bookstore.BookstoreLibrary;
import bookstore.InformationReporter;
import bookstore.Item;
import bookstore.ShippingService;
import bookstore.UnknownAddressFault;

// @formatter:off
@WebService(endpointInterface = "bookstore.ShippingService",
			serviceName = "ShippingService",
			portName = "ShippingPT",
			targetNamespace = "http://infosys.tuwien.ac.at/aic10/ass1/dto/shipping")
public class ShippingServiceJaxWs implements ShippingService {
// @formatter:on

	public static final String SHIPPING_ADDRESS_UNKNOWN = "Shipping address unknown";

	private BookstoreLibrary library;
	private InformationReporter reporter;

	public ShippingServiceJaxWs(BookstoreLibrary library, InformationReporter reporter) {
		this.library = library;
		this.reporter = reporter;
	}

	@Override
	public String shipItems(Item[] items, Address customerShippingAddress) {
		System.out.println(items[0].toString() + " " + customerShippingAddress.toString());
		if (notValid(customerShippingAddress)) {
			throw new UnknownAddressFault(SHIPPING_ADDRESS_UNKNOWN);
		}
		reporter.notifyShippingEvent(items, customerShippingAddress);
		return new UUID(4242L, 4242L).toString();
	}

	private boolean notValid(Address customerShippingAddress) {
		return !library.isValid(customerShippingAddress.getId());
	}

}
