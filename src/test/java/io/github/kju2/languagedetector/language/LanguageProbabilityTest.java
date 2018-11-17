package io.github.kju2.languagedetector.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import io.github.kju2.languagedetector.language.Language;
import io.github.kju2.languagedetector.language.LanguageProbability;

public class LanguageProbabilityTest {

	@Test
	void givenTwoLanguageProbabilitiesForTheSameLanguageWithTheSameProbabilityThenEqualsReturnsTrue() {
		LanguageProbability lp1 = new LanguageProbability(Language.GERMAN, 0.7f);
		LanguageProbability lp2 = new LanguageProbability(Language.GERMAN, 0.7f);
		assertEquals(lp1, lp2);
		assertEquals(0, lp1.compareTo(lp2));
	}

	@Test
	void givenTwoLanguageProbabilitiesForTheSameLanguageWithTheDifferentProbabilitiesThenEqualsReturnsFalse() {
		LanguageProbability lp1 = new LanguageProbability(Language.GERMAN, 0.7f);
		LanguageProbability lp2 = new LanguageProbability(Language.GERMAN, 0.5f);
		assertNotEquals(lp1, lp2);
		assertEquals(-1, lp1.compareTo(lp2));
	}

	@Test
	void givenTwoLanguageProbabilitiesForDifferentLanguagesThenEqualsReturnsFalse() {
		LanguageProbability lp1 = new LanguageProbability(Language.GERMAN, 0.7f);
		LanguageProbability lp2 = new LanguageProbability(Language.AFRIKAANS, 0.5f);
		assertNotEquals(lp1, lp2);
		assertEquals(-1, lp1.compareTo(lp2));
	}
}
