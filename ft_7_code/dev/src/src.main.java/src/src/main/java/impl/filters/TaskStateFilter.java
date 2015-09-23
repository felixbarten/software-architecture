package src.main.java.impl.filters;

import src.main.java.Pipe;
import src.main.java.Task;
import src.main.java.Task.State;
import src.main.java.impl.SimpleFilter;

public class TaskStateFilter extends SimpleFilter<Task, Task> {
    public TaskStateFilter(Pipe<Task> input, Pipe<Task> output) {
        super(input, output);
    }
    
    /**
     * Example filter, will modify the state contained within a task object. 
     */
    @Override
    protected Task transformOne(Task in) {
    	Task out = in;
    	// check if state == start then change the state to stopped otherwise change state to .
    	if (out.getState() == State.STARTED) { 		
    		out.setState(State.STOPPED);
    	} else {
    		out.setState(State.FINISHED);
    	}
    	
        //System.out.println("filtered " + " to " + out.toString() + "\n");
        return out;
    }
}
