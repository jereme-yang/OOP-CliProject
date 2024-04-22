package oop.project.cli;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ParsedArguments {
    private String[] parsedInputs;

    public void setParsedInputs(String[] parsedInputs) {
        this.parsedInputs = parsedInputs;
    }

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

    public void addCommand(String command) {
        String[] newArray = Arrays.copyOf(commands, commands.length + 1);
        newArray[newArray.length - 1] = command;
        commands = newArray;
    }
    public void addFlag(String flag) {
        String[] newArray = Arrays.copyOf(flags, flags.length + 1);
        newArray[newArray.length - 1] = flag;
        flags = newArray;
    }

    private String errMessage;

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

    private void checkCom(String[] example, int index) {
        if (!isCommand(parsedInputs[index])) {
            errMessage = "Argument at index " + index + " is not a valid command";
        }
    }
    private boolean isFlag(String flag) {
        for (String f : flags) {
            if (f.equals(flag)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCommand(String command) {
        for (String cmd : commands) {
            if (cmd.equals(command)) {
                return true;
            }
        }
        return false;
    }

}
