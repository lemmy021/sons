package rs.sons.helper;

import org.apache.commons.lang.WordUtils;

public class WordWrap {

	public static String[] wordWrapping(String text, int wrapLength) {
		String wrappedText = WordUtils.wrap(text, wrapLength);

		return wrappedText.split(System.lineSeparator());
	}
}
