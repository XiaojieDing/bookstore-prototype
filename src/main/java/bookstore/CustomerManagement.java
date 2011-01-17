package bookstore;

import java.math.BigDecimal;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

//@formatter:off
@WebService(name = "CustomerManagement",
			targetNamespace = "http://infosys.tuwien.ac.at/aic10/ass1/dto/customermanagement")
public interface CustomerManagement {
//@formatter:on

	@WebMethod(operationName = "create_customer")
	Customer createCustomer(@WebParam(name = "customer") Customer customer);

	@WebMethod(operationName = "delete_customer")
	void deleteCustomer(@WebParam(name = "id") String id);

	@WebMethod(operationName = "get_customer")
	Customer getCustomer(@WebParam(name = "id") String id);

	@WebMethod(operationName = "update_customer")
	void updateCustomer(@WebParam(name = "customer") Customer customer);

	@WebMethod(operationName = "update_account")
	void updateAccount(@WebParam(name = "id") String id, @WebParam(name = "balance") BigDecimal balance);

	@WebMethod(operationName = "notify_customer")
	void notify(@WebParam(name = "id") String id, @WebParam(name = "message") NotificationMessage message);

}
