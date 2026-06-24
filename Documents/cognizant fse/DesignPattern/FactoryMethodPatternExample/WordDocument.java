package DesignPattern.FactoryMethodPatternExample;

public class WordDocument implements InnerDocument {
    @Override
    public void open() {
        System.out.println("Opening Word Document...");
    }
    
}
