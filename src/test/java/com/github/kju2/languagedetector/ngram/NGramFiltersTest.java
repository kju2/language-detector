package com.github.kju2.languagedetector.ngram;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Predicate;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.github.kju2.languagedetector.ngram.NGramFilters;

public class NGramFiltersTest {

	@Nested
	class HasNonWhitespaceCharactersTest {
		private final Predicate<String> predicate = NGramFilters.HAS_NON_WHITESPACE_CHARACTERS;

		@Test
		void givenOnlyATabThenReturnFalse() {
			assertFalse(predicate.test("\t"));
		}

		@Test
		void givenOnlyANewLineThenReturnFalse() {
			assertFalse(predicate.test("\n"));
		}

		@Test
		void givenATextWithWhitespacesThenReturnTrue() {
			assertTrue(predicate.test("blub\n blub \t blubblub"));
		}
	}
}
