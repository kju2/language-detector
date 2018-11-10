package com.github.kju2.languagedetector.ngram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.kju2.languagedetector.language.Language;
import com.github.kju2.languagedetector.language.LanguageProfile;

/**
 * Contains frequency information for n-grams coming from multiple {@link LanguageProfile}s.
 * <p/>
 * For each n-gram string it knows the languages in which it occurs, and how frequent it occurs in
 * those languages in relation to other n-grams of the same length in those same languages.
 * <p/>
 * Immutable by definition (can't make Arrays unmodifiable).
 */
public final class NGramFrequencies {

	/**
	 * Key = ngram Value = array with probabilities per loaded language, in the same order as
	 * {@code langlist}.
	 */
	private final Map<String, float[]> nGramLanguageProbabilitiesMap;

	/**
	 * All the loaded languages, in exactly the same order as the data is in the float[] in
	 * wordLangProbMap. Example: if wordLangProbMap has an entry for the n-gram "foo" then for each
	 * locale in this langlist here it has a value there. Languages that don't know the n-gram have
	 * the value 0d.
	 */
	private final List<Language> languages;

	/**
	 * @param gramLengths for example [1,2,3]
	 * @throws java.lang.IllegalArgumentException if languageProfiles or gramLengths is empty, or if
	 *         one of the languageProfiles does not have the grams of the required sizes.
	 */
	public static NGramFrequencies of(Collection<LanguageProfile> languageProfiles) throws IllegalArgumentException {
		int languagesIndex = -1;
		List<Language> languages = new ArrayList<>(languageProfiles.size());
		Map<String, float[]> nGramLanguageProbabilitiesMap = new HashMap<>();

		for (LanguageProfile languageProfile : languageProfiles) {
			languagesIndex += 1;
			languages.add(languageProfile.getLanguage());

			for (Map.Entry<String, Integer> x : languageProfile.getNGrams().entrySet()) {
				String nGram = x.getKey();
				float frequency = x.getValue();
				float[] nGramLanguageProbabilities = nGramLanguageProbabilitiesMap.get(nGram);
				if (nGramLanguageProbabilities == null) {
					nGramLanguageProbabilities = new float[languageProfiles.size()];
					nGramLanguageProbabilitiesMap.put(nGram, nGramLanguageProbabilities);
				}
				float nGramLanguageProbability = frequency / languageProfile.getNumberOfOccurrencesForNGramsOfLength(nGram.length());
				nGramLanguageProbabilities[languagesIndex] = nGramLanguageProbability;
			}
		}
		return new NGramFrequencies(languages, nGramLanguageProbabilitiesMap);
	}

	private NGramFrequencies(List<Language> languages, Map<String, float[]> nGramLanguageProbabilitiesMap) {
		this.languages = languages;
		this.nGramLanguageProbabilitiesMap = nGramLanguageProbabilitiesMap;
	}

	public int numberOfLanguages() {
		return languages.size();
	}

	public Language getLanguage(int index) {
		return languages.get(index);
	}

	/**
	 * Don't modify this data structure! (Can't make array immutable...)
	 *
	 * @return null if no language profile knows that ngram. entries are 0 for languages that don't
	 *         know that ngram at all. The array is in the order of the {@link #getLanguageList()}
	 *         language list, and has exactly that size. impl note: this way the caller can handle
	 *         it more efficient than returning an empty array.
	 */
	public float[] getProbabilities(String ngram) {
		return nGramLanguageProbabilitiesMap.get(ngram);
	}
}
