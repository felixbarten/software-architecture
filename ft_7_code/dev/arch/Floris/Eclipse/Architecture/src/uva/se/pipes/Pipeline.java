package uva.se.pipes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * LINQ-like pipeline which allows the chaining of functions acting on multiple
 * data types. Data spawns from a sink and passes through several stages before
 * ending in an (optional) sink. Stages can perform pass-through, filtering and
 * expansion and are configured through a builder function.
 * 
 * Internally it uses raw types, which is bad, but by limiting access through
 * the builder interface and finalizing it, it's somewhat safe.
 * 
 * -- edit: just found out about java.util.Stream, which seems to do the
 * same, but doesn't support a shifting data type and expansion.
 * 
 * @author Floris den Heijer
 *
 */
public final class Pipeline {

	// Source and stages.
	private Supplier<?> _source;
	private boolean _isSourceCollection;
	private final List<Stage> _stages = new ArrayList<Stage>();

	// Keeps track of used builders to avoid type-unsafe insertions.
	private final HashSet<PipelineBuilder<?>> _usedBuilders = new HashSet<PipelineBuilder<?>>();

	/**
	 * Sets up the source generating function and returns a pipeline builder for
	 * it's output type.
	 * 
	 * @param source
	 *            Object implementing the {@link java.util.function.Supplier}
	 *            interface, returning T.
	 * @return Returns a reference to the builder.
	 */
	public <T> PipelineBuilder<T> from(Supplier<T> source) {
		setSource(source, false);
		return builder();
	}

	/**
	 * Sets up the source generating function and returns a pipeline builder for
	 * the type held in the output collection.
	 * 
	 * @param source
	 *            Object implementing the {@link java.util.function.Supplier}
	 *            interface, returning a collection of T's.
	 * @return Returns a reference to the builder.
	 */
	public <T> PipelineBuilder<T> fromCollection(Supplier<Collection<T>> source) {
		setSource(source, true);
		return builder();
	}

	/**
	 * Runs the pipeline; i.e. retrieves a value from the source and puts it
	 * through the stages and (optional) sink.
	 */
	@SuppressWarnings("rawtypes")
	public void run() {

		if (_source == null)
			return;

		// If from single source, supply and run.
		Object s = _source.get();
		if (!_isSourceCollection) {
			runStages(s, _stages);
			return;
		}

		// Otherwise, iterate over results and run each.
		Collection c = (Collection) s;
		if (c != null) {
			for (Object o : c) {
				runStages(o, _stages);
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void runStages(Object o, List<Stage> stages) {

		if (o == null)
			return;

		// Run the object through (remaining) stages.
		int n = stages.size();
		for (int i = 0; i < n; i++) {
			Stage s = stages.get(i);

			// If filter, apply predicate and abort on failure.
			if (s.pred != null && !((Predicate) s.pred).test(o)) {
				return;
			}

			// If pass-through, run.
			if (s.func != null) {
				o = ((Function) s.func).apply(o);
				continue;
			}

			// If pass-through consumer, run and continue.
			if (s.doFunc != null) {
				((Consumer) s.doFunc).accept(o);
				continue;
			}

			// If sink, run and return.
			if (s.sink != null) {
				((Consumer) s.sink).accept(o);
				return;
			}

			// If expander, recurse on sublist and return.
			if (s.expand != null) {
				Collection c = (Collection) ((Function) s.expand).apply(o);
				if (c != null) {
					List<Stage> rest = stages.subList(i + 1, n);
					for (Object e : c) {
						runStages(e, rest);
					}
				}

				return;
			}
		}
	}

	private void setSource(Supplier<?> source, boolean sourceIsCollection) {
		_source = source;
		_isSourceCollection = sourceIsCollection;
		_stages.clear();
		_usedBuilders.clear();
	}

	/**
	 * This works through the magic of type inference.
	 * 
	 * @return Anonymous implementation of the builder interface, adds to used
	 *         builders.
	 */
	private <T> PipelineBuilder<T> builder() {
		return new PipelineBuilder<T>() {
			public PipelineBuilder<T> doVia(Consumer<T> c) {
				addStage(new Stage(c, null, null, null, null));
				return builder();
			}

			public <T2> PipelineBuilder<T2> via(Function<T, T2> f) {
				addStage(new Stage(null, f, null, null, null));
				return builder();
			}

			public void to() {
				to(null);
			}

			public void to(Consumer<T> c) {
				addStage(new Stage(null, null, null, null, c));
			}

			public PipelineBuilder<T> filter(Predicate<T> p) {
				addStage(new Stage(null, null, p, null, null));
				return builder();
			}

			public <T2> PipelineBuilder<T2> expand(Function<T, Collection<T2>> f) {
				addStage(new Stage(null, null, null, f, null));
				return builder();
			}

			void addStage(Stage s) {
				if (_usedBuilders.contains(this)) {
					throw new RuntimeException("Builder already used! You can't use a builder twice.");
				}

				_usedBuilders.add(this);
				_stages.add(s);
			}
		};
	}

	class Stage {
		Stage(Consumer<?> doFunc, Function<?, ?> func, Predicate<?> pred, Function<?, ?> expand, Consumer<?> sink) {
			this.doFunc = doFunc;
			this.func = func;
			this.pred = pred;
			this.expand = expand;
			this.sink = sink;
		}

		Consumer<?> doFunc;
		Function<?, ?> func;
		Predicate<?> pred;
		Function<?, ?> expand; // can't use Collection<?>.. :(
		Consumer<?> sink;
	}
}