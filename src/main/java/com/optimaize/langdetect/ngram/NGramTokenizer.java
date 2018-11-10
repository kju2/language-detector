package com.optimaize.langdetect.ngram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class NGramTokenizer {
	private static final char SPACE = ' ';

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
		CharSequence spacePaddedText = ensureSpacePadding(text);
		List<String> ngrams = new ArrayList<>(maxNumberOfNGrams(spacePaddedText));
		int end = spacePaddedText.length();
		for (int start = 0; start < end; start++) {
			for (int n = minNGramLength; n <= maxNGramLength && start + n <= end; n++) {
				String ngram = spacePaddedText.subSequence(start, start + n).toString();
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

	private CharSequence ensureSpacePadding(CharSequence text) {
		int end = text.length() - 1;
		if (end < 0 || (text.charAt(0) == SPACE && text.charAt(end) == SPACE)) {
			return text;
		}

		StringBuilder sb = new StringBuilder();
		if (text.charAt(0) != SPACE) {
			sb.append(SPACE);
		}
		sb.append(text);
		if (text.charAt(end) != SPACE) {
			sb.append(SPACE);
		}
		return sb;
	}
}
