package com.optimaize.langdetect.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class UrlRemoverTest {

	private static final TextModifier remover = TextModifiers.URL_REMOVER;

	@ParameterizedTest
	@ValueSource(strings = { "", "abc", "abc def ghi" })
	void givenAStringBuilderWithoutAnUrlThenReturnTheStringbuilderUnchanged(String text) {
		StringBuilder input = new StringBuilder(text);
		StringBuilder output = remover.apply(input);
		assertEquals(input, output);
		assertEquals(input.toString(), output.toString());
	}

	@Test
	void givenAStringBuilderWithAnUrlThenReturnTheStringbuilderWithoutTheUrl() {
		StringBuilder input = new StringBuilder("Example: http://example.com is a good example!");
		StringBuilder output = remover.apply(input);
		assertEquals(input, output);
		assertEquals("Example:  is a good example!", output.toString());
	}

	@Test
	void givenAStringBuilderWithSeveralUrlsThenReturnTheStringbuilderWithoutTheUrls() {
		StringBuilder input = new StringBuilder("Example: http://example.com is a good example as is \"https://wikipedia.org\"!");
		StringBuilder output = remover.apply(input);
		assertEquals(input, output);
		assertEquals("Example:  is a good example as is \"\"!", output.toString());
	}
}
