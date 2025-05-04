package org.medirec.medirec.frontend.controller;

import java.util.Locale;

//udrziava si aktualny jazyk
public class AppSettings {

	private static Locale currentLocale = Locale.getDefault();

	public static Locale getLocale() {
		return currentLocale;
	}

	public static void setLocale(Locale locale) {
		currentLocale = locale;
	}
}
