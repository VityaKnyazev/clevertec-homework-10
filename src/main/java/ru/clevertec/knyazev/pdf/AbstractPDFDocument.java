package ru.clevertec.knyazev.pdf;


import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.clevertec.knyazev.pdf.exception.PDFDocumentException;

import java.io.File;
import java.io.IOException;

/**
 * Represents abstraction for working with pdf document (creating, adding data, closing)
 */

@RequiredArgsConstructor
public non-sealed abstract class AbstractPDFDocument implements PDFDocumentElements {

    @Getter
    protected final String pdfPath;

    /**
     * Create document using existing pdf document path with template and
     * result pdf document that storing data
     *
     * @param pdfTemplatePath existing pdf template path to document
     * @param pdfPath         result pdf document path
     * @param pdfFontPath     path to font of new pdf document
     * @param fontEncoding    font encoding
     * @return pdf document
     * @throws PDFDocumentException when pdf creating error
     */
    protected Document createDocument(String pdfTemplatePath,
                                      String pdfPath,
                                      String pdfFontPath,
                                      String fontEncoding) throws PDFDocumentException {
        try {
            Document document = new Document(new PdfDocument(new PdfReader(pdfTemplatePath),
                    new PdfWriter(new File(pdfPath))));

            FontProgram fontProgram = FontProgramFactory.createFont(pdfFontPath);
            PdfFont font = PdfFontFactory.createFont(fontProgram, fontEncoding);
            document.setFont(font);

            return document;
        } catch (IOException e) {
            throw new PDFDocumentException(e);
        }
    }

    /**
     * Create document using result pdf document path that storing data
     *
     * @param pdfPath         result pdf document path
     * @param pdfFontPath     path to font of new pdf document
     * @param fontEncoding    font encoding
     * @return pdf document
     * @throws PDFDocumentException when pdf creating error
     */
    protected Document createDocument(String pdfPath,
                                      String pdfFontPath,
                                      String fontEncoding) throws PDFDocumentException {
        try {
            Document document = new Document(new PdfDocument(new PdfWriter(new File(pdfPath))));

            FontProgram fontProgram = FontProgramFactory.createFont(pdfFontPath);
            PdfFont font = PdfFontFactory.createFont(fontProgram, fontEncoding);
            document.setFont(font);

            return document;
        } catch (IOException e) {
            throw new PDFDocumentException(e);
        }
    }

    /**
     * Create pdf document for working with
     *
     * @return pdf document
     * @throws PDFDocumentException
     */
    public abstract Document createDocument() throws  PDFDocumentException;

    /**
     * Close existing pdf document
     *
     * @param document existing pdf document
     */
    public static void closeDocument(Document document) {
        if (document != null) {
            document.close();
        }
    }
}
