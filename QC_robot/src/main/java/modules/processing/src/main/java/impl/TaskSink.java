package src.main.java.impl;

import src.main.java.Pipe;
import src.main.java.Sink;
import src.main.java.Task;

	public class TaskSink extends Sink<Task> {
	    public TaskSink(Pipe<Task> input) {
	        super(input);
	    }

	    @Override
	    public void takeFrom(Pipe<Task> pipe) {
	        try {
	            Task in;
	            while ((in = pipe.nextOrNullIfEmptied()) != null) {
	                System.out.println(in);
	                in.toString();
	                delayForDebug(300);
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

