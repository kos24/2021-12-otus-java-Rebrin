package homework;

import homework.command.Balance;
import homework.command.DepositMoney;
import homework.command.Executor;
import homework.command.WithdrawMoney;
import homework.entity.Money;
import homework.repository.MoneyRepository;
import homework.repository.MoneyRepositoryImpl;
import homework.service.ConcreteATM;

public class Demo {

    public static void main(String[] args) {

        MoneyRepository moneyRepository = new MoneyRepositoryImpl(new Money());

        var atm = new ConcreteATM(moneyRepository);
        var executor = new Executor(atm);

        executor.addCommand(new DepositMoney(Banknote.ONE_THOUSAND, 2));
        executor.addCommand(new DepositMoney(Banknote.FIVE_HUNDRED, 2));
        executor.addCommand(new DepositMoney(Banknote.ONE_HUNDRED, 1));
        executor.addCommand(new Balance());
        executor.addCommand(new WithdrawMoney(2100));
        executor.addCommand(new Balance());
        executor.executeCommands();
    }


}