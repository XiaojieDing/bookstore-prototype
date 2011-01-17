package bookstore.services;

import java.math.BigDecimal;

import javax.jws.WebService;

import bookstore.BookstoreLibrary;
import bookstore.InformationReporter;
import bookstore.Product;
import bookstore.Supplier;
import bookstore.UnknownProductFault;

// @formatter:off
@WebService(endpointInterface = "bookstore.Supplier",
    	    serviceName = "AustriaSupplier",
            targetNamespace = "http://infosys.tuwien.ac.at/aic10/ass1/dto/supplier",
            portName = "SupplierPT")
public class AustriaSupplierJaxWs implements Supplier {
// @formatter:on
	private BookstoreLibrary library;
	private InformationReporter reporter;

	public AustriaSupplierJaxWs(BookstoreLibrary library, InformationReporter reporter) {
		this.library = library;
		this.reporter = reporter;
	}

	@Override
	public BigDecimal order(Product aProduct, int amount) {
		if (!library.isAvailableInAustria(aProduct, amount)) {
			throw new UnknownProductFault("Product: \"" + aProduct + "\" not available anymore.");
		}
		for (int i = 0; i < amount; i++) {
			library.getFromAustriaSupplier(aProduct.getId());
		}
		BigDecimal totalPrice = aProduct.getSingleUnitPrice().multiply(new BigDecimal(amount));
		reporter.notifyOrderRequestFromAustriaSupplier(aProduct, amount, totalPrice);
		return totalPrice;
	}
}
