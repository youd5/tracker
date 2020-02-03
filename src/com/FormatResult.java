package com;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;

public class FormatResult {
	
	public static void openInBrowser(String url) {
		try {
	
			URI uri = new URL(url).toURI();
			Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
			if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
				desktop.browse(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
