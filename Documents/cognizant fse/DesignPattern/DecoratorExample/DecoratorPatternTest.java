package DesignPattern.DecoratorExample;

public class DecoratorPatternTest {
    public static void main(String args[])
    {
        Notifier notifier = new SlackNotifierDecorator(new SmsNotifierDecorator(new EmailNotifier()));
        notifier.send();
    }   
    
}
