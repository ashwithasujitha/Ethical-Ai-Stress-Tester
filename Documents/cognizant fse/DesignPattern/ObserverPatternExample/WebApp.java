package DesignPattern.ObserverPatternExample;

public class WebApp implements Observer {
    private String user;

    public WebApp(String user) {
        this.user= user;
    }

    @Override
    public void update(double price, String name) {
        System.out.println("Web App[ " + user + "]" + name + " price updated to " + price);
    }
    
}
