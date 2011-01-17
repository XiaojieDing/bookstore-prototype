package bookstore;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "productavailability")
public class ProductAvailability {

	private boolean isAvailable;
	private int estimatedDeliveryTime;

	public ProductAvailability() {
		// Needed by Apache CXF.
	}

	public ProductAvailability(boolean isAvailable, int estimatedDeliveryTime) {
		this.isAvailable = isAvailable;
		this.estimatedDeliveryTime = estimatedDeliveryTime;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public int getEstimatedDeliveryTime() {
		return estimatedDeliveryTime;
	}

}
