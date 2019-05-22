package spam_filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Driver Application.
 */
public class Application {
	public static final String INPUT_RESOURCE = "resources/SMSSpam";
	// Number of messages to test against.
	private static final int TEST_SAMPLES = 50;

	public void analyzeSMSCorpus() {
		// Read data from disk and store aggregated data. Raw data is not
		// stored in memory intentionally to minimize RAM usage by this process.
		// The input test file is small in size (477,907 bytes) but this approach
		// is important in distributed systems data processing when dealing with
		// large scale data.

		List<SMSParser> parsers = new ArrayList<SMSParser>();

		// Initialize a parser with no Corpus token filters
		SMSParser unFilteredparser = new SMSParser(null, true);
				
		// Initialize a parser with text token filters to analyze the
		// corpus statistics with removed 'uninteresting words'
		List<ParserFilter> textParserFilter = new ArrayList<ParserFilter>();
		textParserFilter.add(new TextFilter());
		SMSParser textFilterparser = new SMSParser(textParserFilter, true);

		parsers.add(textFilterparser);
		parsers.add(unFilteredparser);
		
		try {
			SMSFileReaderUtility.readFile(INPUT_RESOURCE, parsers);
		} catch (IOException e) {
			System.err.println("Error reading input resource " + e);
			System.exit(1);
		}
		
		System.out.println("************* Unfiltered Metrics *************");
		AnalyzerUtility.printTopLineStatistics(unFilteredparser.getAggregates());
		AnalyzerUtility.printCorpusMetrics(unFilteredparser.getAggregates());

		System.out.println("\n********* Filtered out uninteresting tokens Metrics *********");
		AnalyzerUtility.printCorpusMetrics(textFilterparser.getAggregates());
	}
	
	public void testSpamFilter() {
		
		// To test the logic from Paul Grahms paper on SPAM filtering
		// I perform two tests. First test will test the logic against
		// unfiltered corpus aggregate that contains 'uninteresting tokens'. 
		// The list of uninteresting key words can be found in TextFilter.java. 
		// The second test will run against filtered aggregates that do
		// not contain these tokens. Finally, I will compare the results
		// to understand if filtering uninteresting words improves the
		// accuracy of finding the probability if a message is SPAM.
		List<SMSParser> parsers = new ArrayList<SMSParser>();

		// Initialize a parser with no Corpus token filters
		SMSParser unFilteredParser = new SMSParser(null, true);

		// Initialize a parser with text and digit token filters to analyze the
		// corpus statistics with removed 'uninteresting words' and tokens that
		// contain digits.
		List<ParserFilter> filters = new ArrayList<ParserFilter>();
		filters.add(new TextFilter());
		SMSParser filteredParser = new SMSParser(filters, true);

		parsers.add(unFilteredParser);
		parsers.add(filteredParser);

		try {
			SMSFileReaderUtility.readFile(Application.INPUT_RESOURCE, parsers);
		} catch (IOException e) {
			System.err.println("Error reading input resource " + e);
			System.exit(1);
		}
		
		SpamDetector spamDetector = new SpamDetector();
		
		System.out.println("Test accuracy of unfiltered dataset: " + getAccuracy(spamDetector, unFilteredParser.getAggregates()));
		
		System.out.println("Test accuracy of filtered dataset: " + getAccuracy(spamDetector, filteredParser.getAggregates()));
				
	}
	
	private double getAccuracy(SpamDetector spamDetector, CorpusAggregate aggregates) {
		int totalFileLines = aggregates.getTotalCount();
		
		int numIncorrect = 0;
		try {
			// Get 5 random messages from corpus.
			List<String> testMessages = SMSFileReaderUtility.getRandomFileLines(Application.INPUT_RESOURCE,
					totalFileLines, TEST_SAMPLES);
			
			for(String message: testMessages) {
				
				String [] tokens = message.split(SMSParser.WHITE_SPACE_REGEX);
				if( tokens.length >= 2) {
					String type = tokens[0];
					
					// Assert if actual and result are the same.
					if(SMSParser.isSpam(type) != spamDetector.isSpam(aggregates, tokens, 1)) 
						numIncorrect++;
					
					//System.out.println(type + ": result " + isSpam(aggregates, tokens, 1) );
					
				}				
			}
		} catch (IOException e) {
			System.err.println("Error reading input resource " + e);
			System.exit(1);
		}
		
		return getPercentAccuracy(numIncorrect, TEST_SAMPLES);
	}
	
	private double getPercentAccuracy(int totalIncorrect, int total) {
		if(totalIncorrect == 0)
			return 100;
		else
			return (((double)total-totalIncorrect)/total)*100;
	}
	
	public static void main(String[] args) {
		Application app = new Application();
		
		System.out.println("Data Analysis");
		app.analyzeSMSCorpus();

		System.out.println("\nSpam detector test");
		app.testSpamFilter();
		
	}
}
