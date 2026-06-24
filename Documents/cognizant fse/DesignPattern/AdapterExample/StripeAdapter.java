package DesignPattern.AdapterExample;

public class StripeAdapter implements PaymentProcessor{
    private Stripegateway stripeGateway;

    public StripeAdapter(Stripegateway stripeGateway) {
        this.stripeGateway = stripeGateway;
    }

    @Override
    public void processPayment(double amount) {
        stripeGateway.makePayment(amount);
    }
    
}
