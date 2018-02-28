package dst.ass1.nosql;

import java.util.ArrayList;

public class MongoTestData {
	public static final String DATA_DESC_LOGS = "logs";
	public static final String DATA_DESC_MATRIX = "matrix";
	public static final String DATA_DESC_BLOCK = "alignment_block";

	private ArrayList<String> testData = new ArrayList<String>();
	private ArrayList<String> testDataDesc = new ArrayList<String>();

	public MongoTestData() {
		String s1 = "{ \"log_set\" : [\"SCHEDULED\", \"STREAMING\", \"CANCELED\", \"FINISHED\"] }";
		String s2 = "{ \"matrix_data\" : [[1, 0, 0, 0], [0, 1, 0, 0], [0, 0, 1, 0], [0, 0, 0, 1]]}";
		String s3 = "{ \"alignment_nr\" : 0, \"primary\" : { "
				+ " \"chromosome\" : \"chr11\", \"start\" : 3001012, \"end\" : 3001075 }, \"align\" : { "
				+ " \"chromosome\" : \"chr13\", \"start\" : 70568380, \"end\" : 70568443 }, \"blastz\" : 3500, "
				+ "seq : [\"TCAGCTCATAAATCACCTCCTGCCACAAGCCTGGCCTGGTCCCAGGAGAGTGTCCAGGCTCAGA\", "
				+ "\"TCTGTTCATAAACCACCTGCCATGACAAGCCTGGCCTGTTCCCAAGACAATGTCCAGGCTCAGA\"] }";

		testData.add(s1);
		testData.add(s2);
		testData.add(s3);

		testDataDesc.add(DATA_DESC_LOGS);
		testDataDesc.add(DATA_DESC_MATRIX);
		testDataDesc.add(DATA_DESC_BLOCK);
	}

	public String getData(Long eventId) {
		return testData.get(eventId.intValue() % 3);
	}

	public String getDataDescription(Long eventId) {
		return testDataDesc.get(eventId.intValue() % 3);
	}
}
