package io.github.kju2.languagedetector.language;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LanguageProfiles {

	static final String LANGUAGE_PROFILE_DIRECTORY = "models/";

	private static Map<Language, LanguageProfile> builtInLanguages = null;

	/**
	 * Loads and caches all built-in language profiles.
	 *
	 * @return an immutable cache of the built-in language profiles.
	 * @throws IOException if an I/O error occurs.
	 */
	public static Map<Language, LanguageProfile> builtInLanguages() throws IOException {
		if (builtInLanguages == null) {
			builtInLanguages = new EnumMap<>(Language.class);
			for (Language language : Language.values()) {
				LanguageProfile languageProfile = readBuiltIn(language);
				if (languageProfile != null) {
					builtInLanguages.put(language, languageProfile);
				}
			}
			builtInLanguages = Collections.unmodifiableMap(builtInLanguages);
		}

		return builtInLanguages;
	}

	private static LanguageProfile readBuiltIn(Language language) throws IOException {
		try (InputStream inputStream = LanguageProfiles.class.getResourceAsStream(LANGUAGE_PROFILE_DIRECTORY + language.name())) {
			return inputStream == null ? null : LanguageProfile.read(language, inputStream);
		}
	}
}
