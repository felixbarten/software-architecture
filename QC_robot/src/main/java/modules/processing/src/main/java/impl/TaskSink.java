package src.main.java.impl;

import java.util.ArrayList;
import java.util.List;

import src.main.java.Pipe;
import src.main.java.Sink;
import src.main.java.Task;

	public class TaskSink extends Sink<Task> {
	    List<Task> tasks;
		
		public TaskSink(Pipe<Task> input) {
	        super(input);
	        tasks = new ArrayList<Task>();
	    }
		 
	    public List<Task> getTasks() {
			return tasks;
		}


		@Override
	    public void takeFrom(Pipe<Task> pipe) {
	        try {
	            Task in;
	            while ((in = pipe.nextOrNullIfEmptied()) != null) {
	                System.out.println(in);
	                in.toString();
	                tasks.add(in);
	            }
	            System.out.println("sink finished");
	        } catch (InterruptedException e) {
	            System.err.println("interrupted");
	            e.printStackTrace();
	        } finally {
	            System.out.close();
	            
	        }
	    }
	}

