package spam_filter;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyzerUtility {
	
	// Show top ten. In production software this would be a configurable value.
	private static final int TOP_N_TOKENS_PER_TYPE = 10;
	
	// DecimalFormat is not thread safe. Assumption here is made
	// that this is a single threaded process so its okay to keep 
	// as a static global variable.
	private static DecimalFormat df = new DecimalFormat("###.##");
		
	/**
	 * Print top line aggregate metrics of the corpus file
	 * to STDOUT.
	 */
	public static void printTopLineStatistics(CorpusAggregate stats) {
		System.out.println("Top line metrics ----");
		System.out.println("Total SMS occurances: " + stats.getTotalCount());
		System.out.println("Total Ham occurances: " + stats.getTotalHamCount());
		System.out.println("Total Spam occurances: " + stats.getTotalSpamCount());
		
		double hamPercent = (double) stats.getTotalHamCount()/stats.getTotalCount()*100;
		double spamPercent = (double)stats.getTotalSpamCount()/stats.getTotalCount()*100;
		
		System.out.println("Percentage of Ham occurances: " + df.format(hamPercent));
		System.out.println("Percentage of Spam occurances: " +  df.format(spamPercent));
		
	}
	
	/**
	 * Print Type specific aggregate metrics of the corpus file
	 * to STDOUT.
	 */
	public static void printCorpusMetrics(CorpusAggregate stats) {

		System.out.println("\nHam corpus metrics ----");
		System.out.println("Total Ham token occurances: " + stats.getHamTotalTokenCount());	
		// Keep averages as integers since we are interested in whole values for average token (word) occurrences.
		System.out.println("Average token occurances per Ham corpus: " + stats.getHamTotalTokenCount()/stats.getTotalHamCount());
		
		System.out.println(String.format("Top %s tokens for Ham corpus", TOP_N_TOKENS_PER_TYPE) );
		sortAndPrintMap(stats.getHamTokenCounts());

		System.out.println("\nSpam corpus metrics ----");
		System.out.println("Total Spam token occurances: " + stats.getSpamTotalTokenCount());
		// Keep averages as integers since we are interested in whole values for average token (word) occurrences.
		System.out.println("Average token occurances per Spam corpus: " + stats.getSpamTotalTokenCount()/stats.getTotalSpamCount());

		System.out.println(String.format("Top %s tokens for Spam corpus", TOP_N_TOKENS_PER_TYPE) );
		sortAndPrintMap(stats.getSpamTokenCounts());
		
	}
	
	/**
	 * Sort the input map and print top N entries to STDOUT
	 * @param map
	 */
	private static void sortAndPrintMap(Map<String, Integer> map) {
		Map<String, Integer> sortedTokenMap = map.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

		int counter = TOP_N_TOKENS_PER_TYPE;
		for (Map.Entry<String, Integer> entry : sortedTokenMap.entrySet()) {

			if (counter == 0)
				break;
			
			System.out.println(entry.getKey() + ": " + entry.getValue());
			counter--;
		}
	}
}
