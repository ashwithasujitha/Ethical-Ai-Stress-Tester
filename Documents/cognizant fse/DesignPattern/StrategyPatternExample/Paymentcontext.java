package DesignPattern.StrategyPatternExample;

public class Paymentcontext {
    private PaymentStrategy paymentStrategy;
    public Paymentcontext(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
    public void executePayment(double amount) {
        paymentStrategy.pay(amount);
    }
}
