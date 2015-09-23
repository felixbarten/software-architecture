package nl.uva.se.funcpipes;

import java.util.Collection;

/**
 * Interface which signals a component which accepts input.
 * 
 * @author Floris den Heijer
 *
 * @param <T> input type
 */
public interface Feedable<T> {
	void feed(T obj);
	void feed(Collection<T> collection);
}
