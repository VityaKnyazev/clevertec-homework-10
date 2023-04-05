package ru.clevertec.knyazev.data;

import java.io.IOException;
import java.util.Map;

public class StandardDataReader implements DataReader {
	private String[] args;
	
	public StandardDataReader(String[] args) {
		this.args = args;
	}

	@Override
	public Map<String[], String[]> readData() throws IOException {
		if (args == null || args.length == 0)
			throw new IOException("Given input data is empty!");

		String inputData = "";
		
		for (int i = 0; i < args.length; i++) {
			inputData += args[i] + " ";
		}
		
		inputData = inputData.substring(0, inputData.length() - 1);

		return divideData(inputData);
	}

}
