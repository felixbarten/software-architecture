package src.main.java.impl;

import java.util.ArrayList;

import src.main.java.Filter;
import src.main.java.Generator;
import src.main.java.Pipe;
import src.main.java.Sink;
import src.main.java.Task;

public class ExampleScheduler {

	public static void main(String[] args) {
		  // create pipes
        final Pipe<Task> genToFilter = new PipeImpl<Task>();
        final Pipe<Task> filterToOut = new PipeImpl<Task>();
        
        ArrayList<Task> taskList = new ArrayList<>();
        Task coffee = new Task("Make Coffee", 1);
        Task medicine = new Task("Administer medicine", 9);

        taskList.add(coffee);
        taskList.add(medicine);

        // create components that use the pipes
        //final Generator<Integer> generator = new ExampleGenerator(genToFilter);
        final Filter<Integer, String> filter = new ExampleFilter(genToFilter, filterToOut);
        final Sink<String> sink = new ExampleSink(filterToOut);

        // start all components
        generator.start();
        filter.start();
        sink.start();

        System.out.println("runner finished");
	}

}
