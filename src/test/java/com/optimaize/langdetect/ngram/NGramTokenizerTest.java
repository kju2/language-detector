package com.optimaize.langdetect.ngram;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class NGramTokenizerTest {

	private static final NGramTokenizer tokenizer = new NGramTokenizer(1, 3, NGramFilters.HAS_NON_WHITESPACE_CHARACTERS);

	@Test
	void minNGramSizeIsBelowOneThrowsException() {
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> createNGramTokenizer(0, 4));
		assertEquals("'minNGramSize' must be greater than zero.", exception.getMessage());
	}

	@Test
	void maxNGramSizeIsSmallerThanMinNGramSizeThrowsException() {
		Throwable exception = assertThrows(IllegalArgumentException.class, () -> createNGramTokenizer(5, 4));
		assertEquals("'minNGramSize' must not be greater than 'maxNGramSize'.", exception.getMessage());
	}

	@Test
	void givenNoTokenFilterThenNothingIsFiltered() {
		NGramTokenizer tokenizerWithNoFilter = new NGramTokenizer(1, 2, null);
		assertEquals(Arrays.asList(" ", " a", "a", "a ", " ", "  ", " ", " b", "b", "b ", " "), tokenizerWithNoFilter.tokenize("a  b"));
	}

	@Test
	void givenTheHasNonWhitespaceCharactersFilterThenAllTokensHaveAtLeastOneCharacter() {
		NGramTokenizer tokenizerWithNoFilter = new NGramTokenizer(1, 2, NGramFilters.HAS_NON_WHITESPACE_CHARACTERS);
		assertEquals(Arrays.asList(" a", "a", "a ", " b", "b", "b "), tokenizerWithNoFilter.tokenize("a b"));
	}

	@ParameterizedTest(name = "{index}: ''{0}''")
	@MethodSource("tokenizerProvider")
	void tokenize(String input, List<String> expectedOutput) {
		assertEquals(expectedOutput, tokenizer.tokenize(input));
	}

	static Stream<Arguments> tokenizerProvider() {
		return Stream.of(arguments(null, Collections.emptyList()), arguments("", Collections.emptyList()), arguments(" ", Collections.emptyList()),
				arguments("a", Arrays.asList(" a", " a ", "a", "a ")), arguments(" a", Arrays.asList(" a", " a ", "a", "a ")), arguments("a ", Arrays.asList(" a", " a ", "a", "a ")),
				arguments(" a ", Arrays.asList(" a", " a ", "a", "a ")), arguments("ab", Arrays.asList(" a", " ab", "a", "ab", "ab ", "b", "b ")),
				arguments("aa b", Arrays.asList(" a", " aa", "a", "aa", "aa ", "a", "a ", "a b", " b", " b ", "b", "b ")),
				arguments("Foo Bar", Arrays.asList(" F", " Fo", "F", "Fo", "Foo", "o", "oo", "oo ", "o", "o ", "o B", " B", " Ba", "B", "Ba", "Bar", "a", "ar", "ar ", "r", "r ")));
	}

	private NGramTokenizer createNGramTokenizer(int minNGramLength, int maxNGramLength) {
		return new NGramTokenizer(minNGramLength, maxNGramLength, null);
	}
}
