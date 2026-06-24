package DesignPattern.DependencyInjectionExample;

public class CustomerRepositoryImpl implements CustomerRepository {
    @Override
    public String findCustomerById(int id) {
        return "Customer with ID: " + id +", Name:Ashwitha";
    }

    
}
