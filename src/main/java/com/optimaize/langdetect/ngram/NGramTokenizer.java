package com.optimaize.langdetect.ngram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class NGramTokenizer {
	private final int minNGramLength;
	private final int maxNGramLength;
	private final Predicate<String> tokenFilter;

	public NGramTokenizer(int minNGramLength, int maxNGramLength, Predicate<String> tokenFilter) {
		if (minNGramLength < 1) {
			throw new IllegalArgumentException("'minNGramLength' must be greater than zero.");
		}
		if (minNGramLength > maxNGramLength) {
			throw new IllegalArgumentException("'minNGramLength' must not be greater than 'maxNGramLength'.");
		}
		this.minNGramLength = minNGramLength;
		this.maxNGramLength = maxNGramLength;
		this.tokenFilter = tokenFilter != null ? tokenFilter : NGramFilters.FILTER_NOTHING;
	}

	public List<String> tokenize(CharSequence text) {
		if (text == null || text.length() == 0) {
			return Collections.emptyList();
		}
		List<String> ngrams = new ArrayList<>(maxNumberOfNGrams(text));
		int end = text.length();
		for (int start = 0; start < end; start++) {
			for (int n = minNGramLength; n <= maxNGramLength && start + n <= end; n++) {
				String ngram = text.subSequence(start, start + n).toString();
				if (tokenFilter.test(ngram)) {
					ngrams.add(ngram);
				}
			}
		}
		return ngrams;
	}

	private int maxNumberOfNGrams(CharSequence spacePaddedText) {
		return spacePaddedText.length() * maxNGramLength;
	}
}
