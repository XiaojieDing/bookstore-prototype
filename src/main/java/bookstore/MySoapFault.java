package bookstore;

import org.apache.cxf.binding.soap.SoapFault;

public class MySoapFault extends SoapFault {

	public MySoapFault(final String message) {
		super(message, FAULT_CODE_CLIENT);
	}

}
