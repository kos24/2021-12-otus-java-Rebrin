package homework;


import java.util.*;

public class CustomerService {

    TreeMap<Customer, String> customerList = new TreeMap<>();
    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    public Map.Entry<Customer, String> getSmallest() {
//        Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        List<Map.Entry<Customer, String >> list = new ArrayList<>(customerList.entrySet());
        list.sort(Map.Entry.comparingByKey(Comparator.comparing(Customer::getScores)));

        return new Map.Entry<>() {
            @Override
            public Customer getKey() {
                Customer customer = list.get(0).getKey();
                return new Customer(customer.getId(), customer.getName(), customer.getScores());
            }

            @Override
            public String getValue() {
                return list.get(0).getValue();
            }

            @Override
            public String setValue(String value) {
                return list.get(0).setValue(value);
            }
        };
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return customerList.ceilingEntry(customer);
    }

    public void add(Customer customer, String data) {
        customerList.put(customer, data);
    }
}
