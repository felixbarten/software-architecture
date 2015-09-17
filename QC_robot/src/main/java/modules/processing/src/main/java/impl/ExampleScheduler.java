package src.main.java.impl;

import java.util.ArrayList;

import src.main.java.Filter;
import src.main.java.FilterTwo;
import src.main.java.Generator;
import src.main.java.Pipe;
import src.main.java.Sink;
import src.main.java.Task;

public class ExampleScheduler {

	public static void main(String[] args) {
		  // create pipes
        final Pipe<Integer> genToFilter = new PipeImpl<Integer>();
        final Pipe<String> filterToOut = new PipeImpl<String>();
        
        ArrayList<Task> taskList = new ArrayList<>();
        Task coffee = new Task("Make Coffee", 1);
        Task medicine = new Task("Administer medicine", 9);

        
        final Pipe<Task> input = new PipeImpl<Task>();
        final Pipe<Task> input2 = new PipeImpl<Task>();
        final Pipe<Task> output = new PipeImpl<Task>();

        input.put(coffee);
        input2.put(medicine);
        
        taskList.add(coffee);
        taskList.add(medicine);

        // create components that use the pipes
        //final Generator<Integer> generator = new ExampleGenerator(genToFilter);
        final Filter<Integer, String> filter = new ExampleFilter(genToFilter, filterToOut);
        
        
        final FilterPriority filt = new FilterPriority(input, input2, output);
        
        final Sink<String> sink = new ExampleSink(filterToOut);
        
        final Sink<Task> taskSink = new TaskSink(output);

        // start all components
        //generator.start();     
        //filter.start();
        //sink.start();

        filt.start();
        taskSink.start();
        
        System.out.println("runner finished");
	}

}
