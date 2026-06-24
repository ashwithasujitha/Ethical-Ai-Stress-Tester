package DesignPattern.AdapterExample;

public class Stripegateway {
    public void makePayment(double amount) {
        System.out.println("Processing payment of " + amount + " through Stripe.");
    }
}
