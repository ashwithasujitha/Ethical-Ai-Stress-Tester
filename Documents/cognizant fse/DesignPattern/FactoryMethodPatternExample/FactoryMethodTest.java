package DesignPattern.FactoryMethodPatternExample;

public class FactoryMethodTest {
    public static void main(String[] args) {
    DocumentFactory pdfFactory = new PdfDocumentFactory(); 
    InnerDocument pdfDoc = pdfFactory.createDocument();
    pdfDoc.open();

    DocumentFactory wordFactory = new WordDocumentFactory(); 
    InnerDocument wordDoc = wordFactory.createDocument();
    wordDoc.open();


    DocumentFactory excelFactory = new ExcelDocumentfactory();
    InnerDocument excelDoc = excelFactory.createDocument();
    excelDoc.open();
    }
    
}
