package nl.uva.se.funcpipes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The buffer buffers input up to a specified size, then calls a function
 * passing it an array of buffered elements. The output of this function is
 * continued into a pipeline.
 * 
 * @author Floris den Heijer
 *
 * @param <T>
 *            buffer input type
 * @param <R>
 *            buffer pipeline output type
 */
public class Buffer<T, R> implements PipelineBuilder<R>, Feedable<T> {

	public Buffer(int size) {
		this(size, null);
	}

	public Buffer(int size, Function<Collection<T>, R> func) {
		if (size <= 0) {
			throw new RuntimeException("size must be > 0");
		}
		this.size = size;
		this.buffer = new ArrayList<T>(size);
		this.func = func;
		this.pipe = new Pipeline<R>();
	}

	protected final int size;
	protected final List<T> buffer;
	protected final Pipeline<R> pipe;

	protected Function<Collection<T>, R> func;

	/**
	 * @return size of the buffer
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @return buffer function
	 */
	public Function<Collection<T>, R> getFunc() {
		return func;
	}

	/**
	 * @param new
	 *            buffer function
	 */
	public void setFunc(Function<Collection<T>, R> func) {
		this.func = func;
	}

	/**
	 * @param obj
	 *            object to be added to the buffer
	 */
	public void feed(T obj) {
		buffer.add(obj);

		if (buffer.size() == size) {
			flush();
		}
	}

	/**
	 * @param collection
	 *            collection of objects to be added to the buffer
	 */
	public void feed(Collection<T> collection) {
		for (T t : collection) {
			feed(t);
		}
	}

	/**
	 * Flushes the buffer and invokes the transformation function on any data
	 * currently in the buffer.
	 */
	public void flush() {
		List<T> input = new ArrayList<T>(buffer);
		buffer.clear();

		if (func != null) {
			pipe.feed(func.apply(input));
		}
	}

	// Pipeline redirect.
	public PipelineBuilder<R> via(Function<R, R> f) {
		return pipe.via(f);
	}

	@Override
	public PipelineBuilder<R> via(Consumer<R> consumer) {
		return pipe.via(consumer);
	}

	@Override
	public PipelineBuilder<R> filter(Predicate<R> predicate) {
		return pipe.filter(predicate);
	}

	@Override
	public PipelineBuilder<R> expand(Function<R, Collection<R>> f) {
		return pipe.expand(f);
	}

	@Override
	public <U> PipelineBuilder<U> transform(Function<R, U> f) {
		return pipe.transform(f);
	}

	@Override
	public PipelineSplitBuilder<R> split() {
		return pipe.split();
	}

	@Override
	public void to(Feedable<R> p) {
		pipe.to(p);
	}
}
