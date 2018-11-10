package com.github.kju2.languagedetector.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class CharNormalizerTest {

	@ParameterizedTest
	@ValueSource(chars = { 'a', 'A', 'z', 'Z', 'o', '\u0061', '\u007a' })
	void givenALatinCharacterThenReturnItUnchanged(char input) {
		assertEquals(input, CharNormalizer.normalize(input));
	}

	@ParameterizedTest
	@ValueSource(chars = { '#', ',', '\'', '"', '!', '@', '.', ':', ';', '$', '%', '^', '&', '*', '(', ')', '+', '-', '=', '}', ' ', '\u00a0', '\u0000', '\u0009', '\u00a1', '0', '5', '9' })
	void givenAPunctuationOrSimilarCharactersThenReturnASpace(char input) {
		assertEquals(Texts.SPACE, CharNormalizer.normalize(input));
	}

	@ParameterizedTest
	@ValueSource(chars = { ' ', '\n', '\r', '\t', '\u0008', '\u0009', '\u00a0', '\u2007', '\u202f' })
	void givenAWhitespaceCharacterThenReturnASpace(char input) {
		assertEquals(Texts.SPACE, CharNormalizer.normalize(input));
	}

	@ParameterizedTest
	@CsvSource({ "\u06cc, \u064a", "\u1ea0, \u1ec3", "\u3041, \u3042", "\u30b4, \u30a2", "\u3105, \u3105", "\u31B7, \u3105", "\uac24, \uac00" })
	void miscNormalization(char input, char expectedOutput) {
		assertEquals(expectedOutput, CharNormalizer.normalize(input));
	}

	@ParameterizedTest
	@CsvSource({ "\u66A8, \u502A", "\u5EFC, \u5039", "\u5BF6, \u4E9E", "\u58EE, \u4E71", "\u86FE, \u4E5E", "\u5141, \u4E19", "\u5800, \u5742", "\u7984, \u5F66" })
	void kanjiNormalization(char input, char expectedOutput) {
		assertEquals(expectedOutput, CharNormalizer.normalize(input));
	}
}
