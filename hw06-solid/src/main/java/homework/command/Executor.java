package homework.command;

import homework.service.ATM;

import java.util.ArrayDeque;
import java.util.Queue;

public class Executor {

    private final ATM atm;
    private final Queue<Command> commands = new ArrayDeque<>();

    public Executor(ATM atm) {
        this.atm = atm;
    }

    public void addCommand(Command command) {
        commands.add(command);
    }

    public void executeCommands() {
        Command command;
        while ((command = commands.poll()) != null) {
            command.execute(atm);
        }
    }
}