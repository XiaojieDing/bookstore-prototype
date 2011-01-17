package bookstore;

import org.apache.cxf.binding.soap.SoapFault;

@SuppressWarnings("serial")
public class UnknownProductFault extends SoapFault {

	public UnknownProductFault(final String message) {
		super(message, FAULT_CODE_CLIENT);
	}

}
