/*
 * Copyright 2011 Nakatani Shuyo Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.optimaize.langdetect.text;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author Nakatani Shuyo
 * @author Fabian Kessler
 * @author kju2
 */
public class CharNormalizerTest {

	@ParameterizedTest
	@ValueSource(chars = { 'a', 'A', 'z', 'Z', 'o', '\u0061', '\u007a' })
	void givenALatinCharacterThenReturnItUnchanged(char input) {
		assertEquals(input, CharNormalizer.normalize(input));
	}

	@ParameterizedTest
	@ValueSource(chars = { '#', ',', '\'', '"', '!', '@', '.', ':', ';', '$', '%', '^', '&', '*', '(', ')', '+', '-', '=', '}', ' ', '\u00a0', '\u0000', '\u0009', '\u00a1', '0', '5', '9' })
	void givenPunctuationAndSimilarCharactersThenReturnASpace(char input) {
		assertEquals(' ', CharNormalizer.normalize(input));
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
