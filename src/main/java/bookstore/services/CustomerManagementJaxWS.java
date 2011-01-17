package bookstore.services;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.math.BigDecimal;

import javax.jws.WebService;

import org.apache.cxf.jaxrs.client.WebClient;

import bookstore.Customer;
import bookstore.CustomerManagement;
import bookstore.MySoapFault;
import bookstore.NotificationMessage;
import bookstore.UnknownProductFault;

// @formatter:off
@WebService(endpointInterface="bookstore.CustomerManagement",
			serviceName = "CustomerManagementServiceJaxWs",
        	targetNamespace = "http://infosys.tuwien.ac.at/aic10/ass1/dto/customermanagement",
        	portName = "CustomerManagementPT")
public class CustomerManagementJaxWS implements CustomerManagement {
// @formatter:on

	private static final String JAX_RS_MAIN_PATH = "customerservice/customers/";
	private static final String SERVICE_URL = "http://localhost:9000/";

	@Override
	public Customer createCustomer(Customer customer) {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public void deleteCustomer(String id) {
		throw new RuntimeException("Not implemented yet");
	}

	// @formatter:off
	@Override
	public Customer getCustomer(String id) {
		return webClient()
				.type(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.path(JAX_RS_MAIN_PATH + id)
				.get(Customer.class);
	}

	private WebClient webClient() {
		return WebClient.create(SERVICE_URL);
	}

	@Override
	public void updateAccount(String id, BigDecimal balance) {
		webClient()
			.type(APPLICATION_JSON)
			.path(JAX_RS_MAIN_PATH + id + "/account/")
			.put(balance);
	}

	@Override
	public void updateCustomer(Customer customer) {
		webClient()
			.type(APPLICATION_JSON)
			.path(JAX_RS_MAIN_PATH)
			.put(customer);
	}

	@Override
	public void notify(String customerId, NotificationMessage message) {
	
		webClient()
		.type(APPLICATION_JSON)
		.path(JAX_RS_MAIN_PATH + customerId + "/notification")
		.put(message); 
		
		if(getCustomer(customerId)== null)
		{
			throw new MySoapFault(message.getMessage());
		} 
	 
	}
	// @formatter:on
}
