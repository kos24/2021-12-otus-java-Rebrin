package homework;

import homework.command.Balance;
import homework.command.DepositMoney;
import homework.command.Executor;
import homework.command.WithdrawMoney;
import homework.entity.Money;
import homework.repository.MoneyRepository;
import homework.repository.MoneyRepositoryImpl;
import homework.service.ATM;
import homework.service.ConcreteATM;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;


class ATMTest {

    private MoneyRepository moneyRepository;
    private ATM atm;
    private Executor executor;
    private Money money;
    Map<Banknote, Integer> expectedBalance = new EnumMap<>(Banknote.class);

    @BeforeEach
    void setUp() {
        money = new Money();
        moneyRepository = new MoneyRepositoryImpl(new Money());
        atm = new ConcreteATM(moneyRepository);
        executor = new Executor(atm);
    }

    @Test
    void shouldInitialiseMoneyBalanceWithZeroAmountOfBanknotes() {

        expectedBalance.put(Banknote.ONE_THOUSAND, 0);
        expectedBalance.put(Banknote.FIVE_HUNDRED, 0);
        expectedBalance.put(Banknote.ONE_HUNDRED, 0);

        Money actualResult = new Money();

        Assertions.assertThat(actualResult.getMoneyBalance()).isEqualTo(expectedBalance);
    }

    @Test
    void shouldDepositCorrectAmountOfBanknotes() {

        money.getMoneyBalance().put(Banknote.ONE_THOUSAND, 3);
        money.getMoneyBalance().put(Banknote.FIVE_HUNDRED, 5);
        money.getMoneyBalance().put(Banknote.ONE_HUNDRED, 2);

        executor.addCommand(new DepositMoney(Banknote.ONE_THOUSAND, 3));
        executor.addCommand(new DepositMoney(Banknote.FIVE_HUNDRED, 5));
        executor.addCommand(new DepositMoney(Banknote.ONE_HUNDRED, 2));
        executor.executeCommands();

        Money actualResult = moneyRepository.getMoney();
        Assertions.assertThat(actualResult.getMoneyBalance()).isEqualTo(money.getMoneyBalance());
    }

    @Test
    void shouldGiveCorrectBalance() {

        executor.addCommand(new DepositMoney(Banknote.ONE_THOUSAND, 2));
        executor.addCommand(new DepositMoney(Banknote.FIVE_HUNDRED, 0));
        executor.addCommand(new DepositMoney(Banknote.ONE_HUNDRED, 1));
        executor.addCommand(new Balance());
        executor.executeCommands();

        int actualResult = atm.getBalance().getTotal();
        Assertions.assertThat(actualResult).isEqualTo(2100);
    }

    @Test
    void shouldWithdrawCorrectAmountUsingMinimumAmountOfBanknotes() {

        Map<Banknote, Integer> expectedBalance = new EnumMap<>(Banknote.class);
        expectedBalance.put(Banknote.ONE_THOUSAND, 1);
        expectedBalance.put(Banknote.FIVE_HUNDRED, 2);
        expectedBalance.put(Banknote.ONE_HUNDRED, 3);

        executor.addCommand(new DepositMoney(Banknote.ONE_THOUSAND, 3));
        executor.addCommand(new DepositMoney(Banknote.FIVE_HUNDRED, 2));
        executor.addCommand(new DepositMoney(Banknote.ONE_HUNDRED, 5));
        executor.addCommand(new WithdrawMoney(2200));
        executor.executeCommands();

        int actualTotal = atm.getBalance().getTotal();

        Assertions.assertThat(actualTotal).isEqualTo(2300);
        Assertions.assertThat(atm.getBalance().getMoney()).isEqualTo(expectedBalance);
    }

    @Test
    void shouldThrowExceptionWhenAmountNotAvailable() {

        executor.addCommand(new DepositMoney(Banknote.ONE_THOUSAND, 3));
        executor.addCommand(new DepositMoney(Banknote.FIVE_HUNDRED, 2));
        executor.addCommand(new DepositMoney(Banknote.ONE_HUNDRED, 5));
        executor.addCommand(new WithdrawMoney(10250));

        Assertions.assertThatThrownBy(()-> executor.executeCommands())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Невозможно выдать точно запрошенную сумму");

    }

}