package spam_filter;

public class DigitFilter implements ParserFilter{
	private static final String DIGIT_REGEX = ".*\\d.*";
	
	@Override
	public boolean filter(String str) {
		return str.matches(DIGIT_REGEX);
	}

}
