package nl.uva.se.funcpipes;

import java.util.function.Consumer;

/**
 * Builder interface for the split pipeline operation.
 * 
 * @author Floris den Heijer
 *
 * @param <T> Data type of element in the pipeline.
 */
public interface PipelineSplitBuilder<T> {
	/**
	 * Connects split to an existing pipeline.
	 * 
	 * @param p Pipeline to connect.
	 * @return Builder for the remaining splits.
	 */
	PipelineSplitBuilder<T> to(Pipeline<T> p);
	
	/**
	 * Creates a new pipeline and returns the builder, allowing in-line
	 * configuration.
	 * @param config Lambda configuring the new pipeline.
	 * @return Builder for the remaining splits.
	 */
	PipelineSplitBuilder<T> to(Consumer<PipelineBuilder<T>> config);
}
