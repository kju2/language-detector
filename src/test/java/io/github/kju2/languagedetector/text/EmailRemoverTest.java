package io.github.kju2.languagedetector.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.kju2.languagedetector.text.TextModifier;
import io.github.kju2.languagedetector.text.TextModifiers;

public class EmailRemoverTest {

	private static final TextModifier remover = TextModifiers.EMAIL_REMOVER;

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
		StringBuilder input = new StringBuilder("Example: example@example.com is a good example!");
		StringBuilder output = remover.apply(input);
		assertEquals(input, output);
		assertEquals("Example:  is a good example!", output.toString());
	}

	@Test
	void givenAStringBuilderWithSeveralUrlsThenReturnTheStringbuilderWithoutTheUrls() {
		StringBuilder input = new StringBuilder("Example: example@example.com is a good example as is \"info@example.org\"!");
		StringBuilder output = remover.apply(input);
		assertEquals(input, output);
		assertEquals("Example:  is a good example as is \"\"!", output.toString());
	}
}
