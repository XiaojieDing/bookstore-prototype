package bookstore;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.xmlsoap.schemas.ws._2004._08.addressing.EndpointReferenceType;

// @formatter:off
@WebService(name = "SupplierRegistry",
        	targetNamespace = "http://infosys.tuwien.ac.at/aic10/ass1/dto/supplierregistry")
public interface SupplierRegistry {
// @formatter:on

	public static final String GERMAN_SUPPLIER_ADDRESS = "http://localhost:9000/germansupplier";
	public static final String AUSTRIA_SUPPLIER_ADDRESS = "http://localhost:9000/austriasupplier";

	@WebMethod(operationName = "get_address_from_supplier")
	EndpointReferenceType getAddressFromSupplierFor(@WebParam(name = "product") Product aProduct);

}
