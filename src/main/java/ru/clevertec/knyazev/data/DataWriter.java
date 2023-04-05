package ru.clevertec.knyazev.data;

import java.io.IOException;

public interface DataWriter {
	
	/**
	 * 
	 * @param data data to write in output source
	 * @throws IOException when error on writing data
	 */
	void writeData(String data) throws IOException;
}
