package homework.service;

import homework.Banknote;
import homework.entity.Money;
import homework.repository.MoneyRepository;
import homework.view.InfoViewModel;

import java.util.Map;
import java.util.Objects;

public class ConcreteATM implements ATM {

    private final MoneyRepository moneyRepository;

    public ConcreteATM(MoneyRepository moneyRepository) {
        this.moneyRepository = moneyRepository;
    }

    public void depositMoney(Banknote banknote, Integer amount) {
        Money money = moneyRepository.getMoney();
        if (Objects.nonNull(money)) {
            for (var entry : money.getMoneyBalance().entrySet()) {
                if (entry.getKey().equals(banknote)) {
                    entry.setValue(amount);
                    System.out.printf("Внесено купюр номиналом %d рублей: %d шт.%n",
                            entry.getKey().getFaceValue(), amount);
                }
            }
        }
    }

    public InfoViewModel withdrawMoney(Integer amount) {

        InfoViewModel infoViewModel = new InfoViewModel();
        Map<Banknote, Integer> moneyBalance = moneyRepository.getMoney().getMoneyBalance();
        int sumToWithdraw = 0;
        for (var entry : moneyBalance.entrySet()) {
            while (entry.getValue() != 0 && sumToWithdraw <= amount && entry.getKey().getFaceValue() + sumToWithdraw <= amount) {
                sumToWithdraw += entry.getKey().getFaceValue();
                entry.setValue(entry.getValue() - 1);
                infoViewModel.getMoney().put(entry.getKey(), infoViewModel.getMoney().get(entry.getKey()) + 1);
            }
        }
        if (sumToWithdraw == 0 || sumToWithdraw % amount != 0) {
            throw new RuntimeException("Невозможно выдать точно запрошенную сумму");
        }
        return infoViewModel;
    }

    @Override
    public InfoViewModel getBalance() {
        InfoViewModel infoViewModel = new InfoViewModel();
        infoViewModel.setMoney(moneyRepository.getMoney().getMoneyBalance());
        return infoViewModel;
    }
}

