package DesignPattern.FactoryMethodPatternExample;

public  class WordDocumentFactory extends DocumentFactory {
    @Override
    public InnerDocument createDocument() {
        return new WordDocument();
    }
}
