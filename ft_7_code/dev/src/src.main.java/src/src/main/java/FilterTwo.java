package src.main.java;

public abstract class FilterTwo <I, I2, O> extends ThreadedRunner{
	protected Pipe<I> input;
	protected Pipe<I2> input2;
	protected Pipe<O> output;

	public FilterTwo(Pipe<I> input,Pipe<I2> input2, Pipe<O> output) {
		this.input = input;
		this.input2 = input2;
		this.output = output;
	}

	@Override
	public void run() {
		transformBetween(input,input2, output);
	}

	protected abstract void transformBetween(Pipe<I> input,Pipe<I2> input2, Pipe<O> output);
}
