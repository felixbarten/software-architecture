package src.test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.List;

import org.junit.Test;

import src.main.java.Filter;
import src.main.java.Pipe;
import src.main.java.Sink;
import src.main.java.Task;
import src.main.java.Task.State;
import src.main.java.impl.filters.PriorityFilter;
import src.main.java.impl.filters.PassthroughFilter;
import src.main.java.impl.filters.TaskStateFilter;
import src.main.java.impl.PipeImpl;
import src.main.java.impl.TaskSink;


public class PipeFilterTest {


	@Test
	public void multipleInputTest() {
		
        Pipe<Task> input = new PipeImpl<Task>();
        Pipe<Task> input2 = new PipeImpl<Task>();
        Pipe<Task> output = new PipeImpl<Task>();
        
        Task coffee = new Task("Make Coffee", 1);
        Task medicine = new Task("Administer medicine", 9);

        PriorityFilter filt = new PriorityFilter(input, input2, output);        
        TaskSink task_sink = new TaskSink(output);
		
        input.put(coffee);
        input2.put(medicine);
        
        filt.start();
        task_sink.start();
        
        
        filt.stop();
        task_sink.stop();
      
        try {
        	// sleep because of delays in pipe
			Thread.sleep(300);
	        assertEquals(1, task_sink.getTasks().size());
	        assertEquals(medicine, task_sink.getTasks().get(0));
        } catch (InterruptedException e) {
			e.printStackTrace();
		}    
        
        input.closeForWriting();
        input2.closeForWriting();
        output.closeForWriting();
	}
	

	@Test
	public void singleInputTest() {
        Pipe<Task> input = new PipeImpl<Task>();
        Pipe<Task> output = new PipeImpl<Task>();
        
        Task coffee = new Task("Make Coffee", 1);
        Task medicine = new Task("Administer medicine", 9);
                
        input.put(coffee);
        input.put(medicine);
        
        
        final Filter<Task, Task> filter = new PassthroughFilter (input, output);
        TaskSink task_sink = new TaskSink(output);

        filter.start();
        task_sink.start();       		

        filter.stop();
        task_sink.stop();    
        try {
	        Thread.sleep(300);			
	        List<Task> tasks = task_sink.getTasks();
			assertEquals(2, tasks.size());
	        assertEquals(tasks.get(0), coffee);
	        assertEquals(tasks.get(1), medicine);  
        } catch (InterruptedException e) {
			e.printStackTrace();
		}
        input.closeForWriting();
        output.closeForWriting();        
	}
	
	@Test
	public void stateTaskTest() {
        Pipe<Task> input = new PipeImpl<Task>();
        Pipe<Task> output = new PipeImpl<Task>();
        
        Task coffee = new Task("Make Coffee", 1, State.STARTED);
        Task medicine = new Task("Administer medicine", 9, State.IDLE);
        Task openDoor = new Task("Open the door", 9, State.FINISHED);
        
        input.put(coffee);
        input.put(medicine);
        input.put(openDoor);
        
        
        final Filter<Task, Task> filter = new TaskStateFilter(input, output);
        TaskSink task_sink = new TaskSink(output);

        filter.start();
        task_sink.start();       		

        filter.stop();
        task_sink.stop();        
        try {
	        Thread.sleep(300);			
	        List<Task> tasks = task_sink.getTasks();
			assertEquals(3, tasks.size());
	        assertEquals(State.STOPPED, tasks.get(0).getState());
	        assertEquals(State.FINISHED, tasks.get(1).getState());  
	        assertEquals(State.FINISHED, tasks.get(2).getState());  

        } catch (InterruptedException e) {
			e.printStackTrace();
		}
        input.closeForWriting();
        output.closeForWriting();
        
	}
	
	@Test
	public void doubleFilterTest() {
        Pipe<Task> input = new PipeImpl<Task>();
        Pipe<Task> intermediateOutput = new PipeImpl<Task>();
        Pipe<Task> output = new PipeImpl<Task>();
        
        Task coffee = new Task("Make Coffee", 1, State.STARTED);
        Task medicine = new Task("Administer medicine", 9, State.IDLE);
        Task openDoor = new Task("Open the door", 7, State.FINISHED);
        
        input.put(coffee);
        input.put(medicine);
        input.put(openDoor);
        
        
        final Filter<Task, Task> filter = new TaskStateFilter(input, intermediateOutput);
        final PassthroughFilter filter2 = new PassthroughFilter(intermediateOutput, output);
        
        TaskSink task_sink = new TaskSink(output);

        filter.start();
        filter2.start();
        task_sink.start();   
        
        filter.stop();
        filter2.stop();
        task_sink.stop();      

        try {
	        Thread.sleep(400);			
	        List<Task> tasks = task_sink.getTasks();
	        System.out.print(tasks);

			assertEquals(3, tasks.size());
	        assertEquals(State.STOPPED, tasks.get(0).getState());
	        assertEquals(State.FINISHED, tasks.get(1).getState());  
	        assertEquals(State.FINISHED, tasks.get(2).getState());  
	        
	        System.out.print(tasks);

        } catch (InterruptedException e) {
			e.printStackTrace();
		}
        input.closeForWriting();
        intermediateOutput.closeForWriting();
        output.closeForWriting();
    }
	
	/**
	 * Test the output of a number of single input filters with a dual input filter.
	 * The expected result is that for ever 2 elements only one will be returned so it will have a smaller output than the intput.
	 * i
	 * ======> Filter ====>						 out
	 * i2					Filter2=====>Filter3====>
	 * ===================>
	 */

	@Test
	public void multipleFiltersTest() {
        Pipe<Task> input = new PipeImpl<Task>();
        Pipe<Task> input2 = new PipeImpl<Task>();
        Pipe<Task> output = new PipeImpl<Task>();
        Pipe<Task> intermediateOut = new PipeImpl<Task>();
        Pipe<Task> intermediateOut2 = new PipeImpl<Task>();

        Task coffee = new Task("Make Coffee", 1, State.STARTED);
        Task medicine = new Task("Administer medicine", 9, State.IDLE);
        Task openDoor = new Task("Open the door", 5, State.FINISHED);
        Task helpResident = new Task("Help Resident", 7, State.PAUSED);
        Task makeBeverage = new Task("Make Tea", 2, State.INITIALIZED);
        
        // input pipe 1        
        input.put(coffee);
        input.put(medicine);
        input.put(openDoor);
        
        // input pipe 2
        input2.put(makeBeverage);
        input2.put(helpResident);
        input2.put(coffee);
        
        final TaskStateFilter filter = new TaskStateFilter(input, intermediateOut);
        final PriorityFilter prioFilter = new PriorityFilter(intermediateOut, input2, intermediateOut2);
		final Filter<Task, Task> filter2 = new TaskStateFilter(intermediateOut2, output);
      
        TaskSink task_sink = new TaskSink(output);

        filter.start();
        prioFilter.start();
        filter2.start();
        task_sink.start();       		

        filter.stop();
        prioFilter.stop();
        filter2.stop();
        task_sink.stop();
        
        try {
	        Thread.sleep(500);			
	        List<Task> tasks = task_sink.getTasks();
			assertEquals(3, tasks.size());
			System.out.print(tasks);

        } catch (InterruptedException e) {
			e.printStackTrace();
		}
       
        input.closeForWriting();
        input2.closeForWriting();
        intermediateOut.closeForWriting();
        intermediateOut2.closeForWriting();
        output.closeForWriting();
	}
	
	/**
	 * test what happens when only one argument is supplied to the multiple input filter
	 */
	@Test
	public void multipleFiltersTest2() {
        Pipe<Task> input = new PipeImpl<Task>();
        Pipe<Task> input2 = new PipeImpl<Task>();
        Pipe<Task> output = new PipeImpl<Task>();

        Task coffee = new Task("Make Coffee", 1, State.STARTED);
        Task medicine = new Task("Administer medicine", 9, State.IDLE);

        // input pipe 1        
        input.put(coffee);
        input.put(medicine);

        // input pipe 2
        input2.put(coffee);

        final PriorityFilter prioFilter = new PriorityFilter(input, input2, output);                   
        TaskSink task_sink = new TaskSink(output);

        prioFilter.start();
        task_sink.start();       		

        prioFilter.stop();
        task_sink.stop();
        
        try {
	        Thread.sleep(500);			
	        List<Task> tasks = task_sink.getTasks();
			assertEquals(1, tasks.size());
			System.out.print(tasks);

        } catch (InterruptedException e) {
			e.printStackTrace();
		}
        input.closeForWriting();
        input2.closeForWriting();
        output.closeForWriting();
	}	
}