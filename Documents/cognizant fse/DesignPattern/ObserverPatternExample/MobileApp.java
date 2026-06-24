package DesignPattern.ObserverPatternExample;

public class MobileApp  implements Observer {
    private String user;

    public MobileApp(String user) {
        this.user= user;
    }

    @Override
    public void update(double price, String name) {
        System.out.println("Mobile App[ " + user + "]" + name + " price updated to " + price);
    }
    
}
