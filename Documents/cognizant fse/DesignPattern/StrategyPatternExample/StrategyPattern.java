package DesignPattern.StrategyPatternExample;

public class StrategyPattern {
    public static void main(String[] args) {
        PaymentStrategy creditCardPayment = new CreditCardPayment();
        PaymentStrategy payPalPayment = new PayPalPayment();
        
        Paymentcontext context = new Paymentcontext(creditCardPayment);
        context.executePayment(1000.0);
        
        context = new Paymentcontext(payPalPayment);
        context.executePayment(2000.0);
    }
}
