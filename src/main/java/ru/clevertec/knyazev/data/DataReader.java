package ru.clevertec.knyazev.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DataReader {
	final String CARD_PREFIX = "card-";
	
	/**
	 * 
	 * @return data read from input source 
	 * @throws IOException when error on reading data from source
	 */
    Map<String[], String[]> readData() throws IOException;
    
    /**
	 * 
	 * @param inputData data input card-123 5-6 6-7
	 * @return Map of two String[] where key is purchases(5-6 6-7) and value is cards(card-123)
	 */
	default Map<String[], String[]> divideData(String inputData) {
		List<String> purchaseData = new ArrayList<>();
		List<String> cardsData = new ArrayList<>();

		String[] data = inputData.split(" ");

		for (String dataInput : data) {
			if (dataInput.contains(CARD_PREFIX) && dataInput.startsWith(CARD_PREFIX)) {
				cardsData.add(dataInput);
			} else {
				purchaseData.add(dataInput);
			}
		}

		return new HashMap<>() {
			private static final long serialVersionUID = -188332342896873359L;
			{
				put(purchaseData.toArray(new String[0]), cardsData.toArray(new String[0]));
			}
		};
	}
}