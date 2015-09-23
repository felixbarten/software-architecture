package uva.se.pipes;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Builder interface for pipeline construction starting from the generic
 * argument.
 * 
 * @author Floris den Heijer
 *
 * @param <T> Type serving as input
 */
public interface PipelineBuilder<T> {

	/**
	 * Adds a transformation to the pipeline.
	 * 
	 * @param f
	 *            Transformation function.
	 * @return Builder over the output type.
	 */
	<T2> PipelineBuilder<T2> via(Function<T, T2> f);

	/**
	 * Adds a consumer to the pipeline.
	 * 
	 * @param c
	 *            Consumer function (void return type).
	 * @return Builder over the input type.
	 */
	PipelineBuilder<T> doVia(Consumer<T> c);

	/**
	 * Adds a filter to the pipeline.
	 * 
	 * @param f
	 *            Filter predicate.
	 * @return Builder over the input type.
	 */
	PipelineBuilder<T> filter(Predicate<T> f);

	/**
	 * Adds an expander to the pipeline, which transforms an element into a
	 * collection. Each element of the collection will be passed along
	 * individually.
	 * 
	 * @param c
	 *            Transformation function.
	 * @return Builder over the type in the output collection.
	 */
	<T2> PipelineBuilder<T2> expand(Function<T, Collection<T2>> c);

	/**
	 * Adds a null sink to the pipeline and closes the builder.
	 */
	void to();

	/**
	 * Adds a consuming sink to the pipeline and closes the builder.
	 * 
	 * @param c
	 *            Consumer function (void return type).
	 */
	void to(Consumer<T> c);
}
