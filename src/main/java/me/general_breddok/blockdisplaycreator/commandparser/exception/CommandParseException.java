package me.general_breddok.blockdisplaycreator.commandparser.exception;

import lombok.Getter;

@Getter
public class CommandParseException extends IllegalArgumentException {
    private final String commandLine;

    public CommandParseException(String commandLine) {
        this.commandLine = commandLine;
    }

    public CommandParseException(String message, String commandLine) {
        super(message);
        this.commandLine = commandLine;
    }

    public CommandParseException(String message, Throwable cause, String commandLine) {
        super(message, cause);
        this.commandLine = commandLine;
    }

    public CommandParseException(Throwable cause, String commandLine) {
        super(cause);
        this.commandLine = commandLine;
    }
}
