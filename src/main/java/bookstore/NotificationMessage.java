package bookstore;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class NotificationMessage {

	private String message; 

	public NotificationMessage() {
		// Needed by Apace CXF. 
	}

	public NotificationMessage(String message) {
		this.message = message; 
	} 

	public String getMessage() {
		return message;
	}
	 

}
