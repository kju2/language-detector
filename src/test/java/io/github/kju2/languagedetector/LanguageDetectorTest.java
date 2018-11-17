package io.github.kju2.languagedetector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import io.github.kju2.languagedetector.LanguageDetector;
import io.github.kju2.languagedetector.language.Language;

public class LanguageDetectorTest {
	private static LanguageDetector detector;

	@BeforeAll
	static void setup() throws IOException {
		detector = new LanguageDetector();
	}

	@Test
	void givenAnEnglishTextThenTheDetectedLanguageIsEnglish() {
		// Source of text: https://en.wikipedia.org/wiki/Wikipedia:About
		String text = "Wikipedia is written collaboratively by largely anonymous volunteers who write without pay. Anyone with Internet access can write and make changes to Wikipedia articles, except in limited cases where editing is restricted to prevent disruption or vandalism.";
		assertEquals(Language.ENGLISH, detector.detectPrimaryLanguageOf(text));
	}

	@Test
	void givenNullThenNoLanguageIsDetected() {
		String text = null;
		assertEquals(Language.UNKNOWN, detector.detectPrimaryLanguageOf(text));
	}

	@Test
	void givenAnEmptyTextThenNoLanguageIsDetected() {
		String text = "";
		assertEquals(Language.UNKNOWN, detector.detectPrimaryLanguageOf(text));
	}

	@Test
	void givenAnTextWithOnlyWhitespaceThenNoLanguageIsDetected() {
		String text = "       ";
		assertEquals(Language.UNKNOWN, detector.detectPrimaryLanguageOf(text));
	}

	@Disabled // TODO Language models need to be rebuild before this can work.
	@Test
	void givenAnTextWithTabsAndNewlinesThenNoLanguageIsDetected() {
		String text = "\\u005Cu0020";
		assertEquals(Language.UNKNOWN, detector.detectPrimaryLanguageOf(text));
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/languages-detected-correctly.csv", numLinesToSkip = 2)
	void languagesDetectedCorrectly(Language language, String text) {
		assertEquals(language, detector.detectPrimaryLanguageOf(text));
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/languages-detected-incorrectly.csv", numLinesToSkip = 2)
	void languagesDetectedIncorrectly(Language language, String text) {
		assertNotEquals(language, detector.detectPrimaryLanguageOf(text));
	}
}
