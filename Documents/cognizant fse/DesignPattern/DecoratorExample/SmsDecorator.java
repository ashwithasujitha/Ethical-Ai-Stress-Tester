package DesignPattern.DecoratorExample;

public class SmsDecorator extends NotifierDecorator {
    public SmsDecorator(Notifier notifier) {
        super(notifier);
    }

    @Override
    public void send() {
        super.send();
        System.out.println("Sending SMS notification...");
       
    }

    
    
    
}
