package DSA.FinancialForecasting;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        System.out.println("Enter the present value, interest rate (as a decimal), and number of years:");
        Scanner scanner = new java.util.Scanner(System.in);
        double presentValue = scanner.nextDouble();
        double interestRate = scanner.nextDouble()/100;
        int years = scanner.nextInt();
        double futureValue = Recursion.calculateFutureValue(presentValue, interestRate, years);
        System.out.println("The future value is: " + futureValue);
    }
}
