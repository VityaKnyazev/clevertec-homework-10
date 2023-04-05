package ru.clevertec.knyazev.data;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import ru.clevertec.knyazev.data.exception.ConverterException;
import ru.clevertec.knyazev.data.exception.ValidatorException;
import ru.clevertec.knyazev.dto.DiscountCardDTO;
import ru.clevertec.knyazev.dto.ProductDTO;

public abstract class DataReaderWriterDecorator implements DataReader, DataWriter{
	private DataReader dataReader;
	private DataWriter dataWriter;
	
	
	public DataReaderWriterDecorator(DataReader dataReader, DataWriter dataWriter) {
		this.dataReader = dataReader;
		this.dataWriter = dataWriter;
	}

	@Override
	public void writeData(String data) throws IOException {
		dataWriter.writeData(data);		
	}

	@Override
	public Map<String[], String[]> readData() throws IOException {
		return dataReader.readData();
	}
	
	public abstract Set<DiscountCardDTO> readDiscountCards() throws IOException, ConverterException;
	
	public abstract Map<ProductDTO, BigDecimal> readPurchases() throws IOException, ConverterException, ValidatorException;
}
