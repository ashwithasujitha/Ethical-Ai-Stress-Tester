package DesignPattern.DependencyInjectionExample;

public class DependencyInjectionTest {
    public static void main(String[] args) {
        CustomerRepository customerRepository = new CustomerRepositoryImpl();
        CustomerService customerService = new CustomerService(customerRepository);
        customerService.getCustomerById(22);
    }
}
