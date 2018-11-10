package com.github.kju2.languagedetector;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import com.github.kju2.languagedetector.language.Language;
import com.github.kju2.languagedetector.language.LanguageProbability;
import com.github.kju2.languagedetector.language.LanguageProfiles;
import com.github.kju2.languagedetector.ngram.NGramFilters;
import com.github.kju2.languagedetector.ngram.NGramFrequencies;
import com.github.kju2.languagedetector.ngram.NGramTokenizer;
import com.github.kju2.languagedetector.text.TextPreprocessor;

public class LanguageDetector {
	/**
	 * Additive (Laplace/Lidstone) smoothing parameter.
	 */
	private static final float ALPHA = 0.05f;
	private static final int ITERATION_LIMIT = 1000;
	private static final float CONV_THRESHOLD = 0.99999f;
	private static final int BASE_FREQ = 10000;
	/**
	 * This is used when no custom seed was passed in. By using the same seed for different calls,
	 * the results are consistent also. Changing this number means that users of the library might
	 * suddenly see other results after updating. So don't change it hastily.
	 */
	private static final long DEFAULT_SEED = 41L;

	public final NGramFrequencies ngramFrequencyData;

	private final TextPreprocessor textPreprocessor = TextPreprocessor.forDetection();
	private final NGramTokenizer nGramTokenizer = new NGramTokenizer(1, 3, NGramFilters.HAS_NON_WHITESPACE_CHARACTERS);
	private final float alpha = 0.05f;

	/**
	 * Returns a language if the best detected language has at least this probability.
	 */
	private final float minimalConfidence = 0.9999f;

	public LanguageDetector() throws IOException {
		ngramFrequencyData = NGramFrequencies.of(LanguageProfiles.builtInLanguages().values());
	}

	public Language detectPrimaryLanguageOf(String text) {
		return detectPrimaryLanguageOf(text != null ? new StringBuilder(text) : null);
	}

	public Language detectPrimaryLanguageOf(StringBuilder text) {
		if (text == null || text.length() < 1) {
			return Language.UNKNOWN;
		}
		CharSequence preprocessedText = textPreprocessor.prepare(text);
		List<String> tokenizedText = nGramTokenizer.tokenize(preprocessedText);
		PriorityQueue<LanguageProbability> languageCandidates = identifyLanguageCandidates(tokenizedText);
		return selectPrimaryLanguage(languageCandidates);
	}

	private Language selectPrimaryLanguage(PriorityQueue<LanguageProbability> languageCandidates) {
		LanguageProbability best = languageCandidates.poll();
		if (best != null && best.getProbability() >= minimalConfidence) {
			return best.getLanguage();
		}
		return Language.UNKNOWN;
	}

	private void updateLangProb(float[] prob, String ngram, float alpha) {
		float[] langProbMap = ngramFrequencyData.getProbabilities(ngram);
		if (langProbMap != null) {
			float weight = alpha / BASE_FREQ;
			for (int i = 0; i < prob.length; ++i) {
				prob[i] *= (weight + langProbMap[i]);
			}
		}
	}

	private float[] priorProbabilities() {
		int size = ngramFrequencyData.numberOfLanguages();

		float[] probabilities = new float[size];
		Arrays.fill(probabilities, 1.0f / size);

		return probabilities;
	}

	/**
	 * This is the original algorithm used for all text length. It is inappropriate for short text.
	 */
	public PriorityQueue<LanguageProbability> identifyLanguageCandidates(List<String> ngrams) {
		if (ngrams.isEmpty()) {
			return new PriorityQueue<>();
		}
		float[] langprob = new float[ngramFrequencyData.numberOfLanguages()];
		Random rand = new Random(DEFAULT_SEED);
		float[] probabilities = priorProbabilities();
		float alpha = (float) (this.alpha + (rand.nextGaussian() * ALPHA));

		for (int i = 0; i < ITERATION_LIMIT; i++) {
			int r = rand.nextInt(ngrams.size());
			updateLangProb(probabilities, ngrams.get(r), alpha);
			if (i % 5 == 0 && normalizeAndReturnMaximum(probabilities) > CONV_THRESHOLD) {
				break; // terminate early if a language has a very high probability
			}
		}
		for (int j = 0; j < langprob.length; ++j) {
			langprob[j] += probabilities[j];
		}
		return sortByProbability(langprob);
	}

	private PriorityQueue<LanguageProbability> sortByProbability(float[] probabilities) {
		PriorityQueue<LanguageProbability> list = new PriorityQueue<>();
		for (int i = 0; i < probabilities.length; ++i) {
			list.add(new LanguageProbability(ngramFrequencyData.getLanguage(i), probabilities[i]));
		}
		return list;
	}

	public float normalizeAndReturnMaximum(float[] probabilities) {
		float sum = 0f;
		for (int i = 0; i < probabilities.length; ++i) {
			sum += probabilities[i];
		}

		float max = 0f;
		for (int i = 0; i < probabilities.length; ++i) {
			float p = probabilities[i] = probabilities[i] / sum;
			max = Math.max(max, p);
		}
		return max;
	}
}
