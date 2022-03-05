package homework.command;

import homework.service.ATM;

@FunctionalInterface
public interface Command {
    void execute(ATM someATM);
}

