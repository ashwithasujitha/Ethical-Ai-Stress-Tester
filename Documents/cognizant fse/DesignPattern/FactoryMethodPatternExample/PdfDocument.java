package DesignPattern.FactoryMethodPatternExample;

public class PdfDocument implements InnerDocument {
    @Override
    public void open() {
        System.out.println("Opening PDF Document...");
    }
    
}
