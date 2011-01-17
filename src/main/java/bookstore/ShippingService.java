package bookstore;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

// @formatter:off
@WebService(name = "ShippingService",
			targetNamespace = "http://infosys.tuwien.ac.at/aic10/ass1/dto/shipping")
public interface ShippingService {
// @formatter:on

	@WebMethod(operationName = "ship_items")
	String shipItems(@WebParam(name = "items") Item[] items, @WebParam(name = "customer_shipping_address") Address customerShippingAddress);

}
