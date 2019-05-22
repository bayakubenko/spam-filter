package spam_filter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SMSFileReaderUtility {

	private static Set<Integer> getRandomIntArray(int range, int num) {
		Random generator = new Random();
		Set<Integer> integers = new HashSet<Integer>();

		for (int i = 0; i < num; i++) {
			integers.add(generator.nextInt(range));
		}

		return integers;
	}

	public static List<String> getRandomFileLines(String fileName, int range, int num)
			throws FileNotFoundException, IOException {
		List<String> smsCorpusList = new ArrayList<String>();

		// Using a set to hold random line numbers of interest.
		// Supports efficient look ups.
		Set<Integer> lineNumbers = getRandomIntArray(range, num);

		// counter to maintain line number progress
		// Starts at 0 since the Random::nextInt returns value
		// between 0 (inclusive) and the specified value (exclusive)
		int counter = 0;

		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			
			String line;
			while ((line = br.readLine()) != null) {

				if (lineNumbers.contains(counter)) {
					smsCorpusList.add(line);
				}

				if (smsCorpusList.size() == lineNumbers.size()) {
					// break out of the loop if all lines are found.
					break;
				}
				counter++;
			}
		}

		return smsCorpusList;
	}

	public static void readFile(String inputFilePath, List<SMSParser> parsers) throws FileNotFoundException, IOException {

		long start_ts = System.currentTimeMillis();

		try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {

			String line;
			while ((line = br.readLine()) != null) {

				for(SMSParser parser: parsers)
					parser.parse(line);
			}

			// Print file processing latency.
			System.out.println("Processed input file in " + getFormattedLatency(start_ts));
		}
	}

	private static String getFormattedLatency(long startTimeMillis) {

		long duration = System.currentTimeMillis() - startTimeMillis;

		long hours = TimeUnit.MILLISECONDS.toHours(duration);
		duration -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
		duration -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);
		duration -= TimeUnit.SECONDS.toMillis(seconds);

		StringBuilder sb = new StringBuilder("Total time (hh:mm:ss:SSS): [");
		sb.append(hours).append(":").append(minutes).append(":").append(seconds).append(":").append(duration)
				.append("]");

		return sb.toString();
	}

}
