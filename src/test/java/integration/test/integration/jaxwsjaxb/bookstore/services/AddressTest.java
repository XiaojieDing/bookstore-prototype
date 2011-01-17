package test.integration.jaxwsjaxb.bookstore.services;

import javax.jws.WebService;

import bookstore.Address;

@WebService
public interface AddressTest {
	Address testAddress(Address address);
}
