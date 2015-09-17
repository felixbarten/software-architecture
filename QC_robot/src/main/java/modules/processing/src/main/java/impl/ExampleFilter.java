package src.main.java.impl;

import src.main.java.Pipe;
import src.main.java.impl.SimpleFilter;

public class ExampleFilter extends SimpleFilter<Integer, String> {
    public ExampleFilter(Pipe<Integer> input, Pipe<String> output) {
        super(input, output);
    }

    @Override
    protected String transformOne(Integer in) {
        String out = Integer.toString(in);
        System.out.println("filtered " + Integer.toString(in) + " to " + out);
        return out;
    }
    
}
