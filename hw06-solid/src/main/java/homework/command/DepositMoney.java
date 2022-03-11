package homework.command;

import homework.service.ATM;
import homework.Banknote;

public class DepositMoney implements Command {

    private final Banknote banknote;
    private final Integer amount;

    public DepositMoney(Banknote banknote, Integer amount) {
        this.banknote = banknote;
        this.amount = amount;
    }

    @Override
    public void execute(ATM someATM) {
        someATM.depositMoney(banknote, amount);
    }
}