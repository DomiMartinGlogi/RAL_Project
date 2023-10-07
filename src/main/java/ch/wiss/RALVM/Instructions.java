package ch.wiss.RALVM;

public enum Instructions {
    ADD(0b1000_0000),
    SUB(0b0100_0000),
    STA(0b0000_0001),
    STI(0b0000_0011),
    LDA(0b0000_0100),
    LDI(0b0000_1100),
    JMP(0b0001_0000),
    JMZ(0b0011_0000),
    DAT(0b0101_1010),
    HLT(0b1111_1111),
    NOP(0b0000_0000),
    PRN(0b1000_0000);

    private final int opcode;

    Instructions(int opcode) {
        this.opcode = opcode;
    }

    public int getOpcode() {
        return opcode;
    }
}

