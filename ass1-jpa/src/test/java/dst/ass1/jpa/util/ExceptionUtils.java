package dst.ass1.jpa.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * ##########################
 * 
 * DO NOT CHANGE THIS CLASS!
 * 
 * ##########################
 * 
 * Contains selected utility methods from Apache Commons Lang.
 */
public class ExceptionUtils {
	/**
	 * The empty String {@code ""}.
	 */
	public static final String EMPTY = "";

	/**
	 * Gets a short message summarising the exception.
	 * <p>
	 * The message returned is of the form {ClassNameWithoutPackage}:
	 * {ThrowableMessage}
	 * 
	 * @param th
	 *            the throwable to get a message for, null returns empty string
	 * @return the message, non-null
	 */
	public static String getMessage(Throwable th) {
		if (th == null) {
			return "";
		}
		String clsName = getShortClassName(th, null);
		String msg = th.getMessage();
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		th.printStackTrace(pw);
		String trace = sw.toString(); // stack trace as a string
		return clsName + ": " + defaultString(msg) + "\nTrace: " + trace;
	}

	/**
	 * <p>
	 * Gets the class name minus the package name for an {@code Object}.
	 * </p>
	 * 
	 * @param object
	 *            the class to get the short name for, may be null
	 * @param valueIfNull
	 *            the value to return if null
	 * @return the class name of the object without the package name, or the
	 *         null value
	 */
	public static String getShortClassName(Object object, String valueIfNull) {
		if (object == null) {
			return valueIfNull;
		}
		return getShortClassName(object.getClass());
	}

	/**
	 * <p>
	 * Gets the class name minus the package name from a {@code Class}.
	 * </p>
	 * 
	 * <p>
	 * Consider using the Java 5 API {@link Class#getSimpleName()} instead. The
	 * one known difference is that this code will return {@code "Map.Entry"}
	 * while the {@code java.lang.Class} variant will simply return
	 * {@code "Entry"}.
	 * </p>
	 * 
	 * @param cls
	 *            the class to get the short name for.
	 * @return the class name without the package name or an empty string
	 */
	public static String getShortClassName(Class<?> cls) {
		if (cls == null) {
			return EMPTY;
		}
		return getShortClassName(cls.getName());
	}

	/**
	 * <p>
	 * Gets the class name minus the package name from a String.
	 * </p>
	 * 
	 * <p>
	 * The string passed in is assumed to be a class name - it is not checked.
	 * </p>
	 * 
	 * <p>
	 * Note that this method differs from Class.getSimpleName() in that this
	 * will return {@code "Map.Entry"} whilst the {@code java.lang.Class}
	 * variant will simply return {@code "Entry"}.
	 * </p>
	 * 
	 * @param className
	 *            the className to get the short name for
	 * @return the class name of the class without the package name or an empty
	 *         string
	 */
	public static String getShortClassName(String className) {
		if (className == null) {
			return EMPTY;
		}
		if (className.length() == 0) {
			return EMPTY;
		}

		return className;
	}

	/**
	 * <p>
	 * Returns either the passed in String, or if the String is {@code null}, an
	 * empty String ("").
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.defaultString(null)  = ""
	 * StringUtils.defaultString("")    = ""
	 * StringUtils.defaultString("bat") = "bat"
	 * </pre>
	 * 
	 * @see String#valueOf(Object)
	 * @param str
	 *            the String to check, may be null
	 * @return the passed in String, or the empty String if it was {@code null}
	 */
	public static String defaultString(String str) {
		return str == null ? EMPTY : str;
	}

}
