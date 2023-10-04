# Proposal RAL Project

The goal of this Project is to write a simple RAL CPU in Java. The CPU is currently planned to have the following features:

- 32-bit Accumulator
- 16-bit Address Bus
- Harvard Architecture
- 32-bit Word Size
- 1024 Words of Program Memory
- 1024 Words of Data Memory
- Simple Instruction Set
- No native floating point support
- No native multiplication/division support
- No native bitwise operations
- No native stack support
- No native subroutine support
- No native I/O support
- No native interrupts
- Only unsigned integer support
- MSB first memory layout

The Program Memory is planned to take input data in form of a compiled pseudo binary file
in .txt format, containing the raw hexadecimal values of the instructions and their arguments as
32-bit integers.
Additionally the Compiler will be able to assign initial values to the Data Memory.

## Usage of the RAM

### Instruction Set

The instruction set is planned to encompass 10 Instructions. The instructions are listed below:
- `ADD x` - Add the value at memory address `x` to the accumulator
- `SUB x` - Subtract the value at memory address `x` from the accumulator
- `STA x` - Store the value of the accumulator at memory address `x`
- `STI x` - Store the value of the accumulator at the address stored at memory address `x`
- `LDA x` - Load the value at memory address `x` into the accumulator
- `LDI x` - Load the value at the address stored at memory address `x` into the accumulator
- `JMP x` - Jump to instruction at memory address `x`
- `JMZ x` - Jump to instruction at memory address `x` if the accumulator is zero
- `HLT`   - Halt the program
- `DAT x y` - Store the 8-bit value `y` at 8-bit memory address `x`

#### Instruction Encoding

Instructions are encoded as hexadecimal values. The encoding is planned to be the following:

| Instruction | Encoding      |
|-------------|---------------|
| `ADD`       | `0b1000 0000` |
| `SUB`       | `0b0100 0000` |
| `STA`       | `0b0000 0001` |
| `STI`       | `0b0000 0011` |
| `LDA`       | `0b0000 0100` |
| `LDI`       | `0b0000 1100` |
| `JMP`       | `0b0001 0000` |
| `JMZ`       | `0b0010 0000` |
| `HLT`       | `0b1111 1111` |
| `DAT`       | `0b0101 1010` |

The instructions will consist of 1 16-bit verb and 1 16-bit argument. The verb will be the first 16 bits of the instruction, the argument will be the last 16 bits of the instruction.

#### Future Expansion

The instruction set is planned to be expanded in the future to include the following instructions:
- `NOP` - No Operation
- `PRN` - Print the value of the accumulator to the console
- `INP` - Read a value from the console into the accumulator

As well as the following instructions for conditional jumps, however these can only be used to target addresses of at most 8-Bit.
- `JMG x y` - Jump to instruction at memory address `x` if the accumulator is greater than `y`
- `JML x y` - Jump to instruction at memory address `x` if the accumulator is less than `y`

### Memory

The Memory layout of the Program Memory is planned to be the following:

|  Byte0 | Byte1       | Byte2      | Byte3      |
|:------:|-------------|------------|------------|
| Unused | Instruction | MSB Target | LSB Target |

The 0th Byte is unused, but reserved for future expansion of the instruction set. The 1st Byte is the instruction, the 2nd and 3rd Byte are the target address of the instruction.
The 0th and 1st Byte are the verb of the instruction, the 2nd and 3rd Byte are the argument of the instruction.

The Data Memory has no special structure and purely contains 32-bit unsigned integers.

## Diagrams

### Compiler

#### Functional Diagram

![RAL_Compiler-Function Diagram.drawio.png](mdRessources%2Fimg%2FRAL_Compiler-Function%20Diagram.drawio.png)

#### Class Diagram

![RAL_Compiler-Class Diagram.png](mdRessources%2FRAL_Compiler-Class%20Diagram.png)

### CPU

#### Functional Diagram

# Tools Used

This project uses the following software tools:

### Integrated Development Environment (IDE):

- [IntelliJ IDEA 2023.2.2 Ultimate Edition](https://www.jetbrains.com/idea/download/): A capable and ergonomic Java IDE for professional developers.

### Programming Languages:

- [Java](https://www.oracle.com/java/technologies/javase-jdk20-downloads.html): This project is written in Java, using Java SDK version 20.

### Assistive Tools:

- [JetBrains AI Assistant](https://www.jetbrains.com/lp/ai-assistant/): An AI-powered coding assistant that provides coding assistance within IntelliJ IDEA.
- [Github Copilot](https://copilot.github.com/): An AI-powered code completion tool that learns from the code you write.

AI tools have been used solely for Debugging Purposes and Code Suggestions, design work has been fulfilled by human hand (and mind). 

# Libraries Used

This project makes use of the following libraries:

### [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/):
A project focused on all aspects of reusable Java components that provides a host of helper utilities for the java.lang API.

### [JUnit](https://junit.org/junit5/):
A simple and open-source framework to write repeatable tests. It is an instance of the xUnit architecture for unit testing frameworks.

### [Jansi](https://jansi.fusesource.org/):
Jansi is a small java library that allows you to use ANSI escape sequences to format your console output which works even on Windows.