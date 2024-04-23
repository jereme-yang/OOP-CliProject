package oop.project.cli;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ParsedArguments {
    private String[] parsedInputs;

    /**
     * Sets the parsed input arguments.
     *
     * @param parsedInputs The parsed input arguments
     */
    public void setParsedInputs(String[] parsedInputs) {
        this.parsedInputs = parsedInputs;
    }

    /**
     * Gets the parsed input arguments.
     *
     * @return The parsed input arguments
     */
    public String[] getParsedInputs() {
        return parsedInputs;
    }

    //stored arguments
    static HashMap<String, String[]> map = new HashMap<>();

    static {
        map.put("add", new String[]{"double", "double"});
        map.put("sub", new String[]{"flag", "double", "flag", "double"});
        map.put("sqrt", new String[]{"double"});
        map.put("calc", new String[]{"command"});
        map.put("date", new String[]{"string"});
        map.put("--left", new String[]{"double"});
        map.put("--right", new String[]{"double"});

    }

    private String[] commands = {"add", "sub", "sqrt", "calc", "date"};
    private String[] flags = {"--left", "--right"};

    /**
     * Adds a command to the list of available commands.
     *
     * @param command The command to add
     */
    public void addCommand(String command) {
        String[] newArray = Arrays.copyOf(commands, commands.length + 1);
        newArray[newArray.length - 1] = command;
        commands = newArray;
    }

    /**
     * Adds a flag to the list of available flags.
     *
     * @param flag The flag to add
     */
    public void addFlag(String flag) {
        String[] newArray = Arrays.copyOf(flags, flags.length + 1);
        newArray[newArray.length - 1] = flag;
        flags = newArray;
    }

    private String errMessage;

    /**
     * Validates the parsed arguments against an example.
     *
     * @param example The example against which to validate the arguments
     * @return A validation message
     */
    public String validateAgainst(String[] example) {
        errMessage = "";
        String expectedUsage = "\nExpected Arguments: " + Arrays.toString(example);
        for (int i = 0; i < parsedInputs.length; i++) {
            if (example[i].equals("flag")) {
                checkFlag(example, i);
            } else if (example[i].equals("command")) {
                checkCom(example, i);
            } else if (example[i].equals("double")) {
                try {
                    Double.parseDouble(parsedInputs[i]);
                } catch (NumberFormatException e) {
                    return "Argument at index " + i + " is not a double" + expectedUsage;
                }
            }

            if (errMessage != "") {
                return errMessage + expectedUsage;
            }
        }
        return "Valid";
    }

    /**
     * Checks if a given argument is a flag and the flag has the necessary arguments.
     *
     * @param example The example against which to validate the arguments
     * @param index The index of the argument to check
     */
    private void checkFlag(String[] example, int index) {
        if (!isFlag(parsedInputs[index])) {
            errMessage = "Argument at index " + index + " is not a valid flag";
            return;
        }
        int numArgs = map.get(parsedInputs[index]).length;
        ++index;
        if (index + numArgs > parsedInputs.length) {
            errMessage = "Missing argument for flag at index " + index;
        }
    }

    /**
     * Checks if a given argument is a command.
     *
     * @param example The example against which to validate the arguments
     * @param index The index of the argument to check
     */
    private void checkCom(String[] example, int index) {
        if (!isCommand(parsedInputs[index])) {
            errMessage = "Argument at index " + index + " is not a valid command";
        }
    }

    /**
     * Checks if a given argument is a flag.
     *
     * @param flag The flag to check
     * @return true if the argument is a flag, false otherwise
     */
    private boolean isFlag(String flag) {
        for (String f : flags) {
            if (f.equals(flag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a given argument is a command.
     *
     * @param command The command to check
     * @return true if the argument is a command, false otherwise
     */
    private boolean isCommand(String command) {
        for (String cmd : commands) {
            if (cmd.equals(command)) {
                return true;
            }
        }
        return false;
    }

}
