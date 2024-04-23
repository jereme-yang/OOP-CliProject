package oop.project.cli;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Scenarios {

    /**
     * Parses and returns the arguments of a command (one of the scenarios
     * below) into a Map of names to values. This method is provided as a
     * starting point that works for most groups, but depending on your command
     * structure and requirements you may need to make changes to adapt it to
     * your needs - use whatever is convenient for your design.
     */
    private static ParsedArguments parsedArgs = new ParsedArguments();

    /**
     * Parses and returns the arguments of a command into a Map of names to values.
     *
     * @param command The command to parse
     * @return A Map of names to values
     * @throws IllegalArgumentException If the command is not recognized or is invalid
     */
    public static Map<String, Object> parse(String command) {
        //This assumes commands follow a similar structure to unix commands,
        //e.g. `command [arguments...]`. If your project uses a different
        //structure, e.g. Lisp syntax like `(command [arguments...])`, you may
        //need to adjust this a bit to work as expected.
        Pattern pattern = Pattern.compile("--\\w+|\"[^\"]*\"|\\S+");
        Matcher matcher = pattern.matcher(command);
        List<String> tokens = new ArrayList<>();

        while (matcher.find()) {
            String token = command.substring(matcher.start(), matcher.end());
            tokens.add(token);
        }

        String[] tokenArray = tokens.toArray(new String[0]);
        for (String token : tokenArray) {
            System.out.println("Token: " + token);
        }

        var base = tokenArray[0];
        String[] arguments = tokenArray.length > 1 ? Arrays.copyOfRange(tokenArray, 1, tokenArray.length) : new String[0];
        parsedArgs.setParsedInputs(arguments);

        return switch (base) {
            case "add" -> add();
            case "sub" -> sub();
            case "sqrt" -> sqrt();
            case "calc" -> calc();
            case "date" -> date();
            default -> throw new IllegalArgumentException("Unknown command: " + base);
        };
    }

    /**
     * Takes two positional arguments:
     *  - {@code left: <your integer type>}
     *  - {@code right: <your integer type>}
     *
     * @return A Map containing the left and right values
     * @throws IllegalArgumentException If the arguments are invalid
     */
    private static Map<String, Object> add() {
        String message = parsedArgs.validateAgainst(parsedArgs.map.get("add"));
        if (message != "Valid") {
            throw new IllegalArgumentException(message);
        }

        String[] args = parsedArgs.getParsedInputs();

        int left = Integer.parseInt(args[0]);
        int right = Integer.parseInt(args[1]);
        return Map.of("left", left, "right", right);
    }

    /**
     * Takes two <em>named</em> arguments:
     *  - {@code left: <your decimal type>} (optional)
     *     - If your project supports default arguments, you could also parse
     *       this as a non-optional decimal value using a default of 0.0.
     *  - {@code right: <your decimal type>} (required)
     *
     * @return A Map containing the left and right values
     * @throws IllegalArgumentException If the arguments are invalid
     */
    static Map<String, Object> sub() {
        String message = parsedArgs.validateAgainst(parsedArgs.map.get("sub"));
        if (message != "Valid") {
            throw new IllegalArgumentException(message);
        }

        String[] args = parsedArgs.getParsedInputs();

        Optional<Double> left = Optional.empty();
        double right = 0.0;
        if (args.length == 2 && args[0].equals("--right")) {
            right = Double.parseDouble(args[1]);
        }
        else if (args.length == 4 && args[0].equals("--left") && args[2].equals("--right")) {
            right = Double.parseDouble(args[3]);
            left = Optional.of(Double.parseDouble(args[1]));
        }
        else {
            throw new IllegalArgumentException("Invalid command structure for 'sub'. Usage: sub --left <value> --right <value>");
        }

        return left.equals(Optional.empty()) ? Map.of("left", left, "right", right) :
                Map.of("left", left.get(), "right", right);
    }

    /**
     * Takes one positional argument:
     *  - {@code number: <your integer type>} where {@code number >= 0}
     *
     * @return A Map containing the number value
     * @throws IllegalArgumentException If the argument is invalid
     */
    static Map<String, Object> sqrt() {
        String message = parsedArgs.validateAgainst(parsedArgs.map.get("sqrt"));
        if (message != "Valid") {
            throw new IllegalArgumentException(message);
        }

        String[] args = parsedArgs.getParsedInputs();
        if (args.length != 1) {
            throw new IllegalArgumentException("Exactly one argument is required for 'sqrt'.");
        }
        int number;
        try {
            number = Integer.parseInt(args[0]);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("sqrt requires a number");
        }
        if (number < 0) {
            throw new IllegalArgumentException("positional argument must be positive");
        }
        return Map.of("number", number);
    }

    /**
     * Takes one positional argument:
     *  - {@code subcommand: "add" | "div" | "sqrt" }, aka one of these values.
     *     - Note: Not all projects support subcommands, but if yours does you
     *       may want to take advantage of this scenario for that.
     *
     * @return A Map containing the subcommand value
     * @throws IllegalArgumentException If the argument is invalid
     */
    static Map<String, Object> calc() {
        String message = parsedArgs.validateAgainst(parsedArgs.map.get("calc"));
        if (message != "Valid") {
            throw new IllegalArgumentException(message);
        }

        String[] args = parsedArgs.getParsedInputs();
        if (args.length != 1) {
            throw new IllegalArgumentException("Exactly one argument is required for 'calc'.");
        }
        String subcommand = args[0];
        if (subcommand.equals("add") || subcommand.equals("sub") || subcommand.equals("sqrt")) {
            return Map.of("subcommand", subcommand);
        }
        throw new IllegalArgumentException("Unknown command: " + subcommand);
    }

    /**
     * Takes one positional argument:
     *  - {@code date: Date}, a custom type representing a {@code LocalDate}
     *    object (say at least yyyy-mm-dd, or whatever you prefer).
     *     - Note: Consider this a type that CANNOT be supported by your library
     *       out of the box and requires a custom type to be defined.
     *
     * @return A Map containing the date value
     * @throws IllegalArgumentException If the argument is invalid
     */
    static Map<String, Object> date() {
        String message = parsedArgs.validateAgainst(parsedArgs.map.get("date"));
        if (message != "Valid") {
            throw new IllegalArgumentException(message);
        }

        String[] args = parsedArgs.getParsedInputs();
        if (args.length != 1) {
            throw new IllegalArgumentException("Exactly one argument is required for 'date'.");
        }
        String[] elements = args[0].split("-");
        if (elements.length != 3) {
            throw new IllegalArgumentException("The correct format is yyyy-mm-dd");
        }
        LocalDate date = LocalDate.EPOCH;
        try {
            date = LocalDate.of(Integer.parseInt(elements[0]), Integer.parseInt(elements[1]), Integer.parseInt(elements[2]));
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("date must be integers");
        }
        return Map.of("date", date);
    }

    //TODO: Add your own scenarios based on your software design writeup. You
    //should have a couple from pain points at least, and likely some others
    //for notable features. This doesn't need to be exhaustive, but this is a
    //good place to test/showcase your functionality in context.

}
