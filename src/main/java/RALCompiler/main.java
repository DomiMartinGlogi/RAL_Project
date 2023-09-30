package RALCompiler;

import org.fusesource.jansi.*;
import org.jetbrains.annotations.NotNull;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

import java.io.*;
import java.util.Map;


public class main {
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

    public static void main(String[] args) {
        AnsiConsole.systemInstall();
        System.out.println(ansi().fg(GREEN).a("RAL Compiler").reset());
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
            errorHandler("Error opening input file");
        }
    }

    /**
     * @brief Handles errors
     * @param message The error message
     */
    private static void errorHandler(String message) {
        System.out.println(ansi().fg(RED).a(message).reset());
        System.exit(1);
    }

    /**
     * @brief Parses the input file and writes the output to the output file
     * @param line The line to parse
     * @return The parsed line
     */
    private static @NotNull String parseLine(String line) {
        String result = "";

        String instruction = line.split(" ")[0];
        String argument1 = line.split(" ")[1];

        if (line.length() == 0 || line.startsWith("\n")) {
            result.concat("\n");
            return result;
        } else if (!instructionSet.containsKey(instruction)) {
            errorHandler("Invalid instruction" + instruction + "on line " + lineNumber);
        }

        int resultHex = 0x0;

        switch (instruction) {
            case "ADD", "DAT", "SUB", "STA", "STI", "LDA", "LDI", "JMP", "JMZ": // These cases are all handled the same way
                resultHex = 0;
                resultHex |= instructionSet.get(instruction);
                resultHex <<= 8;
                resultHex |= Integer.parseInt(argument1);
                break;
            case "HLT":
                resultHex = 0;
                resultHex |= instructionSet.get(instruction);
                break;
        }

        result = Integer.toHexString(resultHex);
        result.concat("\n");

        return result;
    }
}
