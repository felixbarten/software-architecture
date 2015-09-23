package src.main.java.impl.filters;

import src.main.java.Pipe;
import src.main.java.Task;
import src.main.java.impl.SimpleFilter;

public class PassthroughFilter extends SimpleFilter<Task, Task> {
    public PassthroughFilter(Pipe<Task> input, Pipe<Task> output) {
        super(input, output);
    }
    
    @Override
    protected Task transformOne(Task in) {
    	Task out = in;
       // System.out.println("filtered " + " to " + out.toString()+ "\n");
        return out;
    }
}
