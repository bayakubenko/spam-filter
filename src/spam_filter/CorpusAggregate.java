package spam_filter;

import java.util.HashMap;
import java.util.Map;

public class CorpusAggregate {
	// total sms messages processed;
	private int totalCount = 0;
	
	// total spam messages
	private int spamTotalCount = 0;
	
	// total spam messages
	private int hamTotalCount = 0;

	// total words for spam messages
	private int spamTotalTokenCount = 0;

	// total words for ham messages
	private int hamTotalTokenCount = 0;

	// number of word occurrences for ham messages
	private Map<String, Integer> hamTokenCounts = null;

	// number of word occurrences for spam messages
	private Map<String, Integer> spamTokenCounts = null;

	public CorpusAggregate() {
		this.hamTokenCounts = new HashMap<String, Integer>();
		this.spamTokenCounts = new HashMap<String, Integer>();
	}

	public void addHamTokenOccurance(String str) {
		this.hamTotalTokenCount++;
		this.addOccurance(hamTokenCounts, str);
	}

	public void addSpamTokenOccurance(String str) {
		this.spamTotalTokenCount++;
		this.addOccurance(spamTokenCounts, str);
	}

	/**
	 * Tool to update occurrences in a map.
	 * 
	 * @param map
	 * @param str
	 */
	private void addOccurance(Map<String, Integer> map, String str) {
		if (map.containsKey(str)) {
			int count = map.get(str);
			map.put(str, ++count);
		} else {
			map.put(str, 1);
		}
	}

	public void incrementHamTotal() {
		this.hamTotalCount++;
	}

	public void incrementSpamTotal() {
		this.spamTotalCount++;
	}

	public void incrementTotal() {
		this.totalCount++;
	}

	public int getTotalSpamCount() {
		return spamTotalCount;
	}

	public void setTotalSpamCount(int spamCount) {
		this.spamTotalCount = spamCount;
	}

	public int getTotalHamCount() {
		return hamTotalCount;
	}

	public void setTotalHamCount(int hamCount) {
		this.hamTotalCount = hamCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public Map<String, Integer> getHamTokenCounts() {
		return hamTokenCounts;
	}

	public void setHamTokenCounts(Map<String, Integer> hamTokenCounts) {
		this.hamTokenCounts = hamTokenCounts;
	}

	public Map<String, Integer> getSpamTokenCounts() {
		return spamTokenCounts;
	}

	public void setSpamTokenCounts(Map<String, Integer> spamTokenCounts) {
		this.spamTokenCounts = spamTokenCounts;
	}

	public int getSpamTotalTokenCount() {
		return spamTotalTokenCount;
	}

	public void setSpamTotalTokenCount(int spamTotalTokenCount) {
		this.spamTotalTokenCount = spamTotalTokenCount;
	}

	public int getHamTotalTokenCount() {
		return hamTotalTokenCount;
	}

	public void setHamTotalTokenCount(int hamTotalTokenCount) {
		this.hamTotalTokenCount = hamTotalTokenCount;
	}
}
