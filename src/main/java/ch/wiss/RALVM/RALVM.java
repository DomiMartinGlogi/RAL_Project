package ch.wiss.RALVM;

import java.io.*;
import java.util.Scanner;

import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * The RALVM class represents a simple virtual machine that executes programs stored in a file.
 * It provides a static method to run a program.
 */
public class RALVM {

    /**
     * Parses a file and executes the program on an RAL_CPU.
     *
     * @param file the file to parse and execute
     */
    public static void run(String file) {
        System.out.println(ansi().fg(GREEN).a("RAL VM").reset());
        System.out.println(ansi().fg(GREEN).a("Version 0.5").reset());
        System.out.println(ansi().fg(GREEN).a("Created by:").reset());
        System.out.println(ansi().fg(GREEN).a("    - Dominik M. Glogowski").reset());
        System.out.println("Parsing " + file);

        File inputFile = new File(file);
        if (!inputFile.exists()) {
            errorHandler(file + " does not exist.");
        }

        int[] programMemory = new int[1024];

        try {
            Reader inputFileReader = new FileReader(inputFile);
            BufferedReader inputFileBufferedReader = new BufferedReader(inputFileReader);
            int lineNumber = 0;
            while (inputFileBufferedReader.ready()) {
                programMemory[lineNumber] = Integer.decode(inputFileBufferedReader.readLine().replace("\n", ""));
                lineNumber++;
            }
        } catch (Exception e) {
            errorHandler("An Exception occured while attempting to read the file " + file + " : " + e.getMessage());
        }

        System.out.println("File Parsed Successfully");

        System.out.println("Beginning Execution");
        RAL_CPU cpu0 = new RAL_CPU(programMemory);

        cpu0.run();
    }

    /**
     * Handles errors by displaying an error message, waiting for user input, and exiting the program.
     *
     * @param message the error message to display
     */
    private static void errorHandler(String message) {
        System.out.println(ansi().fg(RED).a(message).reset());
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        System.exit(1);
    }
}
