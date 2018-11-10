package com.optimaize.langdetect.text;

import java.util.Arrays;
import java.util.List;

public class TextPreprocessor {

	private final List<TextModifier> pipeline;

	private TextPreprocessor(List<TextModifier> pipeline) {
		this.pipeline = pipeline;
	}

	public static TextPreprocessor forDetection() {
		return new TextPreprocessor(Arrays.asList(TextModifiers.URL_REMOVER, TextModifiers.EMAIL_REMOVER, new MinorityScriptsRemover(), new CharNormalizer(), TextModifiers.SPACE_PADDER));
	}

	public CharSequence prepare(StringBuilder text) {
		StringBuilder result = text;
		for (TextModifier processor : pipeline) {
			result = processor.apply(result);
		}
		return result;
	}
}
