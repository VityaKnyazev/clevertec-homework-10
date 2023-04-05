package ru.clevertec.knyazev.data;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class ConsoleDataReader implements DataReader {

	@Override
	public Map<String[], String[]> readData() throws IOException {
		System.out.println("Введите набор товаров и их количество в формате: id-quantity. Пример: 12-5 3-4.256 3-5.");
		System.out.println(
				"Введите номера дисконтных карт в формате: card-bumber. Пример: card-123256325 card-3256254123 card-864547856.");
		System.out.println("Набор товаров нужно вводить вместе с дисконтными картами.");

		String inputData = "";

		try (Scanner scanner = new Scanner(System.in)) {
			if (scanner.hasNextLine()) {
				inputData = scanner.nextLine();
			}
		}

		if (inputData == null || inputData.length() == 0 || inputData == " ")
			throw new IOException("Given console data is empty!");

		inputData = inputData.replaceAll(System.lineSeparator(), " ").replaceAll("\\s+", " ");

		return divideData(inputData);
	}

}
