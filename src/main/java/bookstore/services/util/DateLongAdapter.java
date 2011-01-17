package bookstore.services.util;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateLongAdapter extends XmlAdapter<Long, Date> {

	@Override
	public Date unmarshal(final Long timemillis) {
		return new Date(timemillis);
	}

	@Override
	public Long marshal(final Date date) {
		return date.getTime();
	}

}
