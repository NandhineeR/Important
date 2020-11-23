package com.qdm.cg.clients.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ClientUtil {


	public static String getDateTimewithZone(LocalDateTime ldt) {
		if (null != ldt) {
			ZonedDateTime ldtZoned = ldt.atZone(ZoneId.systemDefault());

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			return ldtZoned.format(formatter);
		} else {
			return "";
		}

	}

}
