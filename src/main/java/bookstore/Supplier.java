package bookstore;

import java.math.BigDecimal;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

// @formatter:off
@WebService(name = "Supplier",
			targetNamespace = "http://infosys.tuwien.ac.at/aic10/ass1/dto/supplier") 
 
public interface Supplier {
// @formatter:on

	/** @return total price. */
	@WebMethod(operationName = "order")
	BigDecimal order(@WebParam(name = "product") Product product, @WebParam(name = "amount") int amount);

}
