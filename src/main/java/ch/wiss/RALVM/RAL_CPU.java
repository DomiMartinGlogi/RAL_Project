package ch.wiss.RALVM;

/**
 * The RAL_CPU class represents a RAL CPU (Random Access Language).
 * It executes instructions stored in the program memory and updates the
 * data memory and accumulator according to the instructions.
 */
public class RAL_CPU {
    private int[] programMemory;
    private int[] dataMemory;
    private int accumulator = 0;
    private int programCounter = 0;

    /**
     * Constructs a new RAL_CPU object with the given program memory.
     *
     * @param programMemory the program memory to be used by the RAL_CPU object
     */
    public RAL_CPU(int[] programMemory) {
        this.programMemory = programMemory;
        this.dataMemory = new int[1024];
    }

    /**
     * Executes the program stored in the program memory.
     * It iterates through each instruction in the program memory,
     * decodes the instruction, and performs the appropriate operation based on the instruction.
     * After executing the program, it prints the contents of the data memory.
     */
    public void run() {
        while(programCounter < programMemory.length) {
            int[] currentInstruction = decodeInstruction();
            Instructions instruction = getInstruction(currentInstruction[1]);

            if (instruction == null) {
                System.out.println("Undefined Instruction: " + currentInstruction[1] + " at Program Counter : " + programCounter);
                break;
            }

            switch (instruction) {
                case ADD -> accumulator += dataMemory[currentInstruction[2]];
                case SUB -> accumulator -= dataMemory[currentInstruction[2]];
                case STA -> dataMemory[currentInstruction[2]] = accumulator;
                case STI -> dataMemory[dataMemory[currentInstruction[2]]] = accumulator;
                case LDA -> accumulator = dataMemory[currentInstruction[2]];
                case LDI -> accumulator = dataMemory[dataMemory[currentInstruction[2]]];
                case JMP -> programCounter = currentInstruction[2] - 1;
                case JMZ -> {
                    if (accumulator != 0) {
                        programCounter++;
                    } else {
                        programCounter = currentInstruction[2] - 1;
                    }
                }
                case HLT -> {
                    System.out.println("Program terminated successfully.");
                    programCounter = programMemory.length;
                    break;
                }
                case DAT -> dataMemory[currentInstruction[2]] = currentInstruction[3];
            }
            programCounter++;
        }
        printMemory();
    }

    /**
     * Decodes the instruction at the current program counter.
     * Retrieves each byte of the instruction from the program memory and stores them in an integer array.
     *
     * @return An integer array that represents the decoded instruction.
     */
    private int[] decodeInstruction() {
        int[] instruction = new int[4];
        instruction[0] = (programMemory[programCounter] & 0xFF000000) >> 24;
        instruction[1] = (programMemory[programCounter] & 0x00FF0000) >> 16;
        instruction[2] = (programMemory[programCounter] & 0x0000FF00) >> 8;
        instruction[3] = programMemory[programCounter] & 0x000000FF;
        return instruction;
    }

    /**
     * Prints the contents of the program memory and data memory.
     * The program memory is printed in rows of the specified rowSize,
     * with each row starting with the memory address in hexadecimal format
     * and followed by the corresponding value in hexadecimal format.
     * The data memory is printed in the same format.
     */
    private void printMemory() {
        int rowSize = 16; // change this to modify the row size
        System.out.println("Program Memory:");
        for (int i = 0; i < programMemory.length; i++) {
            if (i % rowSize == 0)
                System.out.print("\n" + "0x" + Integer.toHexString(i));
            else
                System.out.print(", " + "0x" + Integer.toHexString(i));
            System.out.print(": " + Integer.toHexString(programMemory[i]));
        }

        System.out.println("\nData Memory:");
        for (int i = 0; i < dataMemory.length; i++) {
            if (i % rowSize == 0)
                System.out.print("\n" + "0x" + Integer.toHexString(i));
            else
                System.out.print(", " + "0x" + Integer.toHexString(i));
            System.out.print(": " + Integer.toHexString(dataMemory[i]));
        }
    }

    /**
     * Returns the instruction associated with the given opcode.
     *
     * @param opcode The opcode of the instruction.
     * @return The instruction associated with the given opcode, or null if no matching instruction is found.
     */
    private Instructions getInstruction(int opcode) {
        for (Instructions instruction : Instructions.values()) {
            if (instruction.getOpcode() == opcode) {
                return instruction;
            }
        }
        return null;
    }
}
