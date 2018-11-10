package com.github.kju2.languagedetector.language;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LanguageProfiles {

	static final String LANGUAGE_PROFILE_DIRECTORY = "languages/";

	private static Map<Language, LanguageProfile> builtInLanguages = null;

	/**
	 * Loads and caches all built-in language profiles.
	 */
	public static Map<Language, LanguageProfile> builtInLanguages() throws IOException {
		if (builtInLanguages == null) {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(LanguageProfiles.class.getClassLoader().getResourceAsStream(LANGUAGE_PROFILE_DIRECTORY)))) {
				builtInLanguages = new EnumMap<>(Language.class);
				for (Language language : reader.lines().map(Language::from).filter(l -> l != Language.UNKNOWN).collect(Collectors.toSet())) {
					builtInLanguages.put(language, readBuiltIn(language));
				}
			}
			builtInLanguages = Collections.unmodifiableMap(builtInLanguages);
		}

		return builtInLanguages;
	}

	private static LanguageProfile readBuiltIn(Language language) throws IOException {
		try (InputStream in = LanguageProfiles.class.getClassLoader().getResourceAsStream(LANGUAGE_PROFILE_DIRECTORY + language.name())) {
			return LanguageProfile.read(language, in);
		}
	}
}
