package test.bookstore.services;

import static test.endtoend.bookstore.builder.CustomerBuilder.aCustomer;

import java.math.BigDecimal;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import bookstore.BookstoreLibrary;
import bookstore.Customer;
import bookstore.CustomerManagement;
import bookstore.InformationReporter;
import bookstore.NotificationMessage;
import bookstore.services.CustomerManagementJaxRS;

public class CustomerManagementJaxRSTest {
	private static final BigDecimal NEW_BALANCE = new BigDecimal(3);
	private static final String CUSTOMER_ID = "279cedd6-ba3b-4a30-a63e-8e54f28f0037";
	private static final NotificationMessage MESSAGE = new NotificationMessage("Items successfully shipped.");

	@Rule
	public JUnitRuleMockery context = new JUnitRuleMockery();
	@Mock
	private BookstoreLibrary library;
	@Mock
	private InformationReporter reporter;

	private CustomerManagement customerService;

	@Before
	public void createNewCustomerManagementService() {
		customerService = new CustomerManagementJaxRS(library, reporter);
	}

	@Test
	public void reportNotificationAboutCustomerRequest() {
		final Customer aCustomer = aCustomer().withId(CUSTOMER_ID).build();

		// @formatter:off
		context.checking(new Expectations() {{
			oneOf(library).getCustomer(CUSTOMER_ID); will(returnValue(aCustomer));
			oneOf(reporter).notifyGetCustomerRequest(CUSTOMER_ID, aCustomer);
		}});
		// @formatter:on

		customerService.getCustomer(CUSTOMER_ID);
	}

	@Test
	public void reportNotificationAboutAccountUpdateOfCustomer() {
		final Customer aCustomer = aCustomer().withId(CUSTOMER_ID).build();

		// @formatter:off
		context.checking(new Expectations() {{
			oneOf(library).getCustomer(CUSTOMER_ID); will(returnValue(aCustomer));
			oneOf(reporter).notifyUpdateOfCustomersAccount(aCustomer, NEW_BALANCE);
			oneOf(library).updateAccount(aCustomer, NEW_BALANCE);
		}});
		// @formatter:on

		customerService.updateAccount(CUSTOMER_ID, NEW_BALANCE);
	}

	@Test
	public void reportThatCustomerReceivesANotificationMessage() {
		final Customer aCustomer = aCustomer().withId(CUSTOMER_ID).build();

		// @formatter:off
		context.checking(new Expectations() {{
			oneOf(library).getCustomer(CUSTOMER_ID); will(returnValue(aCustomer));
			oneOf(reporter).notifyThatCustomerReceivesANotificationMessage(aCustomer, MESSAGE.getMessage());
		}});
		// @formatter:on

		customerService.notify(aCustomer.getId(), MESSAGE);
	}
}
