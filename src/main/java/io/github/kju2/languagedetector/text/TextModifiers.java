package io.github.kju2.languagedetector.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TextModifiers {

	/**
	 * Removes all URLs. Modifies the given {@link StringBuilder} instance.
	 * <p>
	 * <b>WARNING</b>: Doesn't recognize URLs with internationalized domain names.
	 */
	public static final TextModifier URL_REMOVER = createRegexRemover("https?://[-_.?&~;+=/#0-9A-Za-z]+");

	/**
	 * Removes all emails. Modifies the given {@link StringBuilder} instance.
	 * <p>
	 * <b>WARNING</b>: Doesn't recognize emails with internationalized domain names.
	 */
	public static final TextModifier EMAIL_REMOVER = createRegexRemover("[-_.0-9A-Za-z]+@[-_0-9A-Za-z]+[-_.0-9A-Za-z]+");

	public static final TextModifier SPACE_PADDER = text -> {
		if (text.length() < 1) {
			return text;
		}

		if (text.charAt(0) != Texts.SPACE) {
			text.insert(0, Texts.SPACE);
		}

		int end = text.length() - 1;
		if (text.charAt(end) != Texts.SPACE) {
			text.append(Texts.SPACE);
		}
		return text;
	};

	public static TextModifier createRegexRemover(Pattern pattern) {
		return sb -> {
			Matcher matcher = pattern.matcher(sb);
			int index = 0;
			while (matcher.find(index)) {
				sb.delete(matcher.start(), matcher.end());
			}
			return sb;
		};

	}

	public static TextModifier createRegexRemover(String pattern) {
		return createRegexRemover(Pattern.compile(pattern));
	}
}
