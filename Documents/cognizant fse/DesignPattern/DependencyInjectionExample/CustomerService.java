package DesignPattern.DependencyInjectionExample;

public class CustomerService {
    private CustomerRepository customerRepository;
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    public void getCustomerById(int id) {
        String customer = customerRepository.findCustomerById(id);
        System.out.println(customer);
    }
}
