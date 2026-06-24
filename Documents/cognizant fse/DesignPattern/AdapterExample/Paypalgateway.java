package DesignPattern.AdapterExample;

public class Paypalgateway {
    public void sendPayment(double amount) {
        System.out.println("Processing payment of " + amount + " through PayPal.");
    }
}
