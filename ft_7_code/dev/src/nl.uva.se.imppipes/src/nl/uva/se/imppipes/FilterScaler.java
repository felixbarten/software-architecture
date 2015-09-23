package nl.uva.se.imppipes;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A <code>FilterScaler</code> has identical <code>Filter</code>s process values. The number of
 * <code>Filter</code>s used is increased whenever the number of elements in the <code>Queue</code>
 * the <code>Filter</code>s get their input from exceeds the number of <code>Filter</code>s.
 * @author FT_7
 *
 * @param <I> the type of the values put into the <code>Filter</code>s used by
 * 				<code>this FilterScaler</code>
 * @param <O> the type of the values the <code>Filter</code>s used by <code>this FilterScaler</code>
 * 				put into the next <code>FilterScaler</code>
 */
public class FilterScaler<I,O> {
	
	protected ArrayList<Filter<I,O>> filters;
	private FilterScaler<O,? extends Object> next;
	private volatile Queue<I> queue;
	
	/**
	 * Constructor for <code>Object</code>s of class <code>FilterScaler</code>.
	 * 
	 * @param firstFilter a <code>Filter</code> of the type <code>this FilterScaler</code> should
	 * 						use to process the <code>Object</code>s added to it's <code>queue</code>
	 * @param nextFilterScaler the <code>FilterScaler</code> the <code>Filter</code>s used by
	 * 							<code>this FilterScaler</code> should add the values they have
	 * 							processed to, for further processing. If no further processing is
	 * 							needed, <code>null</code> should be provided and
	 * 							<code>this FilterScaler</code> should be extended with a way to
	 * 							return the result, as is done in {@link FinalFilterScaler}
	 */
	public FilterScaler(Filter<I,O> firstFilter,
			FilterScaler<O,? extends Object> nextFilterScaler) {
		next = nextFilterScaler;
		queue = new ConcurrentLinkedQueue<>();
		filters = new ArrayList<>();
		firstFilter.setQueue(queue);
		firstFilter.setNext(nextFilterScaler);
		filters.add(firstFilter);
	}
	
	/**
	 * Starts the <code>Filter</code>s used by <code>this FilterScaler</code> and the
	 * <code>next FilterScaler</code> if one is present.
	 */
	public void start() {
		for (Filter<I,O> f : filters) {
			f.start();
		}
		if (next != null) {
			next.start();
		}
	}
	
	/**
	 * Adds a value to the <code>queue</code> the <code>Filter</code>s used by
	 * <code>this FilterScaler</code> get their input from and makes
	 * <code>this FilterScaler</code> start using an extra <code>Filter</code> if this addition
	 * makes the number of <code>Object</code>s in <code>this FilterScaler</code>'s
	 * <code>queue</code> exceed the number of <code>Filter</code>s it uses
	 * 
	 * @param input a value that should be processed by one of the <code>Filter</code>s used by
	 * 			<code>this FilterScaler</code>
	 * @see #scale()
	 */
	public void add(I input) {
		queue.add(input);
		if (queue.size() > filters.size()) {
			scale();
		}
	}
	
	/**
	 * Adds a value to the <code>queue</code> the <code>Filter</code>s used by
	 * <code>this FilterScaler</code> get their input form without possibly increasing the number
	 * of <code>Filter</code>s used. This method should be used instead of {@link #add(Object)}
	 * when the use is multiple <code>Filter</code>s (all running in different <code>Thread</code>s
	 * is a problem.
	 * 
	 * @param input
	 */
	protected void addWithoutScaling(I input) {
		queue.add(input);
	}
	
	/**
	 * Adds a <code>Filter</code> to the list of <code>filters</code> used by
	 * <code>this FilterScaler</code> and starts that <code>Filter</code>. The new
	 * <code>Filter</code> to use is created by calling {@link Filter#clone() clone()} on the
	 * <code>Filter</code> provided to the constructor of this class when creating
	 * <code>this FilterScaler</code>. It is therefore important the provided <code>Filter</code>
	 * has implemented {@link Filter#clone() clone{}} according to the specification in the
	 * documentation of <code>Filter</code>.
	 */
	private void scale() {
		@SuppressWarnings("unchecked")
		//The result of filters.get(index) is always an Filter<I,O> and can 'thus be casted to one
		Filter<I,O> newFilter = (Filter<I,O>) filters.get(0).clone();
		//Index doesn't matter because all Filters in filters are equal of the same Class
		//and use the same in- and output
		filters.add(newFilter);
		newFilter.start();
	}
	
	/**
	 * Stops all <code>Filter</code>s used by <code>this FilterScaler</code> by calling
	 * {@link FilterScaler#stopExecution()}, clears the list of <code>Filter</code>s used by
	 * <code>this FilterScaler</code> to delete references to them and stops the next
	 * <code>FilterScaler</code> if one is present.
	 * 
	 * @return <code>true</code> when all activities performed by this method have finished
	 */
	public boolean stopExecution() {
		for (Filter<I,O> f : filters) {
			//Return value is for waiting for filters to finish only and can thus be discarded
			f.stopExecution();
		}
		if (!(this instanceof FinalFilterScaler)) {
			filters.clear();
		}
		//Return value is for waiting for this scaler and following Scalers to finish only
		return (next != null) ? next.stopExecution() : true;
	}
	
	/**
	 * @return the <code>Filter</code> provided to the constructor of this class when constructing
	 * 			<code>this FilterScaler</code> or <code>null</code> if a call to
	 * 			{@link #stopExecution()} has removed the reference to that <code>Filter</code>
	 */
	protected Filter<I,O> getFirstFilter() {
		return filters.get(0);
	}
}