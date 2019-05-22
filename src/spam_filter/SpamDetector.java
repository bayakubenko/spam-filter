package spam_filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 * Class wraps around probability computation logic described in
 * http://www.paulgraham.com/spam.html
 *
 */
public class SpamDetector {

	// Paul Grahm excerpt
	// "One question that arises in practice is what probability to assign to a word
	// you've never seen, i.e. one that doesn't occur in the hash table of word
	// probabilities. I've found, again by trial and error, that .4 is a good number 
	// to use"
	private static final double DEFAULT_SPAM_PROBABILITY = .4;

	// Paul Grahm excerpt
	// "the most interesting fifteen tokens, where interesting is measured by how
	// far their spam probability is from a neutral .5, are used to calculate the
	// probability that the mail is spam"
	private static final int INTERESTING_TOKEN_MAX = 15;

	// Paul Grahm excerpt
	// "interesting is measured by how far their spam probability is from a neutral
	// .5"
	private static final double NEUTRAL_OFFSET = .5;
	
	// Paul Grahm excerpt
	// "I treat mail as spam if the algorithm above gives it a probability of more 
	// than .9 of being spam"
	private static final double SPAM_PROBABILITY_THRESHOLD = .9;
	
	/**
	 * Compute the probability of the message being spam.
	 * @param aggregate
	 * @param message
	 * @param token_offset
	 * @return
	 */
	public boolean isSpam(CorpusAggregate aggregate, String[] message, int token_offset) {
		Map<String, Double> sortedTokens = getSortedTokenStrength(aggregate, message, token_offset);
		
		double probability = getCompoundProbability(aggregate, sortedTokens);
		return probability >= SPAM_PROBABILITY_THRESHOLD;
	}

	private double getCompoundProbability(CorpusAggregate aggregate, Map<String, Double> sortedTokens) {

		double a = 0.0;
		double b = 0.0;

		int i = INTERESTING_TOKEN_MAX;

		// Entries are in descending order of token spam strength computed by
		// :: getTokenStrength()
		Iterator<Map.Entry<String, Double>> itr = sortedTokens.entrySet().iterator();
		
		while (itr.hasNext() && i > 0) {
			Map.Entry<String, Double> entry = itr.next();

			String token = entry.getKey();

			double prob = getSpamProbabilty(aggregate, token);

			// Excerpt from Paul Grahm http://www.paulgraham.com/naivebayes.html
			if(a == 0.0)
				a = prob;
			
			if(b == 0.0)
				b = (1 - prob);
			
			a *= prob;
			b *= (1 - prob);

			i--;
		}

		// Excerpt from Paul Grahm http://www.paulgraham.com/naivebayes.html
		return a / (a + b);
	}

	private Map<String, Double> getSortedTokenStrength(CorpusAggregate aggregate, String[] tokens, int token_offset) {
		Map<String, Double> tokenStrength = new HashMap<String, Double>();

		for (int i = token_offset; i < tokens.length; i++) {
			String token = TokenNormalizer.normalize(tokens[i]);

			// checks if token exists in aggregate and sets probability
			double probability = getSpamProbabilty(aggregate, token);

			tokenStrength.put(token, getTokenStrength(probability));

		}

		// Return sorted map.
		return tokenStrength.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

	}

	private double getTokenStrength(double spamProbability) {

		// From Paul Grahm excerpt interesting is measured by how far their
		// spam probability is from a neutral .5
		return Math.abs(NEUTRAL_OFFSET - spamProbability);

	}

	/**
	 * Computes probability that a token will be a spam
	 * 
	 * @param token
	 * @return double
	 */
	public double getSpamProbabilty(CorpusAggregate aggregate, String token) {
		/*
		 Common Lisp 
		  	(let ((g (* 2 (or (gethash word good) 0)))
      				(b (or (gethash word bad) 0)))
   						(unless (< (+ g b) 5)
     			(max .01
          			(min .99 (float (/ (min 1 (/ b nbad))
                             (+ (min 1 (/ g ngood))   
                                (min 1 (/ b nbad)))))))))
		  
		Common Lisp pseudo-code translation 
		g = 2 * hamTokenCount(token) or 0 
		b = spamTokenCount(token) or 0
		 
		if ! (g + b < 5 )
		  
		 return max(.01 , 
		        (min(.99, 
		        	min( 1, b / spamCount) / 
		        	(min( 1, b / spamCount) + min(1, g / hamCount) )
		 */

		double hamCount = aggregate.getHamTokenCounts().get(token) == null ? 0.0
				: 2 * aggregate.getHamTokenCounts().get(token).doubleValue();
		
		double spamCount = aggregate.getSpamTokenCounts().get(token) == null ? 0.0
				: aggregate.getSpamTokenCounts().get(token).doubleValue();

		if ((hamCount + spamCount) < 5) {
			return DEFAULT_SPAM_PROBABILITY;
		}

		return Math.max(0.01,
				Math.min(0.99,
						Math.min(1, spamCount / aggregate.getTotalSpamCount())
								/ (Math.min(1, hamCount / aggregate.getTotalHamCount())
										+ Math.min(1, spamCount / aggregate.getTotalSpamCount()))));

	}

}
