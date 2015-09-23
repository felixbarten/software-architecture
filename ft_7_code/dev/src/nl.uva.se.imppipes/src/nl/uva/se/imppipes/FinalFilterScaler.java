package nl.uva.se.imppipes;

import java.util.List;

/**
 * This class is barely a wrapper for a <code>FinalFilter</code> so it can be chained after a list
 * of <code>FilterScaler</code>s.
 * 
 * @author FT_7
 * @param <O> the type of <code>Object</code>s processed by the <code>FinalFilter</code> wrapped in
 * 				<code>this FinalFilterScaler</code>
 * @see FinalFilter
 */
public class FinalFilterScaler<O> extends FilterScaler<O,O> {
	
	/**
	 * Constructor for <code>Object</code>s of class <code>FinalFilterScaler</code>. Creates a new
	 * <code>FinalFilter</code> collecting <code>Object</code>s of type <code>O</code> to collect
	 * <code>Object</code>s added to the queue of <code>this FinalFilterScaler</code>.
	 */
	public FinalFilterScaler() {
		super(new FinalFilter<O>(),null);
	}
	
	/**
	 * @return a <code>List</code> of the <code>Object</code>s collected the
	 *			<code>FinalFilter</code> wrapped in <code>this FinalFilterScaler</code>
	 */
	public List<O> getResult() {
		FinalFilter<O> filter = (FinalFilter<O>) getFirstFilter();
		return filter.getOutput();
	}
	
	/**
	 * Add an <code>Object</code> to the <code>queue</code> of <code>this FinalFilterScaler</code>,
	 * without possibly causing <code>this FinalFilterScaler</code> to clone the
	 * <code>Filter</code> wrapped in it like {@link FilterScaler#add(Object) add(Object)} could do.
	 * Calls {@link FilterScaler#addWithoutScaling(Object)} instead of
	 * {@link FilterScaler#add(Object)}.
	 */
	@Override
	public void add(O input) {
		addWithoutScaling(input);
	}
	
	/**
	 * Clear the List of <code>Filter</code>s wrapped in <code>this FinalFillterScaler</code>.
	 */
	public void reset() {
		filters.clear();
	}
}