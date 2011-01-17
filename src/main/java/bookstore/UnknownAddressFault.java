package bookstore;

import org.apache.cxf.binding.soap.SoapFault;

@SuppressWarnings("serial")
public class UnknownAddressFault extends SoapFault {

	public UnknownAddressFault(final String message) {
		super(message, FAULT_CODE_CLIENT);
	}

}
