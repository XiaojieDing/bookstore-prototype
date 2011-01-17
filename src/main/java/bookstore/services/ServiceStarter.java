package bookstore.services;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Endpoint;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import bookstore.BookstoreLibrary;
import bookstore.CustomerManagement;
import bookstore.InformationReporter;
import bookstore.ShippingService;
import bookstore.Supplier;
import bookstore.SupplierRegistry;
import bookstore.Warehouse;

public class ServiceStarter {
	
	private Server jaxRsServer;
	private BookstoreLibrary library; 
	private InformationReporter reporter;
	private BookstoreJaxWS bookstoreService; 
	private List<Endpoint> endpoints = new ArrayList<Endpoint>();

	public static void main(String[] args) throws InterruptedException {
		new ServiceStarter().provideServices();
		System.out.println("Server ready..."); 
        
       /* Thread.sleep(5 * 60 * 1000); 
        System.out.println("Server exiting");
        System.exit(0);*/
	}

	private void provideServices() {
		// TODO Implement provideServices method in Service Starter.
		endpoints.add(Endpoint.publish("http://localhost:9000/warehouse", new WarehouseJaxWS(library, reporter)));
		endpoints.add(Endpoint.publish("http://localhost:9000/customermanagement", new CustomerManagementJaxWS()));
		endpoints.add(Endpoint.publish("http://localhost:9000/shipping", new ShippingServiceJaxWs(library, reporter)));
		endpoints.add(Endpoint.publish("http://localhost:9000/registry", new SupplierRegistryJaxWs(library, reporter)));
		endpoints.add(Endpoint.publish("http://localhost:9000/supplierfacade", new SupplierFacadeJaxWs(createRegistry())));
		endpoints.add(Endpoint.publish("http://localhost:9000/austriasupplier", new AustriaSupplierJaxWs(library, reporter)));
		endpoints.add(Endpoint.publish("http://localhost:9000/germansupplier", new GermanySupplierJaxWs(library, reporter)));

		bookstoreService = new BookstoreJaxWS(createCustomerService(), createWarehouse(), createShippingService(), createSupplierFacade(), reporter);
		endpoints.add(Endpoint.publish("http://localhost:9000/bookstore", bookstoreService));

		publishJaxRSCustomerManagementService(); 
	}
	private SupplierRegistry createRegistry() {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(SupplierRegistry.class);
		factory.setAddress("http://localhost:9000/registry");
		return (SupplierRegistry) factory.create();
	}

	private CustomerManagement createCustomerService() {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(CustomerManagement.class);
		factory.setAddress("http://localhost:9000/customermanagement");
		return (CustomerManagement) factory.create();
	}

	private Warehouse createWarehouse() {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(Warehouse.class);
		factory.setAddress("http://localhost:9000/warehouse");
		return (Warehouse) factory.create();
	}

	private ShippingService createShippingService() {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(ShippingService.class);
		factory.setAddress("http://localhost:9000/shipping");
		return (ShippingService) factory.create();
	}

	private Supplier createSupplierFacade() {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(Supplier.class);
		factory.setAddress("http://localhost:9000/supplierfacade");
		return (Supplier) factory.create();
	}

	private void publishJaxRSCustomerManagementService(){
		JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
		sf.setResourceClasses(CustomerManagementJaxRS.class);
		sf.setResourceProvider(CustomerManagementJaxRS.class, new SingletonResourceProvider(new CustomerManagementJaxRS(library, reporter)));
		sf.setAddress("http://localhost:9000/");
		jaxRsServer = sf.create();
	}

}
