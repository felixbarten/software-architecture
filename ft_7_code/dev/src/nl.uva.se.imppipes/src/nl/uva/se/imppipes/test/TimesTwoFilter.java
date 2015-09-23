package nl.uva.se.imppipes.test;

import nl.uva.se.imppipes.Filter;

/**
 * <code>Object</code>s of this class are simple <code>Filter</code>s, multiplying each
 * <code>Integer</code> they are told to process by two. This <code>Filter</code> is usefull for
 * testing only because the overhead of the filtering processes makes using a <code>Filter</code>
 * an inefficient way of performing a simple task, like multiplying by two, on each element of a
 * <code>List</code> of elements.
 * 
 * @author FT_7
 */
public class TimesTwoFilter extends Filter<Integer,Integer> {
	
	/**
	 * @return a new <code>TimesTwoFilter</code> processing elements of the same <code>queue</code>
	 * 			and adding the results to the queue of the same <code>FilterScaler</code> as
	 * 			<code>this TimesTwoFilter</code>
	 */
	@Override
	public TimesTwoFilter clone() {
		TimesTwoFilter clone = new TimesTwoFilter();
		copyNextAndQueue(this,clone);
		return clone;
	}
	
	/**
	 * @param input the <code>Object</code> to process, that is, the <code>Integer</code> to multiply by two
	 * @return input*2
	 */
	public Integer process(Integer input) {
		return input*2;
	}
}