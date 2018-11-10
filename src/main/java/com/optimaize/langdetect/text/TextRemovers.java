package com.optimaize.langdetect.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextRemovers {
	public static final TextRemover url = createRegexRemover("https?://[-_.?&~;+=/#0-9A-Za-z]+");
	public static final TextRemover email = createRegexRemover("[-_.0-9A-Za-z]+@[-_0-9A-Za-z]+[-_.0-9A-Za-z]+");

	public static TextRemover createRegexRemover(Pattern pattern) {
		return sb -> {
			Matcher matcher = pattern.matcher(sb);
			int index = 0;
			while (matcher.find(index)) {
				sb.replace(matcher.start(), matcher.end(), "");
			}
			return sb;
		};
	}

	public static TextRemover createRegexRemover(String pattern) {
		return createRegexRemover(Pattern.compile(pattern));
	}
}
