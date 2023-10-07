package ch.wiss.RALCompiler;

import org.fusesource.jansi.*;
import org.jetbrains.annotations.NotNull;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

import java.io.*;
import java.util.Map;
import java.util.Scanner;


/**
 * A class that represents a compiler for a specific programming language.
 * The compiler can parse an input file, compile it, and generate an output file with compiled code.
 */
public class Compiler {
    private static File inputFile = new File("program.txt");
    private static File outputFile = new File("program.txt");
    private static int lineNumber = 0;

    /**
     * Represents the instruction set used in the program.
     * Each instruction is mapped to its corresponding binary representation.
     */
    private static Map<String, Integer> instructionSet = Map.ofEntries(
            Map.entry("ADD", 0b1000_0000),
            Map.entry("SUB", 0b0100_0000),
            Map.entry("STA", 0b0000_0001),
            Map.entry("STI", 0b0000_0011),
            Map.entry("LDA", 0b0000_0100),
            Map.entry("LDI", 0b0000_1100),
            Map.entry("JMP", 0b0001_0000),
            Map.entry("JMZ", 0b0011_0000),
            Map.entry("DAT", 0b0101_1010),
            Map.entry("HLT", 0b1111_1111),
            Map.entry("NOP", 0b0000_0000),
            Map.entry("PRN", 0b1000_0000));


    /**
     * Compiles an input file and generates an output file.
     * Uses ANSI console to display compiler information.
     *
     * @param file The path of the input file.
     */
    public static void compile(String file) {
        AnsiConsole.systemInstall();
        System.out.println(ansi().fg(GREEN).a("RAL Compiler").reset());
        System.out.println(ansi().fg(GREEN).a("Version 0.5").reset());
        System.out.println(ansi().fg(GREEN).a("Created by:").reset());
        System.out.println(ansi().fg(GREEN).a("    - Dominik M. Glogowski").reset());
        inputFile = new File(file);
        if (!inputFile.exists()) {
            errorHandler("Input file does not exist");
        }
        try {
            Reader inputFileReader = new FileReader(inputFile);
            BufferedReader inputStream = new BufferedReader(inputFileReader);

            if (outputFile.exists()) {
                outputFile.delete();
            }
            if(outputFile.createNewFile()) {
                System.out.println(ansi().fg(GREEN).a("Created output file").reset());
            } else {
                errorHandler("Error creating output file");
            }
            FileOutputStream outputStream = new FileOutputStream(outputFile);

            while(inputStream.ready()) {
                if (lineNumber >= 1024) {
                    errorHandler("Program is too long");
                }
                String line = inputStream.readLine();
                String outputLine = parseLine(line);
                outputStream.write(outputLine.getBytes());
                lineNumber++;
            }
        } catch (Exception e) {
            System.out.println(e);
            errorHandler("Error opening input file");
        }
    }

    /**
     * Handles an error by displaying the error message, waiting for user input, and exiting the program.
     *
     * @param message The error message to display.
     */
    private static void errorHandler(String message) {
        System.out.println(ansi().fg(RED).a(message).reset());
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        System.exit(1);
    }

    /**
     * Parses a line of assembly code and returns the corresponding hexadecimal instruction.
     *
     * @param line The line of assembly code to parse.
     * @return The hexadecimal instruction.
     * @note Neither Public, nor private so it can get tested in Unit Tests
     */
    static @NotNull String parseLine(@NotNull String line) {
        if (line.length() == 0 || line.startsWith("\n")) {
            return "\n";
        }
        line = line.split(";")[0];
        String[] lineParts = line.split(" ");
        String instruction = lineParts.length > 0 ? lineParts[0] : "";
        String addressArg = "";
        String dataArg = "";

        if(!instruction.equals("HLT")) {
            addressArg = lineParts.length > 1 ? lineParts[1] : "";
            if(instruction.equals("DAT")) {
                dataArg = lineParts.length > 2 ? lineParts[2] : "";
                if (((checkIfNumeric(addressArg) && Integer.parseUnsignedInt(addressArg) > 256))
                        || (checkIfNumeric(dataArg) && Integer.parseUnsignedInt(dataArg) > 256)) {
                    errorHandler("Attempting to access inaccessible Memory on line : " + lineNumber);
                }
            } else {
                if (checkIfNumeric(addressArg) && Integer.parseUnsignedInt(addressArg) > 1024) {
                    errorHandler("Attempting to access inaccessible Memory on line : " + lineNumber);
                }
            }
        }

        if (!instructionSet.containsKey(instruction)) {
            errorHandler("Invalid instruction" + instruction + "on line " + lineNumber);
        }

        int resultHex = 0x0;

        switch (instruction) {
            case "ADD", "SUB", "STA", "STI", "LDA", "LDI", "JMP", "JMZ":
                validateArgument(instruction, addressArg, "1");
                resultHex = getHexValue(instruction, addressArg);
                resultHex <<= 8;
                break;
            case "HLT", "NOP", "PRN":
                resultHex = instructionSet.get(instruction);
                resultHex <<= 16;
                break;
            case "DAT":
                validateArgument(instruction, addressArg, "1");
                validateArgument(instruction, dataArg, "2");
                resultHex = getHexValueWithData(instruction, addressArg, dataArg);
                break;
        }

        String result = Integer.toHexString(resultHex);
        result = result + "\n";
        result = "0x" + result;

        return result;
    }

    /**
     * Validates the argument for a given instruction on a specific line of assembly code.
     *
     * @param instruction The instruction for which the argument is being validated.
     * @param argument    The argument to validate.
     * @param argNumber   The number of the argument being validated.
     */
    private static void validateArgument(String instruction, String argument, String argNumber) {
        if (argument.isEmpty()) {
            errorHandler("Missing argument " + argNumber + " on line " + lineNumber + " for instruction " + instruction + ".");
        }
    }

    /**
     * Converts an instruction and an address argument into a hexadecimal value.
     *
     * @param instruction The instruction to convert.
     * @param addressArg  The address argument to convert.
     * @return The hexadecimal value obtained from the conversion.
     */
    private static int getHexValue(String instruction, String addressArg) {
        int resultHex = instructionSet.get(instruction);
        resultHex <<= 8;
        resultHex |= Integer.parseUnsignedInt(addressArg);

        return resultHex;
    }

    /**
     * Converts an instruction, an address argument, and a data argument into a hexadecimal value.
     *
     * @param instruction The instruction to convert.
     * @param addressArg  The address argument to convert.
     * @param dataArg     The data argument to convert.
     * @return The hexadecimal value obtained from the conversion.
     */
    private static int getHexValueWithData(String instruction, String addressArg, String dataArg) {
        int resultHex = getHexValue(instruction, addressArg);
        resultHex <<= 8;
        resultHex |= Integer.parseUnsignedInt(dataArg);

        return resultHex;
    }

    private static boolean checkIfNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
