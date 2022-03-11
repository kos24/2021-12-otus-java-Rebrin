package homework.command;

import homework.service.ATM;

public class Balance implements Command {

    @Override
    public void execute(ATM someATM) {
        System.out.println("Баланс в банкомате:");
        System.out.println(someATM.getBalance());
    }
}