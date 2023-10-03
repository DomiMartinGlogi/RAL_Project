package ch.wiss.RALCompiler;

import org.fusesource.jansi.*;
import org.jetbrains.annotations.NotNull;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

import java.io.*;
import java.util.Map;
import java.util.Scanner;


public class Compiler {
    private static File inputFile = new File("program.txt");
    private static File outputFile = new File("program.txt");
    private static int lineNumber = 0;

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
            Map.entry("HLT", 0b1111_1111));


    /**
     * @brief Compiles the input file
     * @param file The input file
     */
    public static void compile(String file) {
        AnsiConsole.systemInstall();
        System.out.println(ansi().fg(GREEN).a("RAL Compiler").reset());
        System.out.println(ansi().fg(GREEN).a("Version 0.1").reset());
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
     * @brief Handles errors
     * @param message The error message
     */
    private static void errorHandler(String message) {
        System.out.println(ansi().fg(RED).a(message).reset());
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        System.exit(1);
    }

    /**
     * @brief Parses the input line and returns the parsed result.
     * @param line The line to parse.
     * @return The parsed line.
     */
    private static @NotNull String parseLine(@NotNull String line) {
        if (line.length() == 0 || line.startsWith("\n")) {
            return "\n";
        }

        String[] lineParts = line.split(" ");
        String instruction = lineParts.length > 0 ? lineParts[0] : "";
        String addressArg = "";
        String dataArg = "";

        if(!instruction.equals("HLT")) {
            addressArg = lineParts.length > 1 ? lineParts[1] : "";
            if(instruction.equals("DAT")) {
                dataArg = lineParts.length > 2 ? lineParts[2] : "";
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
            case "HLT":
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

    private static void validateArgument(String instruction, String argument, String argNumber) {
        if (argument.isEmpty()) {
            errorHandler("Missing argument " + argNumber + " on line " + lineNumber + " for instruction " + instruction + ".");
        }
    }

    private static int getHexValue(String instruction, String addressArg) {
        int resultHex = instructionSet.get(instruction);
        resultHex <<= 8;
        resultHex |= Integer.parseUnsignedInt(addressArg);

        return resultHex;
    }

    private static int getHexValueWithData(String instruction, String addressArg, String dataArg) {
        int resultHex = getHexValue(instruction, addressArg);
        resultHex <<= 8;
        resultHex |= Integer.parseUnsignedInt(dataArg);

        return resultHex;
    }
}
