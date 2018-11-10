package com.optimaize.langdetect.ngram;

import java.util.function.Predicate;

public class NGramFilters {

	private NGramFilters() {}

	public static final Predicate<String> FILTER_NOTHING = s -> true;
	public static final Predicate<String> HAS_NON_WHITESPACE_CHARACTERS = s -> !s.trim().isEmpty();
}
