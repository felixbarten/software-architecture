package src.main.java.impl;

import src.main.java.Pipe;
import src.main.java.Task;

public class FilterPriority extends SimpleFilterTwo<Task, Task, Task> {
	
    public FilterPriority(Pipe<Task> input, Pipe<Task> input2 , Pipe<Task> output) {
        super(input, input2, output);
    }

	@Override
	protected Task transformOne(Task in, Task in2) {
		return (in.getPriority() >= in2.getPriority() ? in : in2);	
	}
}
