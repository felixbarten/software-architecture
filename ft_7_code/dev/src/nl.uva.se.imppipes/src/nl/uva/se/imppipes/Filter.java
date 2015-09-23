package nl.uva.se.imppipes;

import java.util.Queue;

/**
 * A <code>Filter</code> is a {@link Thread} for processing elements in a pipe.
 * A filter expect values to be added to it's <code>queue</code> and calls
 * {@link FilterScaler#add(Object)} on the <code>next FilterScaler</code> to have the results it
 * produces processed further.
 * 
 * @author FT_7
 *
 * @param <I> the type of the values put into <code>this Filter</code>
 * @param <O> the type of the values <code>this Filter</code> puts into the next filter
 * 
 * @see FilterScaler
 */
public abstract class Filter<I,O>
 	extends Thread implements Cloneable {
	
	protected FilterScaler<O,? extends Object> next;
	protected volatile Queue<I> queue;
	private volatile boolean stop = false;
	/**
	 * The time <code>this Filter</code> sleeps before trying again when it finds it's
	 * <code>queue</code> empty after it is done processing an <code>Object</code>, in ms. The
	 * default value is an arbitrary time of 10ms.
	 */
	public static final int SLEEP_TIME = 10;
	private volatile boolean stopped = false;
	private volatile Object ownMonitor = new Object();
	
	/**
	 * Set the <code>FilterScaler this Filter</code> calls
	 * {@link FilterScaler#add(Object) add(Object)} on to have <code>Object</code>s it has
	 * processed, processed further.
	 * 
	 * @param nextFilterScaler a <code>FilterScaler</code> which accepts <code>Object</code>s of
	 * type <code>O</code> as input
	 */
	public void setNext (FilterScaler<O, ? extends Object> nextFilterScaler) {
		next = nextFilterScaler;
	}
	
	/**
	 * Set the <code>queue this Filter</code> should process values from. If the same
	 * <code>Queue</code> is passed to multiple <code>Filter</code>s, it should be synchronized.
	 * 
	 * @param newQueue a <code>Queue</code> of <code>Object</code>s of type <code>I</code> or
	 * 					subtypes of <code>I</code>
	 */
	public void setQueue(Queue<I> newQueue) {
		queue = newQueue;
	}
	
	/**
	 * This method should not be called manually but is called by {@link Thread#start()} when that
	 * method is called to run <code>this Filter</code>. While running, a <code>Filter</code>
	 * gets and removes an <code>Object</code> from it's <code>queue</code>, processes it, adds
	 * the result to <code>next</code> and goes on with the next <code>Object</code> from it's
	 * <code>queue</code>. When the <code>queue</code> is empty, a <code>Filter</code> sleeps for
	 * {@link #SLEEP_TIME} before trying to get an <code>Object</code> to process again.
	 */
	@Override
	public void run() {
		while (!stop || !queue.isEmpty()) {
			I input = queue.poll();
			if (input == null) {
				try {
					sleep(SLEEP_TIME);
				}
				catch (InterruptedException e) {}//interruption during sleep is no problem here
			}
			else {
				O output = process(input);
				if (next != null && output != null) next.add(output);
			}
		}
		stopped = true;
		synchronized (ownMonitor) {
			ownMonitor.notifyAll();
		}
	}
	
	/**
	 * Processes a value. This method defines what <code>this Filter</code> does with each value
	 * it gets from it's <code>queue</code> before adding it to the <code>next</code>
	 * <code>FilterScaler</code>.
	 * 
	 * @param input the value to process
	 * @return the result of processing <code>input</code>
	 */
	public abstract O process(I input);
	
	/**
	 * Returns a <code>Filter</code> of the same type as <code>this Filter</code>, processing
	 * input from the same <code>queue</code> and adding the results to the same
	 * <code>FilterScaler</code>. N.B. This method can be inconsistent with {@link #equals} and
	 * {@link #hashCode()}.
	 * 
	 * @see #copyNextAndQueue(Filter, Filter)
	 */
	@Override
	public abstract Object clone();
	
	/**
	 * Stops the execution of <code>this Filter</code>. Makes {@link #run()} terminate as soon as
	 * <code>this Filter</code>'s <code>queue</code> becomes empty.
	 * @return <code>true</code> when <code>this Filter</code> has finished running
	 */
	public boolean stopExecution() {
		synchronized (ownMonitor) {
			stop = true;
			while (!stopped) {
				try {
					ownMonitor.wait();
				}
				catch (InterruptedException e) {
					//InterruptedException doesn't matter here
				}
			}
		}
		//Return value is for waiting for this filter to finish only
		return true;
	}
	
	/**
	 * Sets the <code>FilterScaler to</code> adds the values it has processed to, to the one
	 * <code>from</code> adds the values it has processed to and the <code>Queue to</code>
	 * gets input from to the one <code>form</code> gets input form. This method might be usefull
	 * when implementing {@link #clone()} for a <code>Filter</code>.
	 * 
	 * @param from the <code>Filter</code> to copy the <code>next</code> and <code>queue</code> from
	 * @param to the <code>Filter</code> to copy the <code>next</code> and <code>queue</code> to
	 * @return <code>true</code> when this method has finished running
	 */
	protected boolean copyNextAndQueue(Filter from, Filter to) {
		to.setNext(from.next);
		to.setQueue(from.queue);
		return true;
	}
}