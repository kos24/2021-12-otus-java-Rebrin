package homework.service;

import homework.Banknote;
import homework.view.InfoViewModel;

public interface ATM {

    void depositMoney(Banknote banknote, Integer amount);
    InfoViewModel withdrawMoney(Integer amount);
    InfoViewModel getBalance();
}