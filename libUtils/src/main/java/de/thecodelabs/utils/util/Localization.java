package de.thecodelabs.utils.util;

import de.thecodelabs.utils.logger.LoggerBridge;

import java.text.MessageFormat;
import java.util.*;

/**
 * @author tobias
 */
public class Localization
{

	private static LocalizationDelegate delegate;

	private static List<ResourceBundle> bundles;

	/**
	 * @param delegate
	 */
	public static void setDelegate(LocalizationDelegate delegate)
	{
		Localization.delegate = delegate;
	}

	/**
	 *
	 */
	public static void load()
	{
		if(delegate == null)
		{
			throw new NullPointerException("Delegate is null. Use: Localization.setDelegate()");
		}
		bundles = new ArrayList<>();

		if(delegate.useMultipleResourceBundles())
		{
			for(String bundle : delegate.getBaseResources())
			{
				bundles.add(loadResourceBundle(bundle));
			}
		}
		else
		{
			final String baseResource = delegate.getBaseResource();
			if(baseResource == null)
			{
				LoggerBridge.debug("Resource bundle is null. Delegate Method might be not overwritten");
				return;
			}

			bundles.add(loadResourceBundle(baseResource));
		}
	}

	private static ResourceBundle loadResourceBundle(String base)
	{
		ResourceBundle bundle = loadBundle(base, Localization.class.getClassLoader());
		LoggerBridge.debug("Loaded localization bundle: " + bundle.getBaseBundleName() + " for language: " + bundle.getLocale());
		return bundle;
	}

	/**
	 * @param name
	 * @param loader
	 * @return
	 */
	public static ResourceBundle loadBundle(String name, ClassLoader loader)
	{
		Locale locale = delegate != null ? delegate.getLocale() : Locale.GERMAN;
		try
		{
			return ResourceBundle.getBundle(name, locale, loader);
		}
		catch(MissingResourceException e)
		{
			return ResourceBundle.getBundle(name, Locale.GERMAN, loader);
		}
	}

	/**
	 * @return
	 */
	public static ResourceBundle getBundle()
	{
		if(bundles.isEmpty())
		{
			throw new RuntimeException("No ResourceBundles available");
		}
		return bundles.get(0);
	}

	public static List<ResourceBundle> getBundles()
	{
		return bundles;
	}

	private static Optional<ResourceBundle> getResourceBundleForLocalKey(String key)
	{
		if(bundles == null)
		{
			LoggerBridge.debug("Localization is not initialized");
			return Optional.empty();
		}
		return bundles.stream().filter(resourceBundle -> resourceBundle.containsKey(key)).findAny();
	}

	/**
	 * @param key
	 * @return
	 */
	private static String getRawString(String key)
	{
		final Optional<ResourceBundle> bundleOptional = getResourceBundleForLocalKey(key);
		if(bundleOptional.isPresent())
		{
			ResourceBundle bundle = bundleOptional.get();
			return bundle.getString(key);
		}
		else
		{
			LoggerBridge.debug("ResourceKey not found: " + key);
			return key;
		}
	}

	/**
	 * @param message
	 * @param args
	 * @return
	 */
	private static String formatStringReplace(String message, Object... args)
	{
		int index = 0;
		while(message.contains("{}"))
		{
			if(args.length > index)
			{
				if(args[index] != null)
				{
					message = message.replaceFirst("\\{\\}", args[index].toString());
				}
				else
				{
					message = message.replaceFirst("\\{\\}", "null");
				}
				index++;
			}
			else
			{
				LoggerBridge.error("Args invalid: " + message);
				break;
			}
		}
		return message;
	}

	public static String getString(String key, Object... args)
	{
		final String message = getRawString(key);

		// Use old method
		if(!delegate.useMessageFormatter())
		{
			return formatStringReplace(message, args);
		}
		else
		{
			return MessageFormat.format(message, args);
		}
	}


	public interface LocalizationDelegate
	{

		Locale getLocale();

		default String getBaseResource()
		{
			return null;
		}

		default String[] getBaseResources()
		{
			return new String[]{};
		}

		default boolean useMultipleResourceBundles()
		{
			return false;
		}

		default boolean useMessageFormatter()
		{
			return false;
		}
	}
}
