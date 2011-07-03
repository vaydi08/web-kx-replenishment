package com.sol.kx.replenishment.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
	private static final DateFormat df = new SimpleDateFormat("yyyy / MM / dd");
	
	public static String getDate() {
		return df.format(new Date());
	}
}
