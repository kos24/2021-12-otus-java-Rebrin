package homework.view;

import homework.Banknote;

import java.util.EnumMap;
import java.util.Map;

public class InfoViewModel {

    private Map<Banknote, Integer> money = new EnumMap<>(Banknote.class);

    public Map<Banknote, Integer> getMoney() {
        if (money.isEmpty()) {
            for (var banknote : Banknote.values()) {
                money.put(banknote, 0);
            }
        }
        return money;
    }

    public void setMoney(Map<Banknote, Integer> money) {
        this.money = money;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        money.entrySet().stream()
                .filter(m -> m.getValue() != 0)
                .forEach(entry -> sb.append(
                        String.format("Купюр номиналом %s рублей: %d шт.%n",
                                entry.getKey().getFaceValue(), entry.getValue()))
                );
        sb.append(String.format("Итого: %d рублей", getTotal()));
        return sb.toString();
    }

    public int getTotal() {
        return money.entrySet().stream()
                .map(entry -> entry.getKey().getFaceValue() * entry.getValue())
                .reduce(0, Integer::sum);
    }
}