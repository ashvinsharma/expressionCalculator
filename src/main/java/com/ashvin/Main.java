package com.ashvin;

import com.ashvin.calculator.Calculator;
import com.ashvin.calculator.exception.EvalException;
import com.ashvin.calculator.exception.ExpressionParseException;
import com.ashvin.calculator.exception.IllegalTokenException;
import com.ashvin.calculator.simple.SimpleCalculator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final var scanner = new Scanner(System.in);
        final Calculator calc = new SimpleCalculator();
        final String exitString = "exit";

        while(true) {
            System.out.print("> ");
            final var input = scanner.nextLine();
            if (input.equalsIgnoreCase(exitString)) return;

            try {
                calc.compile(input);
            } catch (IllegalTokenException | ExpressionParseException e) {
                System.err.printf("Compilation failed!\n%s\n", e.getMessage());
                continue;
            }
            try {
                System.out.println(calc.calculate());
            } catch (Exception e) {
                System.err.printf("FATAL! Computation failed!\n%s\n", e.getMessage());
            }
        }
    }
}
