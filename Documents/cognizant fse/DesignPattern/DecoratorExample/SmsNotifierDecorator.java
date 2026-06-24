package DesignPattern.DecoratorExample;

public class SmsNotifierDecorator extends NotifierDecorator {
    public SmsNotifierDecorator(Notifier notifier) {
        super(notifier);
    }

    @Override
    public void send() {
        super.send();
        System.out.println("Sending SMS notification...");
    }

  
    
}
