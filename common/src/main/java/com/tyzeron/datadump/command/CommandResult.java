package com.tyzeron.datadump.command;


/**
 * Represents the result of a command execution
 */
public class CommandResult {

    private final boolean success;
    private final String message;
    private final ResultType type;

    private CommandResult(boolean success, String message, ResultType type) {
        this.success = success;
        this.message = message;
        this.type = type;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public ResultType getType() {
        return type;
    }

    public static CommandResult success(String message) {
        return new CommandResult(true, message, ResultType.SUCCESS);
    }

    public static CommandResult error(String message) {
        return new CommandResult(false, message, ResultType.ERROR);
    }

    public static CommandResult info(String message) {
        return new CommandResult(true, message, ResultType.INFO);
    }

    public enum ResultType {
        SUCCESS,
        ERROR,
        INFO
    }

}
