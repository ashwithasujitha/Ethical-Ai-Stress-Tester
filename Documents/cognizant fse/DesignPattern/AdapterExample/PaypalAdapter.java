package DesignPattern.AdapterExample;

public class PaypalAdapter implements PaymentProcessor {
    private Paypalgateway paypalGateway;

    public PaypalAdapter(Paypalgateway paypalGateway) {
        this.paypalGateway = paypalGateway;
    }

    @Override
    public void processPayment(double amount) {
        paypalGateway.sendPayment(amount);
    }
    
}
