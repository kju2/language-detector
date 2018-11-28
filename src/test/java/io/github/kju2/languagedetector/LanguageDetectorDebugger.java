package io.github.kju2.languagedetector;

import java.io.IOException;

import io.github.kju2.languagedetector.language.Language;

public class LanguageDetectorDebugger {
	public static void main(String[] args) throws IOException {
		String text = "ENTER_TEXT_HERE";
		LanguageDetector detector = new LanguageDetector();
		Language detecetedLanguage = detector.detectPrimaryLanguageOf(text);
		System.out.println("Primary language of the given text is: " + detecetedLanguage.name());
	}
}
