package DesignPattern.BuilderPatternExample;

public class BuilderPatternTest {
    public static void main(String args[])
    {
        Computer gamingPc = new Computer.Builder()
            
            .setCPU("Intel Core i9")
            .setRAM("32")
            .setStorage(1000)
            .build();

            gamingPc.display();
            
            System.out.println();

            Computer officePc = new Computer.Builder()
            .setCPU("Intel Core i5")
            .setRAM("16")
            .setStorage(500)
            .build();

            officePc.display();

        
    }
}
