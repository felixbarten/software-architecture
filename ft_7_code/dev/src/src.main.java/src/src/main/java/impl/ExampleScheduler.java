package src.main.java.impl;

import java.util.ArrayList;

import src.main.java.Filter;
import src.main.java.FilterTwo;
import src.main.java.Pipe;
import src.main.java.Sink;
import src.main.java.Task;
import src.main.java.impl.filters.PriorityFilter;
import src.main.java.impl.filters.TaskStateFilter;

public class ExampleScheduler {

	public static void main(String[] args) {
		// create pipes
        Pipe<Task> input = new PipeImpl<Task>();
        Pipe<Task> input2 = new PipeImpl<Task>();
        Pipe<Task> output = new PipeImpl<Task>();
        
        Task coffee = new Task("Make Coffee", 1);
        Task medicine = new Task("Administer medicine", 9);


        // pipeline 1       
        final PriorityFilter filt = new PriorityFilter(input, input2, output);        
        final Sink<Task> taskSink = new TaskSink(output);
        System.out.println("Start pipeline 1");
        input.put(coffee);
        input2.put(medicine);
        
        filt.start();
        taskSink.start();
        filt.stop();
        taskSink.stop();
        
        input.closeForWriting();
        input2.closeForWriting();
        System.out.println("end pipeline 1");

        // pipeline 2
        System.out.println("Start pipeline 2");
        input = new PipeImpl<Task>();
        input2 =  new PipeImpl<Task>();
        output  = new PipeImpl<Task>();
        
        input.put(coffee);
        input2.put(medicine);
        
        PriorityFilter prio_filter = new PriorityFilter(input, input2, output);        
        Pipe<Task> input3 = output;        
        final Filter<Task, Task> filt2 = new TaskStateFilter(input3, output);
        TaskSink task_sink = new TaskSink(output);

        prio_filter.start();
        filt2.start();
        task_sink.start();
        prio_filter.stop();
        filt2.stop();
        task_sink.stop();
        
        System.out.println("end pipeline 2");

        
        System.out.println("runner finished");
	}

}
