package com.optimaize.langdetect;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import com.optimaize.langdetect.cybozu.util.Util;
import com.optimaize.langdetect.language.Language;
import com.optimaize.langdetect.language.LanguageProbability;
import com.optimaize.langdetect.ngram.NGramFilters;
import com.optimaize.langdetect.ngram.NGramFrequencies;
import com.optimaize.langdetect.ngram.NGramTokenizer;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.textOld.CommonTextObjectFactories;
import com.optimaize.langdetect.textOld.TextObjectFactory;

/**
 * @author Nakatani Shuyo
 * @author Fabian Kessler
 * @author Elmer Garduno
 */
public class LanguageDetector {
	/**
	 * Additive (Laplace/Lidstone) smoothing parameter.
	 */
	private static final float ALPHA = 0.05f;
	private static final int ITERATION_LIMIT = 1000;
	private static final float CONV_THRESHOLD = 0.99999f;
	private static final int BASE_FREQ = 10000;
	private static final TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();
	/**
	 * This is used when no custom seed was passed in. By using the same seed for different calls,
	 * the results are consistent also. Changing this number means that users of the library might
	 * suddenly see other results after updating. So don't change it hastily. I chose a prime number
	 * *clueless*. See https://github.com/optimaize/language-detector/issues/14
	 */
	private static final long DEFAULT_SEED = 41L;

	public final NGramFrequencies ngramFrequencyData;

	private final NGramTokenizer nGramTokenizer = new NGramTokenizer(1, 3, NGramFilters.HAS_NON_WHITESPACE_CHARACTERS);
	private final float alpha = 0.05f;

	/**
	 * Returns a language if the best detected language has at least this probability.
	 */
	private final float minimalConfidence = 0.9999f;

	public LanguageDetector() throws IOException {
		ngramFrequencyData = NGramFrequencies.of(new LanguageProfileReader().readAllBuiltIn());
	}

	public Language detectPrimaryLanguageOf(CharSequence text) {
		if (text == null || text.length() < 1) {
			return Language.UNKNOWN;
		}

		PriorityQueue<LanguageProbability> probabilities = sortByProbability(detectBlock(textObjectFactory.forText(text)));
		LanguageProbability best = probabilities.poll();
		if (best != null && best.getProbability() >= minimalConfidence) {
			return best.getLanguage();
		}
		return Language.UNKNOWN;
	}

	private PriorityQueue<LanguageProbability> sortByProbability(float[] probabilities) {
		PriorityQueue<LanguageProbability> list = new PriorityQueue<>();
		for (int i = 0; i < probabilities.length; ++i) {
			list.add(new LanguageProbability(ngramFrequencyData.getLanguage(i), probabilities[i]));
		}
		return list;
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
	public float[] detectBlock(CharSequence text) {
		List<String> ngrams = nGramTokenizer.tokenize(text);
		if (ngrams.isEmpty()) {
			return new float[] {};
		}
		float[] langprob = new float[ngramFrequencyData.numberOfLanguages()];
		Random rand = new Random(DEFAULT_SEED);
		float[] probabilities = priorProbabilities();
		float alpha = (float) (this.alpha + (rand.nextGaussian() * ALPHA));

		for (int i = 0; i < ITERATION_LIMIT; i++) {
			int r = rand.nextInt(ngrams.size());
			updateLangProb(probabilities, ngrams.get(r), alpha);
			if (i % 5 == 0 && Util.normalizeProb(probabilities) > CONV_THRESHOLD) {
				break; // terminate early if a language has a very high probability
			}
		}
		for (int j = 0; j < langprob.length; ++j) {
			langprob[j] += probabilities[j];
		}
		return langprob;
	}
}
