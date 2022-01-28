package homework;

import java.util.*;

public class CustomerService {

    TreeMap<Customer, String> customerList = new TreeMap<>(Comparator.comparingLong(Customer::getScores));
    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    public Map.Entry<Customer, String> getSmallest() {
//        Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        Customer customer = customerList.firstEntry().getKey();
        String value = customerList.firstEntry().getValue();

        return Map.entry(new Customer(customer.getId(), customer.getName(), customer.getScores()), value);
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        if (Objects.isNull(customerList.higherEntry(customer))) {
            return null;
        }
        Customer tempCustomer = customerList.higherEntry(customer).getKey();
        String value = customerList.higherEntry(customer).getValue();
        return Map.entry(new Customer(tempCustomer.getId(), tempCustomer.getName(), tempCustomer.getScores()), value);
    }

    public void add(Customer customer, String data) {
        customerList.put(customer, data);
    }
}
