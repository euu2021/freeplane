package org.freeplane.core.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JOptionPane;

import org.freeplane.core.controller.Controller;
import org.freeplane.core.ui.components.UITools;
import org.freeplane.plugin.macos.MacChanges;

/**
 * Provides methods and constants which are dependend on the underlying java version
 * 
 * @author robert.ladstaetter
 */
public class Compat {
	public static final String JAVA_VERSION = System.getProperty("java.version");
	
	public static final String VERSION_1_6_0 = "1.6.0";
	private static enum OS {MAC, WINDOWS, OTHER};
	private static OS os = null;


	public static URL fileToUrl(final File pFile) throws MalformedURLException {
		return pFile.toURL();
	}

	public static boolean isLowerJdk(final String version) {
		return JAVA_VERSION.compareTo(version) < 0;
	}

	public static boolean isMacOsX() {
		initOS();
		return os.equals(OS.MAC);
	}


	private static void initOS() {
	    if(os == null){
			final String osProperty = System.getProperty("os.name");
			if (osProperty.startsWith("Mac OS") || System.getProperty("debug.os.name", "").startsWith("Mac")){ 
				os = OS.MAC;
				return;
			}
			if (osProperty.startsWith("Windows") || System.getProperty("debug.os.name", "").startsWith("Windows")){ 
				os = OS.WINDOWS;
				return;
			}
			os = OS.OTHER;
		}
    }
	public static boolean isWindowsOS() {
		initOS();
		return os.equals(OS.WINDOWS);
    }

	/**
	 * This is a correction of a method getFile of a class URL. Namely, on
	 * Windows it returned file paths like /C: etc., which are not valid on
	 * Windows. This correction is heuristic to a great extend. One of the
	 * reasons is that file: something every browser and every system uses
	 * slightly differently.
	 */
	private static String urlGetFile(final URL url) {
		final String osNameStart = System.getProperty("os.name").substring(0, 3);
		if (osNameStart.equals("Win") && url.getProtocol().equals("file")) {
			final String fileName = url.toString().replaceFirst("^file:", "").replace('/', '\\');
			return (fileName.indexOf(':') >= 0) ? fileName.replaceFirst("^\\\\*", "") : fileName;
		}
		else {
			return url.getFile();
		}
	}

	public static File urlToFile(final URL pUrl) throws URISyntaxException {
		return new File(Compat.urlGetFile(pUrl));
	}

	public static void useScreenMenuBar() {
		/* This is only for apple but does not harm for the others. */
		//		if (isMacOsX()) 
		System.setProperty("apple.laf.useScreenMenuBar", "true");
	}

	public static void macChanges(Controller controller) {
		if(isMacOsX()){
			MacChanges.apply(controller);
		}
		
	}
}
