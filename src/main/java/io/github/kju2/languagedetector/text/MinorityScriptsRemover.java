package io.github.kju2.languagedetector.text;

import java.lang.Character.UnicodeScript;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * If a text has several Unicode scripts then keep only the code points from the predominant script
 * and common characters (e.g. ' ', '.', ...).
 * <p>
 * Code points from minority scripts are kept if the ratio to the predominant script is above a
 * configurable threshold, e.g. a text contains 80 code points in Cyrillic, 16 code points in Latin
 * and 4 code points of punctuation.
 * <ul>
 * <li>If the threshold is 0.3 then the ratio 16/80 is below that and all Latin code points are
 * removed from the text.</li>
 * <li>If the threshold is 0.1 then the ratio 16/80 is above that and all code points kept.</li>
 * </ul>
 */
public class MinorityScriptsRemover implements TextModifier {

	private final float threshold;

	public MinorityScriptsRemover() {
		this(0.4f);
	}

	public MinorityScriptsRemover(float threshold) {
		this.threshold = threshold;
	}

	@Override
	public StringBuilder apply(StringBuilder text) {
		SortedSet<Map.Entry<Character.UnicodeScript, Long>> usedScriptsSortedByOccurrences = countScriptsUsedInText(text);
		if (usedScriptsSortedByOccurrences.size() < 2) {
			// There is no minority script, therefore nothing to remove.
			return text;
		}

		Set<UnicodeScript> minorityScripts = new HashSet<>(usedScriptsSortedByOccurrences.size() - 1);
		float numberOfCodePointsOfMajorityScript = usedScriptsSortedByOccurrences.first().getValue().floatValue();
		for (Entry<UnicodeScript, Long> script : usedScriptsSortedByOccurrences) {
			if (script.getValue().floatValue() / numberOfCodePointsOfMajorityScript <= threshold) {
				minorityScripts.add(script.getKey());
			}
		}

		if (minorityScripts.isEmpty()) {
			return text;
		}
		return removeMinorityScripts(minorityScripts, text);

	}

	private SortedSet<Map.Entry<Character.UnicodeScript, Long>> countScriptsUsedInText(StringBuilder sb) {
		Map<Character.UnicodeScript, Long> counter = new EnumMap<>(Character.UnicodeScript.class);
		Character.UnicodeScript script = null;
		for (int codePoint : sb.codePoints().toArray()) {
			script = determineUnicodeScript(script, codePoint);

			if (script != null) {
				long numberOfOccurrences = counter.getOrDefault(script, 0l);
				counter.put(script, numberOfOccurrences + 1);
			}
		}
		// Following scripts are removed because they belong to several languages or none.
		counter.remove(Character.UnicodeScript.COMMON);
		counter.remove(Character.UnicodeScript.UNKNOWN);

		return sortByOccurrence(counter);
	}

	private SortedSet<Map.Entry<Character.UnicodeScript, Long>> sortByOccurrence(Map<Character.UnicodeScript, Long> counter) {
		TreeSet<Map.Entry<Character.UnicodeScript, Long>> unicodeScriptsSortedByOccurrences = new TreeSet<>(Comparator.comparing(Map.Entry<Character.UnicodeScript, Long>::getValue).reversed());
		unicodeScriptsSortedByOccurrences.addAll(counter.entrySet());
		return unicodeScriptsSortedByOccurrences;
	}

	private StringBuilder removeMinorityScripts(Set<UnicodeScript> minorityScripts, StringBuilder text) {
		StringBuilder textWithoutMinorityScripts = new StringBuilder(text.length());

		Character.UnicodeScript script = null;
		for (int c : text.codePoints().toArray()) {
			script = determineUnicodeScript(script, c);

			if (minorityScripts.contains(script)) {
				// This code point belongs to a minority script and should be replaced with a space
				// unless the last character added was already a space.
				int lastIndex = textWithoutMinorityScripts.length() - 1;
				if (lastIndex > 0 && textWithoutMinorityScripts.charAt(lastIndex) != Texts.SPACE) {
					textWithoutMinorityScripts.append(Texts.SPACE);
				}
			} else {
				textWithoutMinorityScripts.appendCodePoint(c);
			}
		}
		return textWithoutMinorityScripts;
	}

	private Character.UnicodeScript determineUnicodeScript(Character.UnicodeScript scriptOfPreviousCodePoint, int codePoint) {
		Character.UnicodeScript script = Character.UnicodeScript.of(codePoint);
		if (script == Character.UnicodeScript.INHERITED) {
			// Inherited means it belongs to the same script as the previous code point.
			return scriptOfPreviousCodePoint;
		}
		return script;
	}
}
