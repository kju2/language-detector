package io.github.kju2.languagedetector.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.kju2.languagedetector.text.TextModifier;
import io.github.kju2.languagedetector.text.TextModifiers;

public class SpacePadderTest {
	private static final TextModifier modifier = TextModifiers.SPACE_PADDER;

	@ParameterizedTest
	@ValueSource(strings = { "", " abc ", " abc def ghi " })
	void givenAStringBuilderWithoutASpaceAtTheBeginningAndEndReturnTheStringbuilderUnchanged(String text) {
		StringBuilder input = new StringBuilder(text);
		StringBuilder output = modifier.apply(input);
		assertEquals(input, output);
		assertEquals(input.toString(), output.toString());
	}

	@ParameterizedTest
	@CsvSource({ "'a', ' a '", "' b', ' b '", "'c ', ' c '", "'a b', ' a b '" })
	void givenAStringBuilderWithoutASpaceAtTheBeginningAndOrEndThenReturnAStringBuilderWithASpaceAtTheBeginningAndEnd(String input, String expectedOutput) {
		assertEquals(expectedOutput, modifier.apply(new StringBuilder(input)).toString());
	}
}
