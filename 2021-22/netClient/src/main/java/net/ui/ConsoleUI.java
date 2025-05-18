package net.ui;

import net.core.UI;
import net.core.command.Command;
import net.core.command.Connect;
import net.core.command.Disconnect;
import net.core.command.Send;

public class ConsoleUI implements UI {
    private final ConsoleConnector console;

    public ConsoleUI(ConsoleConnector console) {
        this.console = console;
    }

    @Override
    public Command getCommand() {
        Command command = null;
        while (command == null) {
            console.write("> ");
            String input = console.read();
            command = parseCommand(input);
            System.out.println("comm = " + command);
        }
        return command;
    }

    Command parseCommand(String line) {
        line = line.strip();
        var lowerLine = line.toLowerCase();
        if (lowerLine.equals("connect")) {
            return parseConnect(line);
        }
        else if (lowerLine.startsWith("send ")) {
            return parseSend(line);
        }
        else if (lowerLine.equals("disconnect")) {
            return parseDisconnect(line);
        }
        else {
            //console.write("unknown command");
            return null;
        }
    }

    private Connect parseConnect(String line) {
        return new Connect();
    }

    private Send parseSend(String line) {
        line = line.substring("send ".length());
        return new Send(line);
    }

    private Disconnect parseDisconnect(String line) {
        return new Disconnect();
    }

    @Override
    public void showMessage(String message) {
        console.write(message);
    }
}
