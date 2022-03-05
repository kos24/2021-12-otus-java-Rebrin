package homework.command;

import homework.service.ATM;

public class WithdrawMoney implements Command {

    private final Integer amount;

    public WithdrawMoney(Integer amount) {
        this.amount = amount;
    }

    @Override
    public void execute(ATM someATM) {
        System.out.println("Выдано:");
        System.out.println(someATM.withdrawMoney(amount));
    }
}