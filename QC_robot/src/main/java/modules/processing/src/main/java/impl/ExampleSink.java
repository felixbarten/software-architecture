package src.main.java.impl;

import src.main.java.Pipe;
import src.main.java.Sink;


public class ExampleSink extends Sink<String> {
    public ExampleSink(Pipe<String> input) {
        super(input);
    }

    @Override
    public void takeFrom(Pipe<String> pipe) {
        try {
            String in;
            while ((in = pipe.nextOrNullIfEmptied()) != null) {
                System.out.println(in);
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