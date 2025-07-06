package me.general_breddok.blockdisplaycreator.commandparser.exception;

public class InvalidNumberOfCommandArgumentsException extends CommandParseException {
    public InvalidNumberOfCommandArgumentsException(String commandLine) {
        super(commandLine);
    }

    public InvalidNumberOfCommandArgumentsException(String s, String commandLine) {
        super(s, commandLine);
    }

    public InvalidNumberOfCommandArgumentsException(String message, Throwable cause, String commandLine) {
        super(message, cause, commandLine);
    }

    public InvalidNumberOfCommandArgumentsException(Throwable cause, String commandLine) {
        super(cause, commandLine);
    }
}
