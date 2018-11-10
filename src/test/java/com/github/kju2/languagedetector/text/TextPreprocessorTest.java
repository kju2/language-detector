package com.github.kju2.languagedetector.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TextPreprocessorTest {

	@Test
	void givenATextForDetectionThenReturnItCleanedAndNormalized() {
		TextPreprocessor preprocessor = TextPreprocessor.forDetection();
		StringBuilder input = new StringBuilder("This (\u064a) is an English text - example@example.com - with a lot of noise (see http://example.com)!!!");
		String expectedOutput = " This     is an English text      with a lot of noise  see     ";
		assertEquals(expectedOutput, preprocessor.prepare(input).toString());
	}
}
