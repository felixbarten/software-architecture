package src.main.java.impl;

import java.util.ArrayList;
import java.util.List;

import src.main.java.Filter;
import src.main.java.FilterTwo;
import src.main.java.Pipe;

public abstract class SimpleFilterTwo<I,I2, O> extends FilterTwo<I,I2, O> {
	 public SimpleFilterTwo(Pipe<I> input,Pipe<I2> input2,Pipe<O> output) {
	        super(input,input2, output);
	    }

	    @Override
	    protected void transformBetween(Pipe<I> input,Pipe<I2> input2, Pipe<O> output) {
	        try {
	        	List<I, I2> taskList = new ArrayList<Task>();
	            I in;
	            I2 in2;
	            while ((in = input.nextOrNullIfEmptied()) != null) {
	                O out = transformOne(in);
	                output.put(out);
	            }
	            while ((in2 = input2.nextOrNullIfEmptied()) != null) {
	                //O out = transformOne(in2);
	                //output.put(out);
	            }
	        } catch (InterruptedException e) {
	            // TODO handle properly, using advice in http://www.ibm.com/developerworks/java/library/j-jtp05236/
	            System.err.println("interrupted");
	            e.printStackTrace();
	            return;
	        }
	        output.closeForWriting();
	    }

	    protected abstract O transformOne(I in);
}
