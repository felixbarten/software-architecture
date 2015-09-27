package nl.uva.se.funcpipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Program {

	public static void main(String[] args) {
		//examples();

		//BPSample();
		exampleBloodPressure();
		//feedbackLoopExample();
		showEngineersLoggingInfo();

	}
	
	public static void BPSample() {
		List<BloodPressure>  bps = new ArrayList<BloodPressure> ();
	
		for (int i=0; i<100; i++) {
			// if not showing output skip creating object step.
			BloodPressure bpObj = BPGenerator.generateBP();
			bps.add(bpObj);
			bpObj.readDiagnosis();
		}
	}
	
	public static List<BloodPressure> BPSamples() {
		List<BloodPressure>  bps = new ArrayList<BloodPressure> ();
	
		for (int i=0; i<100; i++) {
			bps.add(BPGenerator.generateBP());
		}
		return bps;
	}
	
	public static void exampleBloodPressure() {
		System.out.println("Pipeline started");
		Function <List<BloodPressure>, Double> avg = (f) -> f.stream().mapToInt(BloodPressure::getBloodPressure).average().getAsDouble();
		
		Consumer <List<BloodPressure>> average = (f) -> f.stream().mapToInt(BloodPressure::getBloodPressure).average().getAsDouble();
		
		
		Pipeline <List<BloodPressure>> p = new Pipeline<List<BloodPressure>>();
		
		List<BloodPressure> bps = new ArrayList<BloodPressure>();
		bps.add(BPGenerator.generateBP());
		bps.add(BPGenerator.generateBP());		
		bps.add(BPGenerator.generateBP());
		
		p.clear();
		p.transform(avg)
		.via(i -> System.out.println(i));		
		
		p.feed(bps);
		p.feed(Arrays.asList(BPGenerator.generateBP(), BPGenerator.generateBP()));
		p.feed(BPSamples());
				
		System.out.println("Pipeline finished");
		
		
	}
	

	public static void showEngineersLoggingInfo(){
	
		Pipeline<LoggingInfo> p = new Pipeline<LoggingInfo>();
		LoggingInfo li = new LoggingInfo("soft_eng", "test1", "aaa123", 5, 5);
		LoggingInfo li_2 = new LoggingInfo("soft_eng", "test2", "aaa123", 5, 4);
		LoggingInfo li_3 = new LoggingInfo("soft_tester", "test3", "aaa123", 5, 5);
		LoggingInfo li_4 = new LoggingInfo("soft_tester", "test4", "aaa123", 6, 15);
		LoggingInfo li_5 = new LoggingInfo("soft_eng", "test5", "aaa123", 5, 5);
		LoggingInfo li_6 = new LoggingInfo("soft_dev", "test6", "aaa123", 5, 5);

		printTitle("Example: Logging Info for Software Engineers");
		p.clear();
		
		p.filter(i -> i.job.equals("soft_eng"));
		p.via(i -> System.out.print(i + " "));

		p.feed(Arrays.asList(li,li_2,li_3,li_4,li_5, li_6));
	}
		
	public static void examples() {
		
		// This 'functional' pipeline was inspired by LINQ, and is somewhat
		// similar to Java 8's streams.
		//
		// In the pipeline universe, we have:
		// 	  - Pipelines carrying data of some type
		// 	  - Filters, which when given data decide if it should flow through
		//	  - Functions which modify or replace elements
		//	  - Functions which transform elements into a new data type
		//	  - Buffers, which aggregate into a new type
		//
		// A pipeline is simply put just a chain of operations which should
		// be performed sequentially on an element passing through. For instance,
		// when computing '5 * 10 * 5', the pipeline would hold two stages, '*12' and
		// '*5' and would be fed the input '5'.
		Pipeline<Integer> p = new Pipeline<Integer>();

		printTitle("Example 1: function chaining");
		p.via(i -> i * 10);
		p.via(i -> i * 5);
		p.via(i -> System.out.println(i));
		
		// Feeding 5 into the pipeline gives the expected result: 250.
		p.feed(5);
	
		
		// Feeding a collection of numbers is also possible, the following
		// yields 0, 250 and 500.
		p.feed(Arrays.asList(0, 5, 10));
				
		// Operations can only ever be added to a pipeline, i.e. you can't change
		// or swap two operations. To clear the pipeline, use the clear() function.
		p.clear();
		
		
		
		// An example involving a filter would be the elimination of uneven numbers
		// from a stream.
		printTitle("Example 2: filtering");
		p.clear();
		p.filter(i -> i % 2 == 0);
		p.via(i -> System.out.print(i + " "));

		p.feed(intRange(0, 25));		
		System.out.println();
		
		
		
		// If we want to transform elements to another type we can add a transformation.
		// For this example, we'll change from Integer to Double and print.
		printTitle("Example 3: transformations");
		p.clear();
		p.transform(i -> new Double(i));
		p.via(d -> System.out.println(d));
		
		// Feeding 123 to the pipeline results in.. Nothing! Why? Because the transformation
		// changed the pipeline's data type to Double, which actually isn't possible. The
		// transform(..) function internally created a new pipeline of the desired type
		// and hooked it up.
		//
		// Which would be rather useless if we couldn't use the result. Therefore, pipelines
		// can also be configured with through a builder interface, which doesn't have these
		// restrictions.
		p.clear();
		p.transform(i -> new Double(i));
		 p.via(d -> System.out.println(d));
		
		// And now we get the expected result.
		p.feed(123);
		
		
		
		// With these tools its possible to simulate most sequential computations. But what
		// if we wanted to offer some form of branching? This can be achieved with 'split()'.
		//
		// Going back to the filter example, let's split the input into two lanes: one for
		// even numbers and one for uneven numbers.
		printTitle("Example 4: split");
		p.clear();
		p.split()
			.to(newPipe -> newPipe.filter(i -> i % 2 == 0).via(i -> System.out.println("Even " + i)))
			.to(newPipe -> newPipe.filter(i -> i % 2 != 0).via(i -> System.out.println("Uneven " + i)));
		
		// Pushing [0..6] yields alternating even and uneven prints.
		p.feed(intRange(0, 6));
		
		
		
		// So what if we want to merge these back together? Well we can connect the lanes
		// to a single new pipeline and go from there!
		printTitle("Example 5: split and merge");
		
		Pipeline<Integer> merge = new Pipeline<Integer>();
		p.clear();
		p.split()
			.to(newPipe -> newPipe.to(merge))
			.to(newPipe -> newPipe.via(i -> i * -1).to(merge));
		
		merge.via(i -> System.out.println(i));
		
		// Pushing 1 results in two outputs, 1 and -1.
		p.feed(1);
		
		
		
		// If we simply wanted the pair (i, -i) do we really need a split and merge? With
		// the expand function a single stage suffices.
		printTitle("Example 6: expanding");
		p.clear();
		p.expand(i -> Arrays.asList(i, -i));
		p.via(i -> System.out.println(i));
		
		// Pushing 1 again yields the same results, 1 and -1.
		p.feed(1);
		
		
		
		// Now we come to the final block, the buffer. A buffer aggregates input
		// and once filled automatically invokes a transformation function which
		// accepts a collection of elements.
		//
		// As an example, we'll take [0..100], consume it in chunks of 20 integers
		// and print the average, which is a float.
		printTitle("Example 7: buffers and aggregating");
		
		Buffer<Integer, Float> buf = new Buffer<Integer, Float>(20);
		buf.setFunc(is -> {
			int n = is.size();
			int sum = 0;
			for (Integer i : is) {
				sum += i;
			}
			return sum / (float)n;
		});
		buf.via(f -> System.out.println(f));
		
		p.clear();
		p.to(buf);
		p.feed(intRange(0, 100));
		p.clear();
		
		
		
		// This approach isn't limited to lambda functions, as all are just interfaces
		// defined in the java.util.function package. Complex components can have their
		// own classes, leaving only pipeline configuration.
		//
		// Additionally, re-configuring a pipeline over and over again isn't expected
		// use, so remembering to call clear() is not a big issue.
	}
	
	private static void feedbackLoopExample() {
		// In this example, we'll create a simple feedback loop. This isn't ideal for
		// a system like this, but it does showcase the pipeline in action.
		
		// The idea is to input a float, and bring it within a range of a set goal.
		float goal = 2.5f;
		float maxDev = 0.05f;
		float raiseFactor = 1.333f;
		float lowerFactor = 0.333f;
		
		// We'll also use some reusable components, which should clarify the applicability
		// to classes.
		Consumer<Float> print = (f) -> System.out.printf("%.1f ", f);
		Consumer<Float> done = (f) -> System.out.printf("-> %.3f\n", f);
		
		Predicate<Float> isCloseEnough = (f) -> Math.abs(goal - f) <= maxDev;
		Predicate<Float> isNotCloseEnough = (f) -> Math.abs(goal - f) > maxDev;
		Predicate<Float> isOver = (f) -> f > goal;
		Predicate<Float> isUnder = (f) -> f <= goal;
		Function<Float, Float> raise = (f) -> f * raiseFactor;
		Function<Float, Float> lower = (f) -> f * lowerFactor;
		
		Pipeline<Float> p = new Pipeline<Float>();
		p.via(print);
		p.split()
			.to(p2 -> p2.filter(isCloseEnough).via(done))
			.to(p2 -> p2.filter(isNotCloseEnough).split()
					.to(p3 -> p3.filter(isOver).via(lower).to(p))
					.to(p3 -> p3.filter(isUnder).via(raise).to(p)));
		
		printTitle("Example 8: feedback loop");
		p.feed(5f);
		System.out.println();
	}
	
	// Range helper.
	private static List<Integer> intRange(int incLower, int incUpper) {
		return IntStream.rangeClosed(incLower, incUpper).boxed().collect(Collectors.toList());
	}

	private static void printTitle(String s) {
		System.out.println();
		System.out.println("-------------------------------------");
		System.out.println(s);
		System.out.println("-------------------------------------");
	}
}