package oop.project.cli;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
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
    public static Map<String, Object> parse(String command) {
        //This assumes commands follow a similar structure to unix commands,
        //e.g. `command [arguments...]`. If your project uses a different
        //structure, e.g. Lisp syntax like `(command [arguments...])`, you may
        //need to adjust this a bit to work as expected.
        var split = command.split(" ", 2);
        var base = split[0];
        var arguments = split.length == 2 ? split[1] : "";
        return switch (base) {
            case "add" -> add(arguments);
            case "sub" -> sub(arguments);
            case "sqrt" -> sqrt(arguments);
            case "calc" -> calc(arguments);
            case "date" -> date(arguments);
            default -> throw new CommandParseException("Unknown command: " + base);
        };
    }

    /**
     * Takes two positional arguments:
     *  - {@code left: <your integer type>}
     *  - {@code right: <your integer type>}
     */
    private static Map<String, Object> add(String arguments) {
        String[] args = arguments.split(" "); // Split the string by whitespace
        if (args.length != 2) {
            throw new CommandParseException("Exactly two arguments are required for 'add'.");
        }

        // Parse the arguments
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
     */
    static Map<String, Object> sub(String arguments) {
        //TODO: Parse arguments and extract values.
        String[] args = arguments.split(" "); // Split the string by whitespace

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
            throw new CommandParseException("Invalid command structure for 'sub'. Usage: sub --left <value> --right <value>");
        }

        return left.equals(Optional.empty()) ? Map.of("left", left, "right", right) :
                Map.of("left", left.get(), "right", right);
    }

    /**
     * Takes one positional argument:
     *  - {@code number: <your integer type>} where {@code number >= 0}
     */
    static Map<String, Object> sqrt(String arguments) {
        //TODO: Parse arguments and extract values.
        String[] args = arguments.split(" ");
        if (args.length != 1) {
            throw new CommandParseException("Exactly one argument is required for 'sqrt'.");
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
     */
    static Map<String, Object> calc(String arguments) {
        //TODO: Parse arguments and extract values.
        String[] args = arguments.split(" ");
        if (args.length != 1) {
            throw new CommandParseException("Exactly one argument is required for 'calc'.");
        }
        String subcommand = args[0];
        if (subcommand.equals("add") || subcommand.equals("sub") || subcommand.equals("sqrt")) {
            return Map.of("subcommand", subcommand);
        }
        throw new CommandParseException("Unknown command: " + subcommand);
    }

    /**
     * Takes one positional argument:
     *  - {@code date: Date}, a custom type representing a {@code LocalDate}
     *    object (say at least yyyy-mm-dd, or whatever you prefer).
     *     - Note: Consider this a type that CANNOT be supported by your library
     *       out of the box and requires a custom type to be defined.
     */
    static Map<String, Object> date(String arguments) {
        //TODO: Parse arguments and extract values.
        String[] args = arguments.split(" ");
        if (args.length != 1) {
            throw new CommandParseException("Exactly one argument is required for 'date'.");
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
