package com.uncle.loader.utils;

import java.util.HashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

/**
 * @author 杨戬
 * @className SystemPropertyUtils
 * @email uncle.yeung.bo@gmail.com
 * @date 2019/5/31 10:20
 */
public class SystemPropertyUtils {
    public static final String PLACEHOLDER_PREFIX = "${";
    public static final String PLACEHOLDER_SUFFIX = "}";
    public static final String VALUE_SEPARATOR = ":";
    private static final String SIMPLE_PREFIX = "${".substring(1);

    public static String resolvePlaceholders(String text)
    {
        if (text == null) {
            return text;
        }
        return parseStringValue(null, text, text, new HashSet());
    }

    public static String resolvePlaceholders(Properties properties, String text)
    {
        if (text == null) {
            return text;
        }
        return parseStringValue(properties, text, text, new HashSet());
    }

    private static String parseStringValue(Properties properties, String value, String current, Set<String> visitedPlaceholders)
    {
        StringBuilder buf = new StringBuilder(current);

        int startIndex = current.indexOf("${");
        while (startIndex != -1)
        {
            int endIndex = findPlaceholderEndIndex(buf, startIndex);
            if (endIndex != -1)
            {
                String placeholder = buf.substring(startIndex + "${".length(), endIndex);
                String originalPlaceholder = placeholder;
                if (!visitedPlaceholders.add(originalPlaceholder)) {
                    throw new IllegalArgumentException("Circular placeholder reference '" + originalPlaceholder + "' in property definitions");
                }
                placeholder = parseStringValue(properties, value, placeholder, visitedPlaceholders);

                String propVal = resolvePlaceholder(properties, value, placeholder);
                if ((propVal == null) && (":" != null))
                {
                    int separatorIndex = placeholder.indexOf(":");
                    if (separatorIndex != -1)
                    {
                        String actualPlaceholder = placeholder.substring(0, separatorIndex);

                        String defaultValue = placeholder.substring(separatorIndex + ":".length());
                        propVal = resolvePlaceholder(properties, value, actualPlaceholder);
                        if (propVal == null) {
                            propVal = defaultValue;
                        }
                    }
                }
                if (propVal != null)
                {
                    propVal = parseStringValue(properties, value, propVal, visitedPlaceholders);

                    buf.replace(startIndex, endIndex + "}".length(), propVal);

                    startIndex = buf.indexOf("${", startIndex + propVal
                            .length());
                }
                else
                {
                    startIndex = buf.indexOf("${", endIndex + "}"
                            .length());
                }
                visitedPlaceholders.remove(originalPlaceholder);
            }
            else
            {
                startIndex = -1;
            }
        }
        return buf.toString();
    }

    private static String resolvePlaceholder(Properties properties, String text, String placeholderName)
    {
        String propVal = getProperty(placeholderName, null, text);
        if (propVal != null) {
            return propVal;
        }
        return properties != null ? properties.getProperty(placeholderName) : null;
    }

    public static String getProperty(String key)
    {
        return getProperty(key, null, "");
    }

    public static String getProperty(String key, String defaultValue)
    {
        return getProperty(key, defaultValue, "");
    }

    public static String getProperty(String key, String defaultValue, String text)
    {
        try
        {
            String propVal = System.getProperty(key);
            if (propVal == null) {
                propVal = System.getenv(key);
            }
            if (propVal == null) {
                propVal = System.getenv(key.replace('.', '_'));
            }
            if (propVal == null) {
                propVal = System.getenv(key.toUpperCase(Locale.ENGLISH).replace('.', '_'));
            }
            if (propVal != null) {
                return propVal;
            }
        }
        catch (Throwable ex)
        {
            System.err.println("Could not resolve key '" + key + "' in '" + text + "' as system property or in environment: " + ex);
        }
        return defaultValue;
    }

    private static int findPlaceholderEndIndex(CharSequence buf, int startIndex)
    {
        int index = startIndex + "${".length();
        int withinNestedPlaceholder = 0;
        while (index < buf.length()) {
            if (substringMatch(buf, index, "}"))
            {
                if (withinNestedPlaceholder > 0)
                {
                    withinNestedPlaceholder--;
                    index += "}".length();
                }
                else
                {
                    return index;
                }
            }
            else if (substringMatch(buf, index, SIMPLE_PREFIX))
            {
                withinNestedPlaceholder++;
                index += SIMPLE_PREFIX.length();
            }
            else
            {
                index++;
            }
        }
        return -1;
    }

    private static boolean substringMatch(CharSequence str, int index, CharSequence substring)
    {
        for (int j = 0; j < substring.length(); j++)
        {
            int i = index + j;
            if ((i >= str.length()) || (str.charAt(i) != substring.charAt(j))) {
                return false;
            }
        }
        return true;
    }
}

