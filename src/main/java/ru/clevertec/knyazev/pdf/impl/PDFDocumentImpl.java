package ru.clevertec.knyazev.pdf.impl;

import com.itextpdf.layout.Document;
import ru.clevertec.knyazev.pdf.AbstractPDFDocument;
import ru.clevertec.knyazev.pdf.exception.PDFDocumentException;

public final class PDFDocumentImpl extends AbstractPDFDocument {

    private final String pdfTemplatePath;

    private final String pdfFontPath;
    private final String pdfFontEncoding;

    public PDFDocumentImpl(String pdfTemplatePath,
                           String pdfPath,
                           String pdfFontPath,
                           String pdfFontEncoding) {
        super(pdfPath);

        this.pdfTemplatePath = pdfTemplatePath;
        this.pdfFontPath = pdfFontPath;
        this.pdfFontEncoding = pdfFontEncoding;
    }

    /**
     * @implSpec
     */
    @Override
    public Document createDocument() throws PDFDocumentException {

        return super.createDocument(pdfTemplatePath,
                pdfPath,
                pdfFontPath,
                pdfFontEncoding);
    }
}
