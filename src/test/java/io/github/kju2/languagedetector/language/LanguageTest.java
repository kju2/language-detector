package io.github.kju2.languagedetector.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.github.kju2.languagedetector.language.Language;

public class LanguageTest {
	@Test
	void givenNullThenReturnLanguageUnknown() {
		assertEquals(Language.UNKNOWN, Language.from(null));
	}

	@Test
	void givenAnEmptyStringThenReturnLanguageUnknown() {
		assertEquals(Language.UNKNOWN, Language.from(""));
	}

	@Test
	void givenAStringThatIsNotAKnownLanguageThenReturnLanguageUnknown() {
		assertEquals(Language.UNKNOWN, Language.from("abcdefgh"));
	}

	@Test
	void givenTheLanguageCodeForEnglishInLowerCaseThenReturnLanguageEnglish() {
		assertEquals(Language.ENGLISH, Language.from("en"));
	}

	@Test
	void givenTheLanguageCodeForEnglishInUpperCaseThenReturnLanguageEnglish() {
		assertEquals(Language.ENGLISH, Language.from("EN"));
	}

	@Test
	void givenTheLanguageNameForEnglishInLowerCaseThenReturnLanguageEnglish() {
		assertEquals(Language.ENGLISH, Language.from("english"));
	}

	@Test
	void givenTheLanguageNameForEnglishInUpperCaseThenReturnLanguageEnglish() {
		assertEquals(Language.ENGLISH, Language.from("ENGLISH"));
	}

	@Test
	void givenTheLanguageNameForEnglishInMixedCaseThenReturnLanguageEnglish() {
		assertEquals(Language.ENGLISH, Language.from("EnGlIsH"));
	}
}
