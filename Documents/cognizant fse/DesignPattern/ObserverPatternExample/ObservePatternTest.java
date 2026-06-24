package DesignPattern.ObserverPatternExample;

public class ObservePatternTest {
    public static void main(String args[]){
        StockMarket stockMarket = new StockMarket();
        Observer mobile = new MobileApp("Ashwitha");
        Observer web= new WebApp("Admin");
        stockMarket.registerObserver(mobile);
        stockMarket.registerObserver(web);
        stockMarket.setStock(4500, "TCS");
        stockMarket.setStock(1500, "INFOSYS");

    }
    
}
