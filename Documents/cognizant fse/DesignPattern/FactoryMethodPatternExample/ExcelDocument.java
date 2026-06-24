package DesignPattern.FactoryMethodPatternExample;

public class ExcelDocument implements InnerDocument {
    @Override
    public void open() {
        System.out.println("Opening Excel Document...");
    }
    
}
