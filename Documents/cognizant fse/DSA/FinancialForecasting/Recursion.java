package DSA.FinancialForecasting;

public class Recursion {
    public static double calculateFutureValue(double presentValue, double interestRate, int years) {
        if (years == 0) {
            return presentValue;
        } else {
            return calculateFutureValue(presentValue * (1 + interestRate), interestRate, years - 1);
        }
    }
}
