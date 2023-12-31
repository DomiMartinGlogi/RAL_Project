package ch.wiss.RALCompiler;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CompilerTest {

    @Test
    void compile() throws IOException {
        String filename = "test_program.txt";
        // Prepare file for testing with known content
        Files.write(Path.of(filename), "ADD 1\nSUB 2\nSTA 3\n".getBytes());
        Compiler.compile(filename);
        // Check that output file was created
        assertTrue(Files.exists(Path.of("program.txt")));
    }

    @Test
    void testParseLine() {
        assertEquals("0x800100\n",      Compiler.parseLine("ADD 1"));
        assertEquals("0x800100\n",      Compiler.parseLine("ADD 1 ; Test Comment"));
        assertEquals("0x400200\n",      Compiler.parseLine("SUB 2"));
        assertEquals("0x10300\n",       Compiler.parseLine("STA 3"));
        assertEquals("0x30100\n",       Compiler.parseLine("STI 1"));
        assertEquals("0x40400\n",       Compiler.parseLine("LDA 4"));
        assertEquals("0xc0500\n",       Compiler.parseLine("LDI 5"));
        assertEquals("0x100600\n",      Compiler.parseLine("JMP 6"));
        assertEquals("0x300700\n",      Compiler.parseLine("JMZ 7"));
        assertEquals("0x5a0809\n",      Compiler.parseLine("DAT 8 9"));
        assertEquals("0xff0000\n",      Compiler.parseLine("HLT"));
        assertEquals("0x0\n",           Compiler.parseLine("NOP"));
        assertEquals("0x800000\n",      Compiler.parseLine("PRN"));
    }
}