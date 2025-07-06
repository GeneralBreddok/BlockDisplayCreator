package me.general_breddok.blockdisplaycreator.commandparser.exception;

public class InvalidCommandArgumentException extends CommandParseException {
    public InvalidCommandArgumentException(String commandLine) {
        super(commandLine);
    }

    public InvalidCommandArgumentException(String s, String commandLine) {
        super(s, commandLine);
    }

    public InvalidCommandArgumentException(String message, Throwable cause, String commandLine) {
        super(message, cause, commandLine);
    }

    public InvalidCommandArgumentException(Throwable cause, String commandLine) {
        super(cause, commandLine);
    }
}
