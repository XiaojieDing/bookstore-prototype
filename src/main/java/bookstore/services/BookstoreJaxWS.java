package bookstore.services;

import static java.text.MessageFormat.format;

import java.math.BigDecimal;

import javax.jws.WebService;

import bookstore.Bookstore;
import bookstore.Customer;
import bookstore.CustomerManagement;
import bookstore.InformationReporter;
import bookstore.Item;
import bookstore.NotificationMessage;
import bookstore.Order;
import bookstore.ProductAvailability;
import bookstore.ShippingService;
import bookstore.Supplier;
import bookstore.Warehouse;

// @formatter:off
@WebService(endpointInterface = "bookstore.Bookstore",
			serviceName = "BookstoreService",
			targetNamespace = "http://infosys.tuwien.ac.at/aic10/ass1/dto/bookstore",
			portName = "BookstorePT")
public class BookstoreJaxWS implements Bookstore {
// @formatter:on

	public static final String SUCCESS_MESSAGE = "Successfully shipped all items to address: \"{0}\"";

	private Order order;
	private Customer aCustomer;
	private BigDecimal totalPrice = new BigDecimal(0);
	private Warehouse warehouse;
	private CustomerManagement customerService;
	private ShippingService shippingService;
	private Supplier supplier;
	private InformationReporter reporter;

	public BookstoreJaxWS(CustomerManagement customerService, Warehouse warehouse, ShippingService shippingService, Supplier supplier, InformationReporter reporter) {
		this.customerService = customerService;
		this.warehouse = warehouse;
		this.shippingService = shippingService;
		this.supplier = supplier;
		this.reporter = reporter;
	}

	// @formatter:off
	@Override
	public void requestOrder(Order anOrder) {
		this.order = anOrder;
		reporter.notifyNewOrderRequest(anOrder);
		aCustomer = customerService.getCustomer(anOrder.getCustomerId());
		try {
			orderItemsOf(anOrder);
		} catch (RuntimeException orderError) {
			customerService.notify(aCustomer.getId(), toNotificationMessage(orderError.getMessage()));
			return;
		}
		try {
			shippingService.shipItems(itemsToArry(anOrder), aCustomer.getShippingAddress());
		} catch (RuntimeException shippingError) {
			customerService.notify(aCustomer.getId(), toNotificationMessage(shippingError.getMessage()));
			return;
		}
		customerService.updateAccount(aCustomer.getId(), newCustomerBalance());
		customerService.notify(aCustomer.getId(),
				               toNotificationMessage(format(SUCCESS_MESSAGE, aCustomer.getShippingAddress())));
	}
	// @formatter:on

	private void orderItemsOf(Order anOrder) {
		System.out.println("Orders"+ anOrder.toString());
		for (Item each : anOrder.getItems()) {
			order(each);
			System.out.println(each.toString());
		}
	}

	private void order(Item anItem) {
		if (availableInWarehouse(anItem)) {
			addToTotalPrice(orderFromWarehouse(anItem));
		} else {
			addToTotalPrice(orderFromSupplier(anItem));
		}
	}

	private BigDecimal orderFromWarehouse(Item anItem) {
		System.out.println(anItem.toString());
		return warehouse.order(anItem.getProduct(), anItem.getQuantity());
	}

	private void addToTotalPrice(BigDecimal price) {
		totalPrice = totalPrice.add(price);
	}

	private BigDecimal orderFromSupplier(Item anItem) {
		return supplier.order(anItem.getProduct(), anItem.getQuantity());
	}

	private boolean availableInWarehouse(Item anItem) {
		ProductAvailability item = warehouse.checkAvailability(anItem.getProduct(), anItem.getQuantity());
		return item.isAvailable();
	}

	private Item[] itemsToArry(Order order) {
		return order.getItems().toArray(new Item[] {});
	}

	private BigDecimal newCustomerBalance() {
		return aCustomer.getOpenBalance().subtract(totalPrice);
	}

	private NotificationMessage toNotificationMessage(String message) {
		return new NotificationMessage(message);
	}

	public Order getOrder() {
		return order;
	}

}
