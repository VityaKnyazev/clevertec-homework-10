package ru.clevertec.knyazev.data;

public class ConsoleDataWriter implements DataWriter {

	@Override
	public void writeData(String data) {
		System.out.print(data);		
	}
}