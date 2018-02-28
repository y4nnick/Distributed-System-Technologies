package dst.ass2.ws.util;

import static org.junit.Assert.assertNotNull;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;
import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notEmpty;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.JMX;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;

import dst.ass2.ws.dto.StreamingDTO;

/**
 * Contains some miscellaneous utility methods.
 */
public final class MiscUtils {
	private MiscUtils() {
	}

	/**
	 * Returns the annotation of the given type declared on the supplied class.<br/>
	 * If no annotation was found, an {@link AssertionError} is thrown.
	 *
	 * @param clazz          the class to look for annotations on
	 * @param annotationType the annotation class to look for
	 * @return the annotation found
	 */
	public static <A extends Annotation> A getAnnotation(Class<?> clazz, Class<A> annotationType) {
		A annotation = findAnnotation(clazz, annotationType);
		assertNotNull(String.format("%s must be annotated with %s", clazz.getSimpleName(), annotationType.getSimpleName()), annotation);
		return annotation;
	}

	/**
	 * Returns the annotation of the given type declared on the supplied method.<br/>
	 * If no annotation was found, an {@link AssertionError} is thrown.
	 *
	 * @param method         the method to look for annotations on
	 * @param annotationType the annotation class to look for
	 * @return the annotation found
	 */
	public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType) {
		A annotation = findAnnotation(method, annotationType);
		assertNotNull(String.format("%s must be annotated with %s", method.getName(), annotationType.getSimpleName()), annotation);
		return annotation;
	}

	/**
	 * Returns a new proxy for a MBean such as a singleton session bean in the {@code dst} domain.
	 *
	 * @param clazz          the type of the bean to find
	 * @param interfaceClass the interface
	 * @return the proxy
	 * @throws javax.management.MalformedObjectNameException
	 *                             the string passed as a parameter does not have the right format
	 * @throws java.io.IOException if a valid MBeanServerConnection cannot be created, for instance because
	 *                             the connection to the remote MBean server has not yet been established
	 *                             (with the connect method), or it has been closed, or it has broken.
	 * @see javax.management.JMX#newMBeanProxy(javax.management.MBeanServerConnection, javax.management.ObjectName, Class, boolean)
	 */
	public static <I, B extends I> I getMBean(JMXConnector jmxc, Class<B> clazz, Class<I> interfaceClass) throws MalformedObjectNameException, IOException {
		ObjectName objectName = new ObjectName("dst:type=" + clazz.getName());
		return JMX.newMBeanProxy(jmxc.getMBeanServerConnection(), objectName, interfaceClass, true);
	}

	/**
	 * Filters the given streamings by time and sorts them in their natural order.
	 *
	 * @param streamingDTOs the streamings to filter
	 * @param time          the start time
	 * @return the streamings started after the given time
	 */
	public static List<StreamingDTO> filterStreamingDtos(List<StreamingDTO> streamingDTOs, long time) {
		if(streamingDTOs == null)
			return new LinkedList<StreamingDTO>();
		List<StreamingDTO> filtered = new ArrayList<StreamingDTO>();
		for (StreamingDTO streamingDto : streamingDTOs) {
			if (streamingDto.getStartDate().getTime() >= time) {
				filtered.add(streamingDto);
			}
		}
		Collections.sort(filtered, new Comparator<StreamingDTO>() {
			@Override
			public int compare(StreamingDTO o1, StreamingDTO o2) {
				return o1.getStartDate().compareTo(o2.getStartDate());
			}
		});
		return filtered;
	}

	/**
	 * Checks whether the given text contains all of the given regular expressions.
	 *
	 * @param text     the text to check
	 * @param patterns the regular expressions
	 * @return {@code true} if all regular expressions match, {@code false} otherwise.
	 */
	public static boolean matches(String text, String... patterns) {
		hasText(text);
		notEmpty(patterns);
		for(String pattern : patterns) {
			Pattern p = Pattern.compile(pattern, Pattern.MULTILINE | Pattern.DOTALL);
			Matcher m = p.matcher(text);
			if(!m.matches()) {
				return false;
			}
		}
		return true;
	}

}
