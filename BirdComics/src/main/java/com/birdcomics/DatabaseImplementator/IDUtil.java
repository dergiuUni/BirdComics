package com.birdcomics.DatabaseImplementator;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IDUtil {

	public static String generateTransId() {
		String tId = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		tId = sdf.format(new Date());
		tId = "T" + tId;

		return tId;
	}
}
