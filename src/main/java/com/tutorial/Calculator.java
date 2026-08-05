package com.tutorial;

/**
 * A deliberately tiny class. The point of this repo is NOT the code —
 * it's the pipeline that builds and tests this code automatically.
 */
public class Calculator {

    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    /** Throws on divide-by-zero so we have something meaningful to test. */
    public int divide(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return a / b;
    }
}
