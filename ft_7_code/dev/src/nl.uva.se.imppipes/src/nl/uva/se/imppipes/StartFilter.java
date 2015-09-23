package nl.uva.se.imppipes;

import java.util.*;

/**
 * A special type of <code>Filter</code> that has all elements in a <code>List</code> processed by
 * a chain of <code>FilterScaler</code>s following <code>this StartFilter</code>.
 * 
 * @author FT_7
 * @param <I> the type of <code>Object</code>s in the <code>List</code> of elements
 * 				<code>this StartFilter</code> should have processed
 */
public class StartFilter<I> extends Filter<I,I> {
	
	private Queue<I> input;
	private Object monitor;
	private volatile boolean pipeStopped = false;
	
	/**
	 * Constructor for <code>Object</code>s of class <code>StartFilter</code>.
	 * 
	 * @param inputList the <code>List</code> of <code>Object</code>s to be processed by the
	 * 					<code>FilterScaler</code>s following <code>this StartFilter</code>
	 * @param next the <code>FilterScaler</code> which <code>queue</code>
	 * 				<code>this StartFilter</code> should add the elements of <code>inputList</code>
	 * 				to
	 * @param theMonitor an <code>Object</code> to synchronize stopping the chain of
	 * 						<code>FilterScaler</code>s following <code>this StartFilter</code> on
	 * 						when necessary. Before getting the result of filtering the elements of
	 * 						<code>inputList</code> from the <code>FilterScaler</code> ending the
	 * 						chain of <code>FilterScaler</code>s started by
	 * 						<code>this StartFilter</code>, a caller should
	 * 						{@link Object#wait() wait} to be {@link Object#notifyAll() notified}
	 * 						by <code>theMonitor</code>
	 */
	public StartFilter(List<I> inputList, FilterScaler<I,? extends Object> next,
			Object theMonitor) {
		super();
		setNext(next);
		input = new LinkedList<>(inputList);
		next.getFirstFilter().setQueue(input);
		monitor = theMonitor;
	}
	
	/**
	 * @throws UnsupportedOperationException because cloning a <code>StartFilter</code> could cause
	 * 			an <code>Object</code> processed by <code>this StartFilter</code> to be processed
	 * 			by it's clone too and then be processed by the chain of <code>FilterScaler</code>s
	 * 			following <code>this StartFilter</code> (and it's clone) multiple times
	 */
	@Override
	public StartFilter<I> clone() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @return <code>input</code>, for using a <code>StartFilter</code> more like other
	 * 			<code>Filter</code>s
	 */
	@Override
	public I process(I input) {
		return input;
	}
	
	/**
	 * This method should not be called manually but is called by {@link Thread#start()} when that
	 * method is called to run <code>this Filter</code>. When this method is called,
	 * <code>this StartFilter</code> {@link Filter#start() starts} the
	 * <code>next FilterScaler</code> and immediatly calls
	 * {@link Filter#stopExecution() stopeExecution()} on, waits for the <code>FilterScaler</code>s
	 * following <code>this StartFilter</code> to finish processing all input and
	 * {@link Object#notifyAll() notifies} all <code>Object</code>s waiting for the
	 * <code>monitor</code> passed to the constructor of this class when constructing
	 * <code>this StartFilter</code>, to signal them the filtering has finished and the results can
	 * be obtained (from the last <code>FilterScaler</code> chained to
	 * <code>this StartFilter</code>.
	 */
	@Override
	public void run() {
		if (next != null) {
			next.start();
		}
		stopExecution();
		pipeStopped = true;
		synchronized (monitor) {
			monitor.notifyAll();
		}
	}
	
	/**
	 * @return whether or not processing all input was processed by the whole chain of
	 * 			<code>FilterScaler</code>s following <code>this StartFilter</code>
	 */
	public boolean isPipeStopped() {
		return pipeStopped;
	}
	
	/**
	 * Calls {@link FilterScaler#stopExecution() stopExecution()} on the <code>FilterScaler</code>
	 * following <code>this StartFilter</code>, to stop the whole chain of
	 * <code>FilterScaler</code>s.
	 */
	@Override
	public boolean stopExecution() {
		return next.stopExecution();
	}
}