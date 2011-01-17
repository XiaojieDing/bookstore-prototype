package bookstore.services;

import java.math.BigDecimal;

import javax.jws.WebService;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.xmlsoap.schemas.ws._2004._08.addressing.EndpointReferenceType;

import bookstore.Product;
import bookstore.Supplier;
import bookstore.SupplierRegistry;

// @formatter:off
@WebService( 
	    	serviceName = "SupplierFacade",
	        targetNamespace = "http://infosys.tuwien.ac.at/aic10/ass1/dto/supplier",
	        portName = "SupplierPT")
public class SupplierFacadeJaxWs implements Supplier {
// @formatter:on
	private SupplierRegistry registry;

	public SupplierFacadeJaxWs(SupplierRegistry registry) {
		this.registry = registry;
	}

	@Override
	public BigDecimal order(Product aProduct, int amount) {
		EndpointReferenceType endPoint = registry.getAddressFromSupplierFor(aProduct);
		Supplier supplier = createSupplierProxyFor(endPoint.getAddress().getValue());
		return supplier.order(aProduct, amount);
	}

	// Public for testing purposes. Would be great to extract generic
	// SOAP-Service.
	public Supplier createSupplierProxyFor(String serviceAddress) {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(Supplier.class);
		factory.setAddress(serviceAddress);
		return (Supplier) factory.create();
	}
}
