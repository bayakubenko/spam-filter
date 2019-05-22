package spam_filter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TextFilter implements ParserFilter {

	// Values added in the code for simplicity. This list should
	// be populated at run time from a file or a DB. A Set is used
	// to guarantee uniqueness in case of duplicates and to performantly
	// access via contains method with O(1).
	private static Set<String> uninterestingWords = new HashSet<String>(
			Arrays.asList("to", "you", "we", "I", "i", "a", "and", "is", "the", "in", "u", "my", "me", 
					"of", "for", "it", "your", "i'm", "on", "for", "ur", "that", "have", "are", "or", 
					"at", "not", "be"));

	public TextFilter() {}

	@Override
	public boolean filter(String str) {
		return uninterestingWords.contains(str);
	}

}
