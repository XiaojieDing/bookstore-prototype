package bookstore.services;

import org.xmlsoap.schemas.ws._2004._08.addressing.AttributedURI;
import org.xmlsoap.schemas.ws._2004._08.addressing.EndpointReferenceType;

import bookstore.BookstoreLibrary;
import bookstore.InformationReporter;
import bookstore.Product;
import bookstore.SupplierRegistry;
import bookstore.UnknownProductFault;

public class SupplierRegistryJaxWs implements SupplierRegistry {

	private BookstoreLibrary library;
	private InformationReporter reporter;

	public SupplierRegistryJaxWs(BookstoreLibrary library, InformationReporter reporter) {
		this.library = library;
		this.reporter = reporter;
	}

	@Override
	public EndpointReferenceType getAddressFromSupplierFor(Product aProduct) {
		String address = library.getSupplierAddressFor(aProduct.getId());
		if (notValid(address)) {
			throw new UnknownProductFault("Product not available");
		}
		reporter.notifyGetSupplierRequest(aProduct, address);
		return anEndpointFor(address);
	}

	private boolean notValid(String address) {
		if (address == null || address.isEmpty()) {
			return true;
		}
		return false;
	}

	private EndpointReferenceType anEndpointFor(String address) {
		AttributedURI uri = new AttributedURI();
		uri.setValue(address);
		EndpointReferenceType endpoint = new EndpointReferenceType();
		endpoint.setAddress(uri);
		return endpoint;
	}
}
