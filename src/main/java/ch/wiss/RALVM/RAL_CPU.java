package ch.wiss.RALVM;

public class RAL_CPU {
    private int[] programMemory;
    private int[] dataMemory;
    private int accumulator = 0;
    private int programCounter = 0;

    public RAL_CPU(int[] programMemory) {
        this.programMemory = programMemory;
        this.dataMemory = new int[1024];
    }

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

    private int[] decodeInstruction() {
        int[] instruction = new int[4];
        instruction[0] = (programMemory[programCounter] & 0xFF000000) >> 24;
        instruction[1] = (programMemory[programCounter] & 0x00FF0000) >> 16;
        instruction[2] = (programMemory[programCounter] & 0x0000FF00) >> 8;
        instruction[3] = programMemory[programCounter] & 0x000000FF;
        return instruction;
    }

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

    private Instructions getInstruction(int opcode) {
        for (Instructions instruction : Instructions.values()) {
            if (instruction.getOpcode() == opcode) {
                return instruction;
            }
        }
        return null;
    }
}
