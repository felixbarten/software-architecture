package src.main.java.impl;

import src.main.java.Pipe;
import src.main.java.Task;
import src.main.java.impl.SimpleFilter;

public class ExampleTaskFilter extends SimpleFilter<Task, Task> {
    public ExampleTaskFilter(Pipe<Task> input, Pipe<Task> output) {
        super(input, output);
    }
    
    @Override
    protected Task transformOne(Task in) {
    	Task out = in;
        System.out.println("filtered " + " to " + out.toString());
        return out;
    }
}
