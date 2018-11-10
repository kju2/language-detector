package com.github.kju2.languagedetector.language;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;

/**
 * A language profile stores how frequent certain n-grams are in a language.
 */
public final class LanguageProfile {

	@Getter
	private final Language language;

	/**
	 * The keys are the n-grams and the value is how often the n-gram occurred in the training data.
	 */
	@Getter
	private final Map<String, Integer> nGrams;

	/**
	 * The keys are the length o the n-gram (e.g. 1 for unigram, 2 for bigram, ...). The values are
	 * how many n-grams with this length were in the training data.
	 */
	private final Map<Integer, Integer> nGramLengthCounter;

	public LanguageProfile(Language language, Map<String, Integer> nGrams, Map<Integer, Integer> nGramCounter) {
		this.language = language;
		this.nGrams = Collections.unmodifiableMap(nGrams);
		nGramLengthCounter = Collections.unmodifiableMap(nGramCounter);
	}

	public static LanguageProfile of(Language language, Map<String, Integer> ngrams) {
		Map<Integer, Integer> nGramCounter = new HashMap<>(4);
		for (Entry<String, Integer> x : ngrams.entrySet()) {
			int key = x.getKey().length();
			Integer counter = nGramCounter.getOrDefault(key, 0);
			nGramCounter.put(key, counter + x.getValue());
		}
		return new LanguageProfile(language, ngrams, nGramCounter);
	}

	public static LanguageProfile read(Language language, InputStream inputStream) throws IOException {
		HashMap<String, Integer> ngrams = new HashMap<>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("utf-8")))) {
			reader.lines().forEach(line -> {
				int separatorIndex = line.indexOf('\t');
				int frequency = Integer.parseInt(line.substring(0, separatorIndex));
				String nGram = line.substring(separatorIndex + 1, line.length());
				ngrams.put(nGram, frequency);
			});
		}

		return LanguageProfile.of(language, ngrams);
	}

	/**
	 * Tells how often all n-grams of a certain length occurred.
	 * 
	 * @return 0 if no n-grams with that length occurred during training, otherwise the number of
	 *         times n-grams of such length were encountered.
	 */
	public long getNumberOfOccurrencesForNGramsOfLength(int nGramLength) {
		return nGramLengthCounter.getOrDefault(nGramLength, 0);
	}

	/**
	 * Writes this {@link LanguageProfile} to an OutputStream in UTF-8.
	 * <p>
	 * Format is: ${frequency}\t${n-gram}\n
	 */
	public void write(OutputStream outputStream) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("utf-8")))) {
			Iterator<String> iter = nGrams.entrySet()
					.stream()
					.sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
					.map(e -> e.getValue().toString() + "\t" + e.getKey() + "\n")
					.iterator();
			while (iter.hasNext()) {
				writer.write(iter.next());
			}
		}
	}
}
