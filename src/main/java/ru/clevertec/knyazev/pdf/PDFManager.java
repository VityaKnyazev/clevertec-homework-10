package ru.clevertec.knyazev.pdf;


import com.itextpdf.layout.Document;
import ru.clevertec.knyazev.pdf.exception.PDFDocumentException;

/**
 * Represents manager that working with pdf document
 *
 * @param <T> data object type that should be added to pdf
 *            document
 */
public abstract class PDFManager<T> {

    /**
     * Manage given pdf document (create pdf, add object data to it
     * than save and close document).
     *
     * @param objectData data for adding to pdf document
     * @return absolute path of saved pdf document
     */
    public String manage(T objectData) {
        Document document = create();
        add(objectData, document);
        return saveAndClose(document);
    }

    /**
     *
     * Create pdf document
     *
     * @return pdf document
     * @throws PDFDocumentException
     */
    abstract protected Document create() throws PDFDocumentException;

    /**
     *
     * Add data to created pdf document
     *
     * @param objectData object data to add to pdf document
     * @param document pdf document
     * @return pdf document with added data
     */
    abstract protected Document add(T objectData, Document document);

    /**
     *
     * Save pdf document and close it
     *
     * @param document pdf document that should be saved and closed
     * @return absolute path to saved pdf file
     */
    abstract protected String saveAndClose(Document document);
}
