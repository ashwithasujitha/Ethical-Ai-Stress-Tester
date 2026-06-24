package DesignPattern.DecoratorExample;

public class EmailNotifier implements Notifier {
    @Override
    public void send() {
        System.out.println("Sending email notification...");
    }
    
}
