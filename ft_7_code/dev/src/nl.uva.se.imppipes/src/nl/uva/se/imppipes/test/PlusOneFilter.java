package nl.uva.se.imppipes.test;

import nl.uva.se.imppipes.Filter;

/**
 * <code>Object</code>s of this class are simple <code>Filter</code>s, adding one to each
 * <code>Integer</code> they are told to process. This <code>Filter</code> is usefull for testing
 * only because the overhead of the filtering processes makes using a <code>Filter</code> an
 * inefficient way of performing a simple task, like adding one, on each element of a
 * <code>List</code> of elements.
 * 
 * @author FT_7
 */
public class PlusOneFilter extends Filter<Integer,Integer> {
	
	/**
	 * @param input the <code>Object</code> to process, that is, the <code>Integer</code> to add
	 * 			one to
	 * @return input+1
	 */
	@Override
	public Integer process(Integer input) {
		return input+1;
	}
	
	/**
	 * @return a new <code>PlusOneFilter</code> processing elements of the same <code>queue</code>
	 * 			and adding the results to the queue of the same <code>FilterScaler</code> as
	 * 			<code>this PlusOneFilter</code>
	 */
	@Override
	public PlusOneFilter clone() {
		PlusOneFilter clone = new PlusOneFilter();
		//Return value is for synchronization only
		copyNextAndQueue(this,clone);
		return clone;
	}
}