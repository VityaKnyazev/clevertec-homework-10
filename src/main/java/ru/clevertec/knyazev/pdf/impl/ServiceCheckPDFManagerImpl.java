package ru.clevertec.knyazev.pdf.impl;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import ru.clevertec.knyazev.data.ServiceDTO;
import ru.clevertec.knyazev.pdf.AbstractPDFDocument;
import ru.clevertec.knyazev.pdf.PDFManager;

import java.io.File;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public class ServiceCheckPDFManagerImpl extends PDFManager<List<ServiceDTO>> {

    private final static String FILENAME_PREFIX = "service-check-";
    private final static String FILENAME_DATE_TIME_FORMAT_PREFIX = "dd-MM-YYYY HH-mm-ss-SSS";
    private final static String FILENAME_SUFFIX = ".pdf";

    private final static String TITLE_PARAGRAPH = "Person services check #%s%n";
    private final static String DATE_TIME_PARAGRAPH = "Date time: %s%n";
    private final static String DATE_TIME_FORMAT_PARAGRAPH = "dd-MM-YYYY HH:mm:ss";
    private final static String CHECK_PARAGRAPH = "For person: %s %s%n";

    private final static List<String> DATA_TABLE_COLUMNS = List.of("service name",
            "service description",
            "price");
    private final static String SUM_TABLE_COLUMN = "total:";

    private final String pdfTemplatePath;
    private final String pdfPath;
    private final String pdfFontPath;
    private final String pdfFontEncoding;

    private AbstractPDFDocument pdfDocumentImpl;

    /**
     * @implSpec
     */
    @Override
    protected Document create() {
        String pdfPathName = pdfPath + File.separator + FILENAME_PREFIX +
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern(FILENAME_DATE_TIME_FORMAT_PREFIX, Locale.ROOT)) +
                FILENAME_SUFFIX;

        pdfDocumentImpl = new PDFDocumentImpl(pdfTemplatePath,
                pdfPathName,
                pdfFontPath,
                pdfFontEncoding);

        return pdfDocumentImpl.createDocument();
    }

    /**
     * @implSpec
     */
    @Override
    protected Document add(List<ServiceDTO> objectData, Document document) {
        Paragraph titleParagraph = pdfDocumentImpl.createParagraph(
                String.format(TITLE_PARAGRAPH, objectData.get(0).personId().toString()),
                85f);
        Paragraph dateTimeParagraph = pdfDocumentImpl.createParagraph(
                String.format(DATE_TIME_PARAGRAPH, objectData.get(0).localDateTime()
                        .format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PARAGRAPH))),
                3f);
        Paragraph checkParagraph = pdfDocumentImpl.createParagraph(
                String.format(CHECK_PARAGRAPH, objectData.get(0).personName(),
                        objectData.get(0).personSurname()), 3f);

        Table dataTable = pdfDocumentImpl.createTable(new float[]{150f, 400f, 45f}, 3f);
        pdfDocumentImpl.addCell(DATA_TABLE_COLUMNS.get(0),
                TextAlignment.CENTER,
                dataTable);
        pdfDocumentImpl.addCell(DATA_TABLE_COLUMNS.get(1),
                TextAlignment.CENTER,
                dataTable);
        pdfDocumentImpl.addCell(DATA_TABLE_COLUMNS.get(2),
                TextAlignment.CENTER,
                dataTable);

        objectData.forEach(serviceDTO -> {
            pdfDocumentImpl.addCell(serviceDTO.serviceName(),
                    TextAlignment.LEFT,
                    dataTable);
            pdfDocumentImpl.addCell(serviceDTO.description(),
                    TextAlignment.CENTER,
                    dataTable);
            pdfDocumentImpl.addCell(DecimalFormat.getCurrencyInstance().format(serviceDTO.price()),
                    TextAlignment.CENTER,
                    dataTable);
        });

        Table sumTable = pdfDocumentImpl.createTable(new float[]{495f, 35f}, 3f);
        pdfDocumentImpl.addCell(SUM_TABLE_COLUMN,
                TextAlignment.LEFT,
                sumTable);
        pdfDocumentImpl.addCell(DecimalFormat.getCurrencyInstance()
                        .format(objectData.stream()
                                .mapToDouble(serviceDTO -> serviceDTO.price().doubleValue())
                                .sum()),
                TextAlignment.CENTER,
                sumTable);

        document.add(titleParagraph);
        document.add(dateTimeParagraph);
        document.add(checkParagraph);
        document.add(dataTable);
        document.add(sumTable);

        return document;
    }

    /**
     * @implSpec
     */
    @Override
    protected String saveAndClose(Document document) {
        String absolutePDFPath = Path.of(pdfDocumentImpl.getPdfPath())
                .toFile().getAbsolutePath();

        PDFDocumentImpl.closeDocument(document);
        pdfDocumentImpl = null;

        return absolutePDFPath;
    }
}
