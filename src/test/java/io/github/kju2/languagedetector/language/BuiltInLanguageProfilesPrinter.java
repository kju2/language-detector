package io.github.kju2.languagedetector.language;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import io.github.kju2.languagedetector.language.Language;
import io.github.kju2.languagedetector.language.LanguageProfiles;

public class BuiltInLanguageProfilesPrinter {
	/**
	 * Print built in languages with name and language code
	 */
	public static void main(String[] args) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(LanguageProfiles.class.getClassLoader().getResourceAsStream(LanguageProfiles.LANGUAGE_PROFILE_DIRECTORY)))) {
			int i = 1;
			for (Language language : reader.lines().map(Language::from).filter(l -> l != Language.UNKNOWN).sorted().collect(Collectors.toList())) {
				System.out.println(i + ". " + language.name() + " (" + language.code + ")");
				i++;
			}
		}
	}
}
