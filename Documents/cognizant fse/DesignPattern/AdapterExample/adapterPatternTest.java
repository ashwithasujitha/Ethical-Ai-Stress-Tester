package DesignPattern.AdapterExample;

public class adapterPatternTest {
    public static void main(String[] args) {
        PaymentProcessor paypalProcessor = new PaypalAdapter(new Paypalgateway());
        paypalProcessor.processPayment(100.0);

        PaymentProcessor stripeProcessor = new StripeAdapter(new Stripegateway());
        stripeProcessor.processPayment(200.0);
    }
}
