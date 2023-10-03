package ch.wiss.RALVM;

import java.io.*;
import java.util.Scanner;

import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.ansi;

public class RALVM {

    public static void run(String file) {
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

    private static void errorHandler(String message) {
        System.out.println(ansi().fg(RED).a(message).reset());
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        System.exit(1);
    }
}
