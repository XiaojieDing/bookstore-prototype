package bookstore;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

// @formatter:off
@WebService(name = "Bookstore",
			targetNamespace = "http://infosys.tuwien.ac.at/aic10/ass1/dto/b")
public interface Bookstore {
// @formatter:on

	@WebMethod(operationName = "request_order")
	void requestOrder(@WebParam(name = "order") Order order);

}
