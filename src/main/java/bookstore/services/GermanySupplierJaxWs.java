package bookstore.services;

import java.math.BigDecimal;

import javax.jws.WebService;

import bookstore.BookstoreLibrary;
import bookstore.InformationReporter;
import bookstore.Product;
import bookstore.Supplier;
import bookstore.UnknownProductFault;

// @formatter:off
@WebService( 
	    	serviceName = "GermanySupplier",
	    	targetNamespace = "http://infosys.tuwien.ac.at/aic10/ass1/dto/supplier",
	    	portName = "SupplierPT")
public class GermanySupplierJaxWs implements Supplier {
// @formatter:on

	private BookstoreLibrary library;
	private InformationReporter reporter;

	public GermanySupplierJaxWs(BookstoreLibrary library, InformationReporter reporter) {
		this.library = library;
		this.reporter = reporter;
	}

	@Override
	public BigDecimal order(Product aProduct, int amount) {
		if (!library.isAvailableInGermany(aProduct, amount)) {
			throw new UnknownProductFault("Product not available");
		}
		for (int i = 0; i < amount; i++) {
			library.getFromGermanSupplier(aProduct.getId());
		}
		BigDecimal totalPrice = aProduct.getSingleUnitPrice().multiply(new BigDecimal(amount));
		reporter.notifyOrderRequestFromGermanSupplier(aProduct, amount, totalPrice);
		return totalPrice;
	}

}
