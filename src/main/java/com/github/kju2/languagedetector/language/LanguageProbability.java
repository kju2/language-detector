package com.github.kju2.languagedetector.language;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class LanguageProbability implements Comparable<LanguageProbability> {

	private final Language language;
	private final float probability;

	/**
	 * Order by probability descending (1.0 to 0.0). Then order by language ascending (a to z).
	 */
	@Override
	public int compareTo(LanguageProbability o) {
		int compare = Double.compare(o.probability, probability);
		return compare != 0 ? compare : language.compareTo(o.getLanguage());
	}
}
