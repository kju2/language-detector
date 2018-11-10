package com.github.kju2.languagedetector.text;

import java.util.function.UnaryOperator;

/**
 * Takes a {@link StringBuilder} and modifies parts of the content. This will in most cases modify
 * the given {@link StringBuilder}, but it is also possible to return a new {@link StringBuilder}
 * instance.
 * <p>
 * TextModifiers are intended to be chained. To not impact the performance by creating and throwing
 * away strings all the time, we use a {@link StringBuilder} .
 */
@FunctionalInterface
public interface TextModifier extends UnaryOperator<StringBuilder> {
	/**
	 * No changes to the {@link UnaryOperator} necessary. We just want to make clear what the
	 * methods do that implement this interface.
	 */
}
