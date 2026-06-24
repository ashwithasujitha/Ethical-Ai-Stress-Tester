package DesignPattern.FactoryMethodPatternExample;

public  class PdfDocumentFactory extends DocumentFactory {
    @Override
   public InnerDocument createDocument() {
        return new PdfDocument();
    }
}
