package DesignPattern.FactoryMethodPatternExample;

public class ExcelDocumentfactory extends DocumentFactory {
    @Override
    public InnerDocument createDocument() {
        return new ExcelDocument();
    }
  
}
