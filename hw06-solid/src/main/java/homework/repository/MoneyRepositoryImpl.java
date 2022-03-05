package homework.repository;

import homework.entity.Money;

public class MoneyRepositoryImpl implements MoneyRepository {

    private final Money money;

    public MoneyRepositoryImpl(Money money) {
        this.money = money;
    }

    @Override
    public Money getMoney() {
        return money;
    }
}