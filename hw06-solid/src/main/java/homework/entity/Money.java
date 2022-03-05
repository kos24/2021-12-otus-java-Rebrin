package homework.entity;

import homework.Banknote;

import java.util.EnumMap;
import java.util.Map;

public class Money {

    private Map<Banknote, Integer> moneyBalance = new EnumMap<>(Banknote.class);

    public Money() {
        for (var banknote : Banknote.values()) {
            moneyBalance.put(banknote, 0);
        }
    }

    public Map<Banknote, Integer> getMoneyBalance() {
        return moneyBalance;
    }

    public void setMoneyBalance(Map<Banknote, Integer> moneyBalance) {
        this.moneyBalance = moneyBalance;
    }
}
