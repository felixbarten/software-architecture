package src.main.java.impl.filters;

import src.main.java.Pipe;
import src.main.java.Task;
import src.main.java.impl.SimpleFilterTwo;

public class PriorityFilter extends SimpleFilterTwo<Task, Task, Task> {
	
    public PriorityFilter(Pipe<Task> input, Pipe<Task> input2 , Pipe<Task> output) {
        super(input, input2, output);
    }

	@Override
	protected Task transformOne(Task in, Task in2) {
		if (in == null)
			return in2;
		if (in2 == null)
			return in;
		System.out.println("Choosing between " + in.toString() + " and " + in2.toString() + "\n");
		Task out = (in.getPriority() >= in2.getPriority() ? in : in2);	
		System.out.println("Selected highest priority task: " + out.toString());
		return out;
	}

}
