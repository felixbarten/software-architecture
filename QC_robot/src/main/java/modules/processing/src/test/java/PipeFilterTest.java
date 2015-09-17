package src.test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.List;

import org.junit.Test;

import src.main.java.Filter;
import src.main.java.Pipe;
import src.main.java.Sink;
import src.main.java.Task;
import src.main.java.impl.C;
import src.main.java.impl.EventHandler;
import src.main.java.impl.EventHandlerImpl;
import src.main.java.impl.ExampleTaskFilter;
import src.main.java.impl.FilterPriority;
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

        FilterPriority filt = new FilterPriority(input, input2, output);        
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
	}
	

	
	
	@Test
	public void singleInputTest() {
        Pipe<Task> input = new PipeImpl<Task>();
        Pipe<Task> output = new PipeImpl<Task>();
        
        Task coffee = new Task("Make Coffee", 1);
        Task medicine = new Task("Administer medicine", 9);
                
        input.put(coffee);
        input.put(medicine);
        
        
        final Filter<Task, Task> filter = new ExampleTaskFilter(input, output);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
            
		
	}
	
		
}