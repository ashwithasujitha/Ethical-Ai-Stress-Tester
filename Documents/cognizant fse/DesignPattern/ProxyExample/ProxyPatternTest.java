package DesignPattern.ProxyExample;

public class ProxyPatternTest {
    public static void main(String[] args) {
        Image image = new ProxyImage("image1.jpg");
        

       
        System.out.println("");
        image.display();

      
       
        System.out.println("");

        System.out.println("Creating second image proxy...");
        image.display();
    }
}
