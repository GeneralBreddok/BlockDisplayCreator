package me.general_breddok.blockdisplaycreator.commandparser.exception;

public class InvalidCommandNameException extends CommandParseException {
    public InvalidCommandNameException(String commandLine) {
        super(commandLine);
    }

    public InvalidCommandNameException(String s, String commandLine) {
        super(s, commandLine);
    }

    public InvalidCommandNameException(String message, Throwable cause, String commandLine) {
        super(message, cause, commandLine);
    }

    public InvalidCommandNameException(Throwable cause, String commandLine) {
        super(cause, commandLine);
    }
}
