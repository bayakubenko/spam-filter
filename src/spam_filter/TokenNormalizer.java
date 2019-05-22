package spam_filter;

/**
 * Utility to normalize tokens
 */
public class TokenNormalizer {
	
	private static final  String EMAIL_REGEX = "^(.+)@(.+)$";
	private static final String HTTP_REGEX ="^(http|https)://.*$";
	private static final String CURRENCY_SYMBOL_REGEX = "\\p{Sc}(([1-9]\\d{0,2}(,\\d{3})*)|(([1-9]\\d*)?\\d))(\\.\\d\\d)?$";
	private static final String PHONE_HUMBER_REGEX = "\\d{11}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";
	private static final String DIGIT_REGEX = "\\d+(\\.\\d+)?";
	
	public static final String EMAIL_TOKEN = "emailaddress";
	public static final String URL_TOKEN = "httpaddress";
	public static final String CURRENCY_TOKEN = "currency";
	public static final String PHONE_NUMBER_TOKEN = "phonenumber";
	public static final String DIGITS_TOKEN = "digits";
	
	
	/**
	 * Poor mans solution for token normalization where a token
	 * is replaced by a normalized string. Can be improved by adding
	 * more cases.
	 * @param token
	 * @return
	 */
	public static String normalize(String token) {	
		
		token = token.toLowerCase();
		token = token.replaceAll(EMAIL_REGEX, EMAIL_TOKEN);
		token = token.replaceAll(HTTP_REGEX, URL_TOKEN);
		token = token.replaceAll(CURRENCY_SYMBOL_REGEX, CURRENCY_TOKEN);
		token = token.replaceAll(PHONE_HUMBER_REGEX, PHONE_NUMBER_TOKEN);
		token = token.replaceAll(DIGIT_REGEX, DIGITS_TOKEN);
		
		return token;
	}

}
