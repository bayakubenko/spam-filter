package spam_filter;

import java.util.List;

/**
 * Parse SMS Lines and build an aggregate of line metrics.
 */
public class SMSParser {
	private static final String HAM = "ham";
	private static final String SPAM = "spam";
	public static final String WHITE_SPACE_REGEX = "\\s+";

	private CorpusAggregate statistics = null;
	private List<ParserFilter> filters = null;
	
	private boolean normalize = true;

	public SMSParser(List<ParserFilter> filters, boolean normalize) {
		this.statistics = new CorpusAggregate();
		this.filters = filters;
		this.normalize = normalize;
	}
	
	public static boolean isSpam(String str) {
		return str.equals(SPAM);
	}

	public static boolean isHam(String str) {
		return str.equals(HAM);
	}
	
	public CorpusAggregate getAggregates() {
		return statistics;
	}

	private boolean filter(String token) {
		for (ParserFilter pf : filters) {
			if (pf.filter(token))
				return true;
		}

		return false;
	}

	/**
	 * Logic to parse the lines of corpus file and update statistics.
	 * 
	 * @param line
	 */
	public void parse(String line) {
		String[] parts = line.split(WHITE_SPACE_REGEX);

		if (parts.length >= 2) {
			
			// In most situations, token normalization logic should be
			// added to take into account unnormalized input data. For demonstration
			// purposes, the type is lower cased as an example of the technique.
			String type = parts[0].toLowerCase();
			if (isSpam(type)) {
				this.statistics.incrementSpamTotal();
				// safety check in case the type is not ham or spam
			} else if (isHam(type)) {
				this.statistics.incrementHamTotal();
			} else {
				// return if the type is not ham or spam.
				return;
			}
			
			// Since we only care about HAM or SPAM for the probability
			// computation, increment after we check the type.
			this.statistics.incrementTotal();

			for (int i = 1; i < parts.length; i++) {
				String token = parts[i];
				
				if(normalize)
					token = TokenNormalizer.normalize(token);

				if (this.filters != null && this.filter(token))
					continue;

				if (isSpam(type)) {
					this.statistics.addSpamTokenOccurance(token);

					// No need to check here if ham.
				} else {
					this.statistics.addHamTokenOccurance(token);
				}
			}
		}
	}
}
