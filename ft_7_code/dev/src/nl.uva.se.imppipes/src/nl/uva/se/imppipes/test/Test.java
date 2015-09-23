package nl.uva.se.imppipes.test;

import java.util.*;

import nl.uva.se.imppipes.*;

/**
 * Class for testing {@link nl.uva.se.imppipes} using {@link PlusOneFilter} and
 * {@link TimesTwoFilter}.
 * 
 * @author FT_7
 */
public class Test {
	
	private StartFilter<Integer> startFilter;
	private FinalFilterScaler<Integer> finalFilter;
	private List<Integer> testValues;
	private Object monitor;
	/**
	 * The number of tests performed. Ten by default.
	 */
	public static final int NUMBER_OF_TESTS = 10;
	/**
	 * The number of values processed per test. Ten by default.
	 */
	public static final int NUMBER_OF_VALUES_PER_TEST = 10;
	/**
	 * A maximum to the values processed while testing. Then by default.
	 */
	public static final int MAXIMUM_TEST_VALUE = 10;
	
	/**
	 * Constructor for <code>Object</code>s of class <code>Test</code>. Creates a chain of a
	 * <code>StartFilter</code>, a <code>PlusOneFilter</code>, a <code>TimesTwoFilter</code> and
	 * a <code>FinalFilterScaler</code>, all expecting and returning <code>Integer</code>s for
	 * processing.
	 */
	public Test() {
		finalFilter = new FinalFilterScaler<>();
		FilterScaler<Integer,Integer> timesTwoFilter
			= new FilterScaler<>(new TimesTwoFilter(),finalFilter);
		FilterScaler<Integer,Integer> plusOneFilter
			= new FilterScaler<>(new PlusOneFilter(),timesTwoFilter);
		testValues = generateRandomIntegerList();
		monitor = new Object();
		startFilter = new StartFilter<>(testValues, plusOneFilter, monitor);
	}
	
	/**
	 * Run <code>NUMBER_OF_TESTS</code> tests. Test progress and results are printed to
	 * <code>System.out</code>.
	 * 
	 * @param args a list of command line parameters, which are ignored
	 */
	public static void main(String[] args) {
		boolean succeeded = true;
		int i = 0;
		while (succeeded && i<NUMBER_OF_TESTS) {
			System.out.println("Starting test " + i);
			succeeded = succeeded && test();
			System.out.println(succeeded ? "Passed test " + i : "Failed test " + i);
			i++;
		}
		System.out.println(succeeded ? "Passed all tests" : "Failed one or more tests");
		System.exit(0);
	}
	
	/**
	 * Created a new <code>Test</code> and use it to run a test by calling {@link Test#runTest()}.
	 * Any occuring <code>Exception</code>s are printed to <code>System.out</code>.
	 * 
	 * @return whether or not the performed test succeeded
	 */
	public static boolean test() {
		Test t = new Test();
		try {
			return t.runTest();
		}
		catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	/**
	 * Start the <code>startFilter</code> of <code>this Test</code>, wait for the filtering to
	 * finish using the method described in the documentation of {@link StartFilter#run()},
	 * reset the filters, get the result and check whether it is equal to the list of
	 * <code>testValues</code> used by <code>this Test</code> after adding one to each element and
	 * then multiplying it by two, ignoring the order of the elements.
	 * 
	 * @return whether or not the filters produced the same result as a call to
	 * {@link #calculateResult()}
	 * @throws InterruptedException if calling {@link Object#wait() wait()} on the
	 * 			<code>monitor</code> used by <code>this Test</code> throws an
	 * 			{@link InterruptedException}
	 */
	public boolean runTest() throws InterruptedException {
		System.out.println("Starting filters");
		startFilter.start();
		synchronized(monitor) {
			while (!startFilter.isPipeStopped()) {
				System.out.println("Waiting for filtering to finish");
				monitor.wait();
			}
		}
		System.out.println("Checking results");
		List<Integer> result = finalFilter.getResult();
		finalFilter.reset();
		List<Integer> control = calculateResult();
		Set<Integer> resultSet = new HashSet<>();
		resultSet.addAll(result);
		Set<Integer> controlSet = new HashSet<>();
		controlSet.addAll(control);
		return result.size() == control.size()
				&& resultSet.equals(controlSet);
	}
	
	/**
	 * @return the result of adding one to each element of the <code>testValues</code> used by
	 * <code>this Test</code> and then multiplying it by two.
	 */
	public List<Integer> calculateResult() {
		List<Integer> result = new ArrayList<>();
		for (int i : testValues) {
			result.add((i+1)*2);
		}
		return result;
	}
	
	/**
	 * @return a <code>List</code> of <code>NUMBER_OF_VALUES_PER_TEST</code> <code>Integer</code>s
	 * 			between 0 and <code>MAXIMUM_TEST_VALUE</code>
	 */
	public List<Integer> generateRandomIntegerList() {
		List<Integer> result = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < NUMBER_OF_VALUES_PER_TEST; i++ ) {
			result.add(random.nextInt(MAXIMUM_TEST_VALUE));
		}
		return result;
	}
}