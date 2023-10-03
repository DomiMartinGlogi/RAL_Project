package ch.wiss;

import ch.wiss.RALCompiler.Compiler;
import ch.wiss.RALVM.RALVM;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java -jar RAL.jar -argument <input file>");
            System.exit(1);
        }
        if (args[0].equals("-compile")) {
            Compiler.compile(args[1]);
        } else if (args[0].equals("-run")) {
            RALVM.run(args[1]);
        } else if (args[0].equals("-help")) {
            System.out.println("Usage: java -jar RAL.jar -argument <input file>");
            System.out.println("Arguments:");
            System.out.println("    -compile <input file> - compiles the input file");
            System.out.println("    -run <input file> - runs the input file");
            System.out.println("    -help - displays this message");
            System.exit(1);
        }
        else {
            System.out.println("Usage: java -jar RAL.jar -argument <input file>");
            System.out.println("Arguments:");
            System.out.println("    -compile <input file> - compiles the input file");
            System.out.println("    -run <input file> - runs the input file");
            System.out.println("    -help - displays this message");
            System.exit(1);
        }
    }
}

