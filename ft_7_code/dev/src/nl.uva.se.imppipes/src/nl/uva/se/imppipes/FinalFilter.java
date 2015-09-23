package nl.uva.se.imppipes;

import java.util.ArrayList;
import java.util.List;

/**
 * A special type of <code>Filter</code> that collects the results it is told to process in a
 * <code>List</code> so they can be obtained.
 * 
 * @author FT_7
 * @param <O> the type of <code>Object</code>s processed by <code>this FinalFilter</code>
 */
public class FinalFilter<O> extends Filter<O,O> {
	
	private List<O> output;
	
	/**
	 * Constructor for objects of class <code>FilterScaler</code>.
	 */
	public FinalFilter() {
		super();
		output = new ArrayList<>();
	}
	
	/**
	 * Add a value to the <code>List</code> of values collected by <code>this FinalFilter</code>.
	 * @param input the value to store
	 * @return null, because this <code>Filter</code> should be the end of a chain of
	 * 			<code>Filter</code>s or <code>FilterScaler</code>s and no further processing should
	 * 			be needed
	 */
	@Override
	public O process(O input) {
		output.add(input);
		return null;
	}
	
	/**
	 * @throws UnsupportedOperationException because a <code>FinalFilter</code> should not be
	 * 											cloned, because usage in a
	 * 											<code>FilterScaler</code> that clones
	 * 											<code>this Filter</code> would cause the results to
	 * 											spread over the <code>List</code>s collected by
	 * 											these <code>Filter</code>s
	 */
	@Override
	public FinalFilter<O> clone() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @return a <code>List</code> of the values processed by <code>this FinalFilter</code>
	 */
	public List<O> getOutput() {
		return output;
	}
}