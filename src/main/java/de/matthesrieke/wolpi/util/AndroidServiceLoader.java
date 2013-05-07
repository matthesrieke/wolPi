package de.matthesrieke.wolpi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Android variant of the famous java.util.ServiceLoader.
 * Services description files are read by default from
 * {@value #DEFAULT_BASE_LOCATION}. 
 * 
 * @author matthes rieke
 *
 * @param <S> the interface for which implementations are loaded.
 */
public final class AndroidServiceLoader<S> {

	public static final String DEFAULT_BASE_LOCATION = "AndroidServiceLoader/services/";

	private ClassLoader classLoaderInstance;
	private Class<S> theInterface;

	private String baseLocation;

	private Collection<S> cached;

	private AndroidServiceLoader(Class<S> ntrfce, String location,
			ClassLoader loader) {
		theInterface = ntrfce;
		classLoaderInstance = loader;
		baseLocation = location;
	}

	/**
	 * @return the loaded interface implementations
	 */
	public Collection<S> implementations() {
		if (cached == null) {
			cached = loadImplementations();
		}
		return cached;
	}

	private Collection<S> loadImplementations() {
		Collection<S> result = new ArrayList<S>();

		URL baseUrl = classLoaderInstance.getResource(baseLocation+theInterface.getCanonicalName());

		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(baseUrl.openStream(),
					"UTF-8"));
			while (br.ready()) {
				parseLine(br.readLine(), result);
			}
		} catch (IOException x) {
			throw new RuntimeException(x);
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException y) {
				throw new RuntimeException("Error closing file stream:"
						+ baseUrl);
			}
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	private void parseLine(String line, Collection<S> result) {
		if (line == null)
			return;
		
		String tmp = line.trim();
		
		if (tmp.startsWith("#"))
			return;
		
		try {
			Class<?> clazz = Class.forName(tmp);
			if (theInterface.isAssignableFrom(clazz)) {
				result.add((S) clazz.newInstance());
			}
		} catch (ClassNotFoundException e) {
			return;
		} catch (InstantiationException e) {
			return;
		} catch (IllegalAccessException e) {
			return;
		}
		
	}

	/**
	 * Services description files are read by default from
	 * {@value #DEFAULT_BASE_LOCATION}. 
	 * 
	 * @param service the interface
	 * @return the object providing access to the implementations
	 */
	public static <S> AndroidServiceLoader<S> load(Class<S> service) {
		return load(service, DEFAULT_BASE_LOCATION);
	}

	/**
	 * Services description files are read from
	 * baseLocation parameter. 
	 * 
	 * @param service the interface
	 * @param baseLocation the directory for service description files
	 * @return the object providing access to the implementations
	 */
	public static <S> AndroidServiceLoader<S> load(Class<S> service,
			String baseLocation) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		return load(service, baseLocation, cl);
	}

	/**
	 * Services description files are read from
	 * baseLocation parameter by the defined loader.
	 * 
	 * @param service the interface
	 * @param baseLocation the directory for service description files
	 * @param loader ClassLoader to use
	 * @return the object providing access to the implementations
	 */
	public static <S> AndroidServiceLoader<S> load(Class<S> service,
			String baseLocation, ClassLoader loader) {
		return new AndroidServiceLoader<S>(service, baseLocation, loader);
	}
	
}
